package net.rasanovum.rosetta.attachment;

import dev.corgitaco.dataanchor.data.TrackedDataContainer;
import dev.corgitaco.dataanchor.data.registry.TrackedDataKey;
import dev.corgitaco.dataanchor.data.registry.TrackedDataRegistries;
import dev.corgitaco.dataanchor.data.type.entity.EntityTrackedData;
import dev.corgitaco.dataanchor.data.type.entity.ServerEntityTrackedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.rasanovum.rosetta.util.RegistryCompat;

import java.util.Optional;

public final class EntityAttachmentKey<T> {
    private final TrackedDataKey<PersistentEntityAttachment<T>> key;

    private EntityAttachmentKey(TrackedDataKey<PersistentEntityAttachment<T>> key) {
        this.key = key;
    }

    static <T> EntityAttachmentKey<T> register(String namespace, String path,
            AttachmentFactory<Entity, T> factory, AttachmentWriter<Entity, T> writer,
            AttachmentReader<Entity, T> reader) {
        AttachmentSupport.validate(path, factory, writer, reader);
        TrackedDataKey<PersistentEntityAttachment<T>> key = TrackedDataRegistries.ENTITY.register(
                RegistryCompat.getLocation(namespace, path), type(),
                (trackedKey, entity) -> new PersistentEntityAttachment<>(trackedKey, entity, factory, writer, reader));
        return new EntityAttachmentKey<>(key);
    }

    public T getOrCreate(Entity entity) {
        TrackedDataContainer<Entity, EntityTrackedData> container = container(entity)
                .orElseThrow(() -> new IllegalStateException("Cannot access attachments for entity " + entity.getId()));
        container.dataAnchor$createTrackedData();
        return find(container).orElseThrow(() -> new IllegalStateException(
                "Failed to create attachment for entity " + entity.getId()));
    }

    public Optional<T> find(Entity entity) {
        return container(entity).flatMap(this::find);
    }

    private Optional<T> find(TrackedDataContainer<Entity, EntityTrackedData> container) {
        return container.dataAnchor$getTrackedData(key).map(PersistentEntityAttachment::value);
    }

    private static Optional<TrackedDataContainer<Entity, EntityTrackedData>> container(Entity entity) {
        return Optional.ofNullable(TrackedDataRegistries.ENTITY.getContainer(entity));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> Class<PersistentEntityAttachment<T>> type() {
        return (Class) PersistentEntityAttachment.class;
    }

    private static final class PersistentEntityAttachment<T> extends ServerEntityTrackedData {
        private final T value;
        private final AttachmentWriter<Entity, T> writer;
        private final AttachmentReader<Entity, T> reader;

        private PersistentEntityAttachment(TrackedDataKey<? extends EntityTrackedData> key, Entity entity,
                AttachmentFactory<Entity, T> factory, AttachmentWriter<Entity, T> writer,
                AttachmentReader<Entity, T> reader) {
            super(key, entity);
            this.value = factory.create(entity);
            this.writer = writer;
            this.reader = reader;
        }

        private T value() { return value; }

        @Override public CompoundTag save() { return writer.save(get(), value); }
        @Override public void load(CompoundTag tag) { reader.load(get(), value, tag); }
    }
}
