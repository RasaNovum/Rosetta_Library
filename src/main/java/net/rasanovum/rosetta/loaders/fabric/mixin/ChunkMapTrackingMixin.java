package net.rasanovum.rosetta.loaders.fabric.mixin;

import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.rasanovum.rosetta.event.ServerHooks;

@Mixin(targets = "net.minecraft.server.level.ChunkMap")
public abstract class ChunkMapTrackingMixin {
    @Shadow @Final private ServerLevel level;

    //? if <1.21 {
    /*
    @Inject(method = "playerLoadedChunk", at = @At("RETURN"))
    private void rosetta$chunkSent(ServerPlayer player,
                                    MutableObject<ClientboundLevelChunkWithLightPacket> packetCache,
                                    LevelChunk chunk, CallbackInfo callbackInfo) {
        ServerHooks.chunkSent(player, level, chunk);
    }

    @Inject(
            method = "updateChunkTracking",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;untrackChunk(Lnet/minecraft/world/level/ChunkPos;)V",
                    shift = At.Shift.AFTER))
    private void rosetta$chunkUnwatched(ServerPlayer player, ChunkPos pos,
                                         MutableObject<ClientboundLevelChunkWithLightPacket> packetCache,
                                         boolean wasLoaded, boolean load, CallbackInfo callbackInfo) {
        ServerHooks.chunkUnwatched(player, level, pos);
    }
    *///?} else {
    //?}
}
