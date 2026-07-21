package net.rasanovum.rosetta.attachment;

import dev.corgitaco.dataanchor.data.TrackedDataContainer;
import dev.corgitaco.dataanchor.data.registry.TrackedDataKey;
import dev.corgitaco.dataanchor.data.registry.TrackedDataRegistries;
import dev.corgitaco.dataanchor.data.type.chunk.ChunkTrackedData;
import dev.corgitaco.dataanchor.data.type.chunk.LevelChunkTrackedData;
import dev.corgitaco.dataanchor.data.type.chunk.ServerLevelChunkTrackedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.rasanovum.rosetta.util.RegistryCompat;

import java.util.Optional;

public final class ChunkAttachmentKey<T> {
    private final TrackedDataKey<PersistentChunkAttachment<T>> key;

    private ChunkAttachmentKey(TrackedDataKey<PersistentChunkAttachment<T>> key) { this.key = key; }

    static <T> ChunkAttachmentKey<T> register(String namespace, String path,
            AttachmentFactory<LevelChunk, T> factory, AttachmentWriter<LevelChunk, T> writer,
            AttachmentReader<LevelChunk, T> reader) {
        AttachmentSupport.validate(path, factory, writer, reader);
        TrackedDataKey<PersistentChunkAttachment<T>> key = TrackedDataRegistries.CHUNK.register(
                RegistryCompat.getLocation(namespace, path), type(),
                (trackedKey, chunk) -> chunk instanceof LevelChunk levelChunk
                        ? new PersistentChunkAttachment<>(trackedKey, levelChunk, factory, writer, reader)
                        : null);
        return new ChunkAttachmentKey<>(key);
    }

    public T getOrCreate(LevelChunk chunk) {
        TrackedDataContainer<ChunkAccess, ChunkTrackedData> container = container(chunk)
                .orElseThrow(() -> new IllegalStateException("Cannot access attachments for chunk " + chunk.getPos()));
        container.dataAnchor$createTrackedData();
        return find(container).orElseThrow(() -> new IllegalStateException(
                "Failed to create attachment for chunk " + chunk.getPos()));
    }

    public Optional<T> find(LevelChunk chunk) { return container(chunk).flatMap(this::find); }
    public void markDirty(LevelChunk chunk) {
        container(chunk).flatMap(container -> container.dataAnchor$getTrackedData(key))
                .ifPresent(PersistentChunkAttachment::markDirty);
    }

    private Optional<T> find(TrackedDataContainer<ChunkAccess, ChunkTrackedData> container) {
        return container.dataAnchor$getTrackedData(key).map(PersistentChunkAttachment::value);
    }

    private static Optional<TrackedDataContainer<ChunkAccess, ChunkTrackedData>> container(ChunkAccess chunk) {
        return Optional.ofNullable(TrackedDataRegistries.CHUNK.getContainer(chunk));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> Class<PersistentChunkAttachment<T>> type() { return (Class) PersistentChunkAttachment.class; }

    private static final class PersistentChunkAttachment<T> extends ServerLevelChunkTrackedData {
        private final T value;
        private final AttachmentWriter<LevelChunk, T> writer;
        private final AttachmentReader<LevelChunk, T> reader;

        private PersistentChunkAttachment(TrackedDataKey<? extends LevelChunkTrackedData> key, LevelChunk chunk,
                AttachmentFactory<LevelChunk, T> factory, AttachmentWriter<LevelChunk, T> writer,
                AttachmentReader<LevelChunk, T> reader) {
            super(key, chunk);
            this.value = factory.create(chunk);
            this.writer = writer;
            this.reader = reader;
        }

        private T value() { return value; }
        @Override public CompoundTag save() { return writer.save(get(), value); }
        @Override public void load(CompoundTag tag) { reader.load(get(), value, tag); }
    }
}
