package net.rasanovum.rosetta.attachment;

final class AttachmentSupport {
    private AttachmentSupport() {}

    static void validate(String path, Object... callbacks) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("Attachment path cannot be blank");
        }
        for (Object callback : callbacks) {
            if (callback == null) {
                throw new IllegalArgumentException("Attachment callbacks cannot be null");
            }
        }
    }
}
