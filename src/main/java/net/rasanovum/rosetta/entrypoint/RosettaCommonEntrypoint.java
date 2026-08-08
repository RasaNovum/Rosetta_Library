package net.rasanovum.rosetta.entrypoint;

import net.rasanovum.rosetta.event.ServerHooks;

/** Implemented by a mod that delegates loader startup and lifecycle wiring to Rosetta. */
public interface RosettaCommonEntrypoint {
    default ServerHooks.Callbacks serverHooks() {
        return new ServerHooks.Callbacks() {};
    }

    default RosettaClientEntrypoint clientEntrypoint() {
        return null;
    }
}
