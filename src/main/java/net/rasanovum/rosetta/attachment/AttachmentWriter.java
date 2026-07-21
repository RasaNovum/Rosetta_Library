package net.rasanovum.rosetta.attachment;

import net.minecraft.nbt.CompoundTag;

@FunctionalInterface
public interface AttachmentWriter<O, T> {
    CompoundTag save(O owner, T value);
}
