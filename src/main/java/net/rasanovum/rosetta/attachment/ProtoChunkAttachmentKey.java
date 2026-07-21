package net.rasanovum.rosetta.attachment;

import dev.corgitaco.dataanchor.data.TrackedDataContainer;
import dev.corgitaco.dataanchor.data.registry.TrackedDataKey;
import dev.corgitaco.dataanchor.data.registry.TrackedDataRegistries;
import dev.corgitaco.dataanchor.data.type.chunk.ChunkTrackedData;
import dev.corgitaco.dataanchor.data.type.chunk.ProtoChunkTrackedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.rasanovum.rosetta.util.RegistryCompat;

import java.util.Optional;

public final class ProtoChunkAttachmentKey<T> {
    private final TrackedDataKey<PersistentProtoChunkAttachment<T>> key;

    private ProtoChunkAttachmentKey(TrackedDataKey<PersistentProtoChunkAttachment<T>> key) { this.key = key; }

    static <T> ProtoChunkAttachmentKey<T> register(String namespace, String path,
            AttachmentFactory<ProtoChunk, T> factory, AttachmentWriter<ProtoChunk, T> writer,
            AttachmentReader<ProtoChunk, T> reader) {
        AttachmentSupport.validate(path, factory, writer, reader);
        TrackedDataKey<PersistentProtoChunkAttachment<T>> key = TrackedDataRegistries.CHUNK.register(
                RegistryCompat.getLocation(namespace, path), type(),
                (trackedKey, chunk) -> chunk instanceof ProtoChunk protoChunk
                        ? new PersistentProtoChunkAttachment<>(trackedKey, protoChunk, factory, writer, reader)
                        : null);
        return new ProtoChunkAttachmentKey<>(key);
    }

    public T getOrCreate(ProtoChunk chunk) {
        TrackedDataContainer<ChunkAccess, ChunkTrackedData> container = container(chunk)
                .orElseThrow(() -> new IllegalStateException("Cannot access attachments for proto-chunk " + chunk.getPos()));
        container.dataAnchor$createTrackedData();
        return find(container).orElseThrow(() -> new IllegalStateException(
                "Failed to create attachment for proto-chunk " + chunk.getPos()));
    }

    public Optional<T> find(ProtoChunk chunk) { return container(chunk).flatMap(this::find); }
    public void markDirty(ProtoChunk chunk) {
        container(chunk).flatMap(container -> container.dataAnchor$getTrackedData(key))
                .ifPresent(PersistentProtoChunkAttachment::markDirty);
    }

    private Optional<T> find(TrackedDataContainer<ChunkAccess, ChunkTrackedData> container) {
        return container.dataAnchor$getTrackedData(key).map(PersistentProtoChunkAttachment::value);
    }

    private static Optional<TrackedDataContainer<ChunkAccess, ChunkTrackedData>> container(ChunkAccess chunk) {
        return Optional.ofNullable(TrackedDataRegistries.CHUNK.getContainer(chunk));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> Class<PersistentProtoChunkAttachment<T>> type() { return (Class) PersistentProtoChunkAttachment.class; }

    private static final class PersistentProtoChunkAttachment<T> extends ProtoChunkTrackedData {
        private final T value;
        private final AttachmentWriter<ProtoChunk, T> writer;
        private final AttachmentReader<ProtoChunk, T> reader;

        private PersistentProtoChunkAttachment(TrackedDataKey<? extends ChunkTrackedData> key, ProtoChunk chunk,
                AttachmentFactory<ProtoChunk, T> factory, AttachmentWriter<ProtoChunk, T> writer,
                AttachmentReader<ProtoChunk, T> reader) {
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
