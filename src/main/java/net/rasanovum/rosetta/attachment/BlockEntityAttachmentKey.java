package net.rasanovum.rosetta.attachment;

import dev.corgitaco.dataanchor.data.TrackedDataContainer;
import dev.corgitaco.dataanchor.data.registry.TrackedDataKey;
import dev.corgitaco.dataanchor.data.registry.TrackedDataRegistries;
import dev.corgitaco.dataanchor.data.type.blockentity.BlockEntityTrackedData;
import dev.corgitaco.dataanchor.data.type.blockentity.ServerBlockEntityTrackedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.rasanovum.rosetta.util.RegistryCompat;

import java.util.Optional;

public final class BlockEntityAttachmentKey<T> {
    private final TrackedDataKey<PersistentBlockEntityAttachment<T>> key;

    private BlockEntityAttachmentKey(TrackedDataKey<PersistentBlockEntityAttachment<T>> key) { this.key = key; }

    static <T> BlockEntityAttachmentKey<T> register(String namespace, String path,
            AttachmentFactory<BlockEntity, T> factory, AttachmentWriter<BlockEntity, T> writer,
            AttachmentReader<BlockEntity, T> reader) {
        AttachmentSupport.validate(path, factory, writer, reader);
        TrackedDataKey<PersistentBlockEntityAttachment<T>> key = TrackedDataRegistries.BLOCK_ENTITY.register(
                RegistryCompat.getLocation(namespace, path), type(),
                (trackedKey, blockEntity) -> new PersistentBlockEntityAttachment<>(trackedKey, blockEntity, factory, writer, reader));
        return new BlockEntityAttachmentKey<>(key);
    }

    public T getOrCreate(BlockEntity blockEntity) {
        TrackedDataContainer<BlockEntity, BlockEntityTrackedData> container = container(blockEntity)
                .orElseThrow(() -> new IllegalStateException("Cannot access attachments for block entity " + blockEntity.getBlockPos()));
        container.dataAnchor$createTrackedData();
        return find(container).orElseThrow(() -> new IllegalStateException(
                "Failed to create attachment for block entity " + blockEntity.getBlockPos()));
    }

    public Optional<T> find(BlockEntity blockEntity) { return container(blockEntity).flatMap(this::find); }
    public void markDirty(BlockEntity blockEntity) {
        container(blockEntity).flatMap(container -> container.dataAnchor$getTrackedData(key))
                .ifPresent(PersistentBlockEntityAttachment::markDirty);
    }

    private Optional<T> find(TrackedDataContainer<BlockEntity, BlockEntityTrackedData> container) {
        return container.dataAnchor$getTrackedData(key).map(PersistentBlockEntityAttachment::value);
    }

    private static Optional<TrackedDataContainer<BlockEntity, BlockEntityTrackedData>> container(BlockEntity blockEntity) {
        return Optional.ofNullable(TrackedDataRegistries.BLOCK_ENTITY.getContainer(blockEntity));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> Class<PersistentBlockEntityAttachment<T>> type() { return (Class) PersistentBlockEntityAttachment.class; }

    private static final class PersistentBlockEntityAttachment<T> extends ServerBlockEntityTrackedData {
        private final T value;
        private final AttachmentWriter<BlockEntity, T> writer;
        private final AttachmentReader<BlockEntity, T> reader;

        private PersistentBlockEntityAttachment(TrackedDataKey<? extends BlockEntityTrackedData> key,
                BlockEntity blockEntity, AttachmentFactory<BlockEntity, T> factory,
                AttachmentWriter<BlockEntity, T> writer, AttachmentReader<BlockEntity, T> reader) {
            super(key, blockEntity);
            this.value = factory.create(blockEntity);
            this.writer = writer;
            this.reader = reader;
        }

        private T value() { return value; }
        @Override public CompoundTag save() { return writer.save(get(), value); }
        @Override public void load(CompoundTag tag) { reader.load(get(), value, tag); }
    }
}
