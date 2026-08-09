package net.rasanovum.rosetta.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

/** Stable access to a value whose loader controls when it becomes available. */
public interface RegistryHandle<T> extends Supplier<T> {
    ResourceLocation id();
}
