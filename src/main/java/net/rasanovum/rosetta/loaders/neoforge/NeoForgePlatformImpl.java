package net.rasanovum.rosetta.loaders.neoforge;

//? if neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.rasanovum.rosetta.loaders.Platform;

public class NeoForgePlatformImpl implements Platform {
    @Override
    public boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    @Override
    public boolean isClientSide() {
        //? if >=26.1
        return FMLEnvironment.getDist() == Dist.CLIENT;
        //? if <26.1
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    @Override
    public boolean isServerSide() {
        //? if >=26.1
        return FMLEnvironment.getDist() == Dist.DEDICATED_SERVER;
        //? if <26.1
        return FMLEnvironment.dist == Dist.DEDICATED_SERVER;
    }

    @Override
    public String loader() {
        return "neoforge";
    }
}
*///?}
