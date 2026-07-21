package net.rasanovum.rosetta.attachment;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ProtoChunk;

/** Creates persistent attachment registries. */
public final class RosettaAttachments {
    private RosettaAttachments() {}

    public static LevelAttachments level(String namespace) {
        return new LevelAttachments(namespace);
    }

    public static EntityAttachments entity(String namespace) {
        return new EntityAttachments(namespace);
    }

    public static PlayerAttachments player(String namespace) {
        return new PlayerAttachments(namespace);
    }

    public static ChunkAttachments chunk(String namespace) {
        return new ChunkAttachments(namespace);
    }

    public static ProtoChunkAttachments protoChunk(String namespace) {
        return new ProtoChunkAttachments(namespace);
    }

    public static BlockEntityAttachments blockEntity(String namespace) {
        return new BlockEntityAttachments(namespace);
    }

    private static String namespace(String namespace) {
        if (namespace == null || namespace.isBlank()) {
            throw new IllegalArgumentException("Attachment namespace cannot be blank");
        }
        return namespace;
    }

    public static final class LevelAttachments {
        private final String namespace;

        private LevelAttachments(String namespace) {
            this.namespace = RosettaAttachments.namespace(namespace);
        }

        public <T> LevelAttachmentKey<T> persistent(
                String path,
                AttachmentFactory<ServerLevel, T> factory,
                AttachmentWriter<ServerLevel, T> writer,
                AttachmentReader<ServerLevel, T> reader
        ) {
            return LevelAttachmentKey.register(namespace, path, factory, writer, reader);
        }
    }

    public static final class EntityAttachments {
        private final String namespace;
        private EntityAttachments(String namespace) { this.namespace = RosettaAttachments.namespace(namespace); }

        public <T> EntityAttachmentKey<T> persistent(String path, AttachmentFactory<Entity, T> factory,
                AttachmentWriter<Entity, T> writer, AttachmentReader<Entity, T> reader) {
            return EntityAttachmentKey.register(namespace, path, factory, writer, reader);
        }
    }

    public static final class PlayerAttachments {
        private final String namespace;
        private PlayerAttachments(String namespace) { this.namespace = RosettaAttachments.namespace(namespace); }

        public <T> PlayerAttachmentKey<T> persistent(String path, AttachmentFactory<ServerPlayer, T> factory,
                AttachmentWriter<ServerPlayer, T> writer, AttachmentReader<ServerPlayer, T> reader) {
            return persistent(path, true, factory, writer, reader);
        }

        public <T> PlayerAttachmentKey<T> persistent(String path, boolean copyOnRespawn,
                AttachmentFactory<ServerPlayer, T> factory, AttachmentWriter<ServerPlayer, T> writer,
                AttachmentReader<ServerPlayer, T> reader) {
            return PlayerAttachmentKey.register(namespace, path, copyOnRespawn, factory, writer, reader);
        }
    }

    public static final class ChunkAttachments {
        private final String namespace;
        private ChunkAttachments(String namespace) { this.namespace = RosettaAttachments.namespace(namespace); }

        public <T> ChunkAttachmentKey<T> persistent(String path, AttachmentFactory<LevelChunk, T> factory,
                AttachmentWriter<LevelChunk, T> writer, AttachmentReader<LevelChunk, T> reader) {
            return ChunkAttachmentKey.register(namespace, path, factory, writer, reader);
        }
    }

    public static final class ProtoChunkAttachments {
        private final String namespace;
        private ProtoChunkAttachments(String namespace) { this.namespace = RosettaAttachments.namespace(namespace); }

        public <T> ProtoChunkAttachmentKey<T> persistent(String path, AttachmentFactory<ProtoChunk, T> factory,
                AttachmentWriter<ProtoChunk, T> writer, AttachmentReader<ProtoChunk, T> reader) {
            return ProtoChunkAttachmentKey.register(namespace, path, factory, writer, reader);
        }
    }

    public static final class BlockEntityAttachments {
        private final String namespace;
        private BlockEntityAttachments(String namespace) { this.namespace = RosettaAttachments.namespace(namespace); }

        public <T> BlockEntityAttachmentKey<T> persistent(String path, AttachmentFactory<BlockEntity, T> factory,
                AttachmentWriter<BlockEntity, T> writer, AttachmentReader<BlockEntity, T> reader) {
            return BlockEntityAttachmentKey.register(namespace, path, factory, writer, reader);
        }
    }
}
