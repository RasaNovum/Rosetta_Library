package net.rasanovum.rosetta.loaders.fabric;

//? if fabric {
import net.fabricmc.api.ModInitializer;
import net.rasanovum.rosetta.attachment.AttachmentBootstrap;

public final class RosettaFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AttachmentBootstrap.initialize(null);
    }
}
//?}
