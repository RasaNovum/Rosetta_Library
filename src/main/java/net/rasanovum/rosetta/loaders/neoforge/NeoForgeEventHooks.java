package net.rasanovum.rosetta.loaders.neoforge;

//? if neoforge {
/*import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.rasanovum.rosetta.event.ServerHooks;

@EventBusSubscriber(modid = "rosetta_library")
public final class NeoForgeEventHooks {
    private static MinecraftServer server;

    private NeoForgeEventHooks() {}

    @SubscribeEvent
    public static void playerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) ServerHooks.playerJoined(player);
    }

    @SubscribeEvent
    public static void playerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) ServerHooks.playerLeft(player);
    }

    @SubscribeEvent
    public static void levelTick(LevelTickEvent.Post event) {
        if (event.getLevel() instanceof ServerLevel level) ServerHooks.serverLevelTick(level);
    }

    @SubscribeEvent
    public static void serverStarting(ServerStartingEvent event) {
        server = event.getServer();
        ServerHooks.serverStarting(server);
    }

    @SubscribeEvent
    public static void serverStopping(ServerStoppingEvent event) {
        ServerHooks.serverStopping(event.getServer());
        server = null;
    }

    @SubscribeEvent
    public static void dataPackReloaded(TagsUpdatedEvent event) {
        if (server != null) ServerHooks.dataPackReloaded(server);
    }

    @SubscribeEvent
    public static void dimensionChanged(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) ServerHooks.playerChangedDimension(player);
    }

    @SubscribeEvent
    public static void blockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player &&
                !ServerHooks.beforeBlockBreak(event.getLevel(), event.getPos(), player)) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        ServerHooks.registerCommands(event.getDispatcher());
    }
}
*///?}
