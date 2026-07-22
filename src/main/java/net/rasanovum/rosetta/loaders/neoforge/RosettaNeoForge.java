package net.rasanovum.rosetta.loaders.neoforge;

//? if neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.rasanovum.rosetta.attachment.AttachmentBootstrap;
import net.rasanovum.rosetta.network.NetworkBackend;

@Mod("rosetta_library")
public final class RosettaNeoForge {
    public RosettaNeoForge(IEventBus eventBus) {
        AttachmentBootstrap.initialize(eventBus);
        NeoForgeNetworkBackend backend = (NeoForgeNetworkBackend) NetworkBackend.INSTANCE;
        eventBus.addListener(backend::registerPayloads);
    }
}
*///?}
