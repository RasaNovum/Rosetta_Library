package net.rasanovum.rosetta.attachment;

@FunctionalInterface
public interface AttachmentFactory<O, T> {
    T create(O owner);
}
