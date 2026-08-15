package net.rasanovum.rosetta.loaders.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import net.rasanovum.rosetta.event.ServerHooks;

@Pseudo
@Mixin(targets = "net.minecraft.server.network.PlayerChunkSender")
public abstract class PlayerChunkSenderMixin {
    //? if <1.21 {
    /*
    *///?} else {
    @Inject(
            method = "sendChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V",
                    shift = At.Shift.AFTER))
    private static void rosetta$chunkSent(ServerGamePacketListenerImpl connection, ServerLevel level,
                                          LevelChunk chunk, CallbackInfo callbackInfo) {
        ServerHooks.chunkSent(connection.player, level, chunk);
    }

    @Inject(
            method = "dropChunk",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V",
                    shift = At.Shift.AFTER))
    private static void rosetta$chunkUnwatched(ServerPlayer player, ChunkPos pos, CallbackInfo callbackInfo) {
        ServerHooks.chunkUnwatched(player, (ServerLevel) player.level(), pos);
    }
    //?}
}
