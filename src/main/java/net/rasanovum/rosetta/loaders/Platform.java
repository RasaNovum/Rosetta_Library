package net.rasanovum.rosetta.loaders;

public interface Platform {
    //? if fabric
    Platform INSTANCE = new net.rasanovum.rosetta.loaders.fabric.FabricPlatformImpl();
    //? if neoforge
    /*Platform INSTANCE = new net.rasanovum.rosetta.loaders.neoforge.NeoForgePlatformImpl();*/
    //? if forge
    /*Platform INSTANCE = new net.rasanovum.rosetta.loaders.forge.ForgePlatformImpl();*/


    boolean isModLoaded(String modid);
    boolean isClientSide();
    boolean isServerSide();
    String loader();
}
