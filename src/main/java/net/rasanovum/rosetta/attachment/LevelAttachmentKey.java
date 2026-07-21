package net.rasanovum.rosetta.attachment;

import dev.corgitaco.dataanchor.data.TrackedDataContainer;
import dev.corgitaco.dataanchor.data.registry.TrackedDataKey;
import dev.corgitaco.dataanchor.data.registry.TrackedDataRegistries;
import dev.corgitaco.dataanchor.data.type.level.LevelTrackedData;
import dev.corgitaco.dataanchor.data.type.level.ServerLevelTrackedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.rasanovum.rosetta.util.RegistryCompat;

import java.util.Optional;

/** A persistent value stored for each server level. */
public final class LevelAttachmentKey<T> {
    private final TrackedDataKey<PersistentLevelAttachment<T>> key;

    private LevelAttachmentKey(TrackedDataKey<PersistentLevelAttachment<T>> key) {
        this.key = key;
    }

    static <T> LevelAttachmentKey<T> register(
            String namespace, String path, AttachmentFactory<ServerLevel, T> factory,
            AttachmentWriter<ServerLevel, T> writer, AttachmentReader<ServerLevel, T> reader
    ) {
        AttachmentSupport.validate(path, factory, writer, reader);

        TrackedDataKey<PersistentLevelAttachment<T>> key = TrackedDataRegistries.LEVEL.register(
                RegistryCompat.getLocation(namespace, path),
                persistentType(),
                (trackedKey, level) -> level instanceof ServerLevel serverLevel
                        ? new PersistentLevelAttachment<>(trackedKey, serverLevel, factory, writer, reader)
                        : null
        );
        return new LevelAttachmentKey<>(key);
    }

    /** Gets the value for a level, creating it when first accessed. */
    public T getOrCreate(ServerLevel level) {
        TrackedDataContainer<Level, LevelTrackedData> container = container(level)
                .orElseThrow(() -> unavailable(level));
        container.dataAnchor$createTrackedData();
        return find(container).orElseThrow(() -> new IllegalStateException(
                "Failed to create level attachment for " + RegistryCompat.keyLocation(level.dimension())));
    }

    /** Gets the value when it is available. */
    public Optional<T> find(Level level) {
        return container(level).flatMap(this::find);
    }

    /** Marks the value for saving. */
    public void markDirty(ServerLevel level) {
        container(level).flatMap(container -> container.dataAnchor$getTrackedData(key))
                .ifPresent(PersistentLevelAttachment::markDirty);
    }

    private Optional<T> find(TrackedDataContainer<Level, LevelTrackedData> container) {
        return container.dataAnchor$getTrackedData(key).map(PersistentLevelAttachment::value);
    }

    private static Optional<TrackedDataContainer<Level, LevelTrackedData>> container(Level level) {
        return Optional.ofNullable(TrackedDataRegistries.LEVEL.getContainer(level));
    }

    private static IllegalStateException unavailable(ServerLevel level) {
        return new IllegalStateException(
                "Cannot access level attachments for " + RegistryCompat.keyLocation(level.dimension()));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> Class<PersistentLevelAttachment<T>> persistentType() {
        return (Class) PersistentLevelAttachment.class;
    }

    private static final class PersistentLevelAttachment<T> extends ServerLevelTrackedData {
        private final T value;
        private final AttachmentWriter<ServerLevel, T> writer;
        private final AttachmentReader<ServerLevel, T> reader;

        @SuppressWarnings({"unchecked", "rawtypes"})
        private PersistentLevelAttachment(
                TrackedDataKey<? extends ServerLevelTrackedData> key,
                ServerLevel level,
                AttachmentFactory<ServerLevel, T> factory,
                AttachmentWriter<ServerLevel, T> writer,
                AttachmentReader<ServerLevel, T> reader
        ) {
            super((TrackedDataKey) key, level);
            this.value = factory.create(level);
            this.writer = writer;
            this.reader = reader;
        }

        private T value() {
            return value;
        }

        @Override
        public CompoundTag save() {
            return writer.save(get(), value);
        }

        @Override
        public void load(CompoundTag tag) {
            reader.load(get(), value, tag);
        }
    }
}
