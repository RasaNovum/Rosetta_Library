package net.rasanovum.rosetta.attachment;

//? if forge {
/*import com.mojang.serialization.Codec;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.rasanovum.rosetta.Rosetta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ForgeAttachmentBackend<O, T> implements AttachmentBackend<O, T> {
    private static final ResourceLocation PROVIDER_ID = new ResourceLocation(Rosetta.MOD_ID, "attachments");
    private static final Capability<Store> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
    private static final Map<Object, Store> STORES = java.util.Collections.synchronizedMap(new WeakHashMap<>());
    private static final Map<String, Definition<?>> DEFINITIONS = new ConcurrentHashMap<>();
    private static boolean registered;

    private final Definition<T> definition;

    private ForgeAttachmentBackend(Definition<T> definition) { this.definition = definition; }

    static void register(Object modEventBus) {
        if (registered) return;
        ((IEventBus) modEventBus).addListener((RegisterCapabilitiesEvent event) -> event.register(Store.class));
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.<AttachCapabilitiesEvent<Entity>, Entity>addGenericListener(Entity.class, ForgeAttachmentBackend::attach);
        forgeBus.<AttachCapabilitiesEvent<BlockEntity>, BlockEntity>addGenericListener(BlockEntity.class, ForgeAttachmentBackend::attach);
        forgeBus.<AttachCapabilitiesEvent<Level>, Level>addGenericListener(Level.class, ForgeAttachmentBackend::attach);
        forgeBus.register(ForgeAttachmentBackend.class);
        registered = true;
    }

    static <O, T> AttachmentBackend<O, T> create(AttachmentKind kind, String namespace, String path,
            Supplier<T> factory, Codec<T> codec, boolean copyOnRespawn) {
        Definition<T> definition = new Definition<>(kind.path + "/" + namespace + "/" + path,
                factory, codec, copyOnRespawn);
        if (DEFINITIONS.putIfAbsent(definition.id(), definition) != null) {
            throw new IllegalArgumentException("Attachment was registered twice: " + definition.id());
        }
        return new ForgeAttachmentBackend<>(definition);
    }

    private static <O> void attach(AttachCapabilitiesEvent<O> event) {
        Store store = new Store();
        STORES.put(event.getObject(), store);
        event.addCapability(PROVIDER_ID, new Provider(store));
        event.addListener(() -> STORES.remove(event.getObject()));
    }

    private Store store(O owner) { return STORES.computeIfAbsent(owner, ignored -> new Store()); }
    public Optional<T> find(O owner) { return store(owner).find(definition); }
    public void set(O owner, T value) { store(owner).set(definition, value); dirty(owner); }
    public void remove(O owner) { store(owner).remove(definition); dirty(owner); }
    public void markDirty(O owner) { dirty(owner); }

    private void dirty(O owner) {
        if (owner instanceof ChunkAccess chunk) chunk.setUnsaved(true);
        else if (owner instanceof BlockEntity blockEntity) blockEntity.setChanged();
    }

    @SubscribeEvent
    public static void loadChunk(ChunkDataEvent.Load event) {
        Store store = STORES.computeIfAbsent(event.getChunk(), ignored -> new Store());
        if (event.getData().contains(Rosetta.MOD_ID + ":attachments", CompoundTag.TAG_COMPOUND))
            store.deserialize(event.getData().getCompound(Rosetta.MOD_ID + ":attachments"));
    }

    @SubscribeEvent
    public static void saveChunk(ChunkDataEvent.Save event) {
        Store store = STORES.get(event.getChunk());
        if (store != null) event.getData().put(Rosetta.MOD_ID + ":attachments", store.serialize());
    }

    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event) {
        if (!(event.getEntity() instanceof ServerPlayer)) return;
        event.getOriginal().reviveCaps();
        try {
            Store from = event.getOriginal().getCapability(CAPABILITY).orElse(null);
            Store to = event.getEntity().getCapability(CAPABILITY).orElse(null);
            if (from != null && to != null) to.copyRespawnValues(from);
        } finally {
            event.getOriginal().invalidateCaps();
        }
    }

    private record Definition<T>(String id, Supplier<T> factory, Codec<T> codec, boolean copyOnRespawn) {}

    static final class Store {
        private final Map<Definition<?>, Object> values = new IdentityHashMap<>();
        private CompoundTag unread = new CompoundTag();

        synchronized <T> Optional<T> find(Definition<T> definition) {
            Object value = values.get(definition);
            if (value != null) return Optional.of((T) value);
            if (!unread.contains(definition.id())) return Optional.empty();
            Optional<T> decoded = definition.codec().parse(NbtOps.INSTANCE, unread.get(definition.id()))
                    .resultOrPartial(Rosetta.LOGGER::error);
            decoded.ifPresent(result -> values.put(definition, result));
            return decoded;
        }

        synchronized <T> void set(Definition<T> definition, T value) { values.put(definition, value); }
        synchronized void remove(Definition<?> definition) { values.remove(definition); unread.remove(definition.id()); }
        synchronized void deserialize(CompoundTag tag) { unread = tag.copy(); }

        synchronized CompoundTag serialize() {
            CompoundTag result = unread.copy();
            values.forEach((rawDefinition, value) -> encode(result, rawDefinition, value));
            return result;
        }

        private static <T> void encode(CompoundTag result, Definition<T> definition, Object value) {
            definition.codec().encodeStart(NbtOps.INSTANCE, (T) value)
                    .resultOrPartial(Rosetta.LOGGER::error)
                    .ifPresent(tag -> result.put(definition.id(), tag));
        }

        synchronized void copyRespawnValues(Store source) {
            for (Definition<?> definition : DEFINITIONS.values()) {
                if (definition.copyOnRespawn()) copyValue(source, definition);
            }
        }

        private <T> void copyValue(Store source, Definition<T> definition) {
            source.find(definition).ifPresent(value -> values.put(definition, value));
        }
    }

    private record Provider(Store store) implements ICapabilitySerializable<CompoundTag> {
        public <C> @NotNull LazyOptional<C> getCapability(@NotNull Capability<C> cap, @Nullable Direction side) {
            return cap == CAPABILITY ? LazyOptional.of(() -> store).cast() : LazyOptional.empty();
        }
        public CompoundTag serializeNBT() { return store.serialize(); }
        public void deserializeNBT(CompoundTag tag) { store.deserialize(tag); }
    }
}
*///?}
