package net.rasanovum.rosetta.registry;

import java.util.Objects;

//? if forge {
/*import net.minecraftforge.eventbus.api.IEventBus;
*///?} else if neoforge {
/*import net.neoforged.bus.api.IEventBus;
*///?}

/** Loader-specific input needed when a {@link ModRegistrar} is attached. */
public final class RegistrationContext {
    //? if forge || neoforge {
    /*private final IEventBus eventBus;

    private RegistrationContext(IEventBus eventBus) {
        this.eventBus = Objects.requireNonNull(eventBus, "eventBus");
    }

    public static RegistrationContext create(IEventBus eventBus) {
        return new RegistrationContext(eventBus);
    }

    IEventBus eventBus() {
        return eventBus;
    }
    *///?} else {
    private RegistrationContext() {}

    public static RegistrationContext create() {
        return new RegistrationContext();
    }
    //?}
}
