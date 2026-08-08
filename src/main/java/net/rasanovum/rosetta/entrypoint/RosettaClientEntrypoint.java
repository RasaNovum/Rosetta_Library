package net.rasanovum.rosetta.entrypoint;

import net.rasanovum.rosetta.event.ClientHooks;
import net.rasanovum.rosetta.event.ClientRenderHooks;

/** Loader-neutral client initialization plus lifecycle and render callbacks. */
public interface RosettaClientEntrypoint extends ClientHooks.Callbacks, ClientRenderHooks.Callbacks {
    default void initialize() {}
}
