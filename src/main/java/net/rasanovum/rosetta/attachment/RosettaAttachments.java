package net.rasanovum.rosetta.attachment;

/** Creates persistent attachment registries. */
public final class RosettaAttachments {
    private RosettaAttachments() {}

    public static LevelAttachments level(String namespace) {
        return new LevelAttachments(namespace);
    }

    public static final class LevelAttachments {
        private final String namespace;

        private LevelAttachments(String namespace) {
            if (namespace == null || namespace.isBlank()) {
                throw new IllegalArgumentException("Attachment namespace cannot be blank");
            }
            this.namespace = namespace;
        }

        public <T> LevelAttachmentKey<T> persistent(
                String path,
                LevelAttachmentKey.Factory<T> factory,
                LevelAttachmentKey.Writer<T> writer,
                LevelAttachmentKey.Reader<T> reader
        ) {
            return LevelAttachmentKey.register(namespace, path, factory, writer, reader);
        }
    }
}
