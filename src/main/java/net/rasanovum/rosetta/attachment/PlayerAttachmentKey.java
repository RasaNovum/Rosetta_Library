package net.rasanovum.rosetta.attachment;

import dev.corgitaco.dataanchor.data.TrackedDataContainer;
import dev.corgitaco.dataanchor.data.registry.TrackedDataKey;
import dev.corgitaco.dataanchor.data.registry.TrackedDataRegistries;
import dev.corgitaco.dataanchor.data.type.entity.EntityTrackedData;
import dev.corgitaco.dataanchor.data.type.entity.PlayerTrackedData;
import dev.corgitaco.dataanchor.data.type.entity.ServerPlayerTrackedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.rasanovum.rosetta.util.RegistryCompat;

import java.util.Optional;

public final class PlayerAttachmentKey<T> {
    private final TrackedDataKey<PersistentPlayerAttachment<T>> key;

    private PlayerAttachmentKey(TrackedDataKey<PersistentPlayerAttachment<T>> key) {
        this.key = key;
    }

    static <T> PlayerAttachmentKey<T> register(String namespace, String path, boolean copyOnRespawn,
            AttachmentFactory<ServerPlayer, T> factory, AttachmentWriter<ServerPlayer, T> writer,
            AttachmentReader<ServerPlayer, T> reader) {
        AttachmentSupport.validate(path, factory, writer, reader);
        TrackedDataKey<PersistentPlayerAttachment<T>> key = TrackedDataRegistries.ENTITY.register(
                RegistryCompat.getLocation(namespace, path), type(),
                (trackedKey, entity) -> entity instanceof ServerPlayer player
                        ? new PersistentPlayerAttachment<>(trackedKey, player, copyOnRespawn, factory, writer, reader)
                        : null);
        return new PlayerAttachmentKey<>(key);
    }

    public T getOrCreate(ServerPlayer player) {
        TrackedDataContainer<Entity, EntityTrackedData> container = container(player)
                .orElseThrow(() -> new IllegalStateException("Cannot access attachments for player " + player.getName().getString()));
        container.dataAnchor$createTrackedData();
        return find(container).orElseThrow(() -> new IllegalStateException(
                "Failed to create attachment for player " + player.getName().getString()));
    }

    public Optional<T> find(ServerPlayer player) {
        return container(player).flatMap(this::find);
    }

    private Optional<T> find(TrackedDataContainer<Entity, EntityTrackedData> container) {
        return container.dataAnchor$getTrackedData(key).map(PersistentPlayerAttachment::value);
    }

    private static Optional<TrackedDataContainer<Entity, EntityTrackedData>> container(ServerPlayer player) {
        return Optional.ofNullable(TrackedDataRegistries.ENTITY.getContainer(player));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> Class<PersistentPlayerAttachment<T>> type() {
        return (Class) PersistentPlayerAttachment.class;
    }

    private static final class PersistentPlayerAttachment<T> extends ServerPlayerTrackedData {
        private final T value;
        private final AttachmentWriter<ServerPlayer, T> writer;
        private final AttachmentReader<ServerPlayer, T> reader;

        private PersistentPlayerAttachment(TrackedDataKey<? extends PlayerTrackedData> key, ServerPlayer player,
                boolean copyOnRespawn, AttachmentFactory<ServerPlayer, T> factory,
                AttachmentWriter<ServerPlayer, T> writer, AttachmentReader<ServerPlayer, T> reader) {
            super(key, player, copyOnRespawn);
            this.value = factory.create(player);
            this.writer = writer;
            this.reader = reader;
        }

        private T value() { return value; }

        @Override public CompoundTag save() { return writer.save(get(), value); }
        @Override public void load(CompoundTag tag) { reader.load(get(), value, tag); }
    }
}
