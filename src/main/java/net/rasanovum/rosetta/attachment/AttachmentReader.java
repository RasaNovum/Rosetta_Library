package net.rasanovum.rosetta.attachment;

import net.minecraft.nbt.CompoundTag;

@FunctionalInterface
public interface AttachmentReader<O, T> {
    void load(O owner, T value, CompoundTag tag);
}
