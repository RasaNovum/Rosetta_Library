package net.rasanovum.rosetta.network;

import dev.corgitaco.dataanchor.network.C2SNetworkContainer;
import dev.corgitaco.dataanchor.network.NetworkContainer;
import dev.corgitaco.dataanchor.network.Packet;
import dev.corgitaco.dataanchor.network.S2CNetworkContainer;
import dev.corgitaco.dataanchor.network.broadcast.PacketBroadcaster;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.rasanovum.rosetta.util.RegistryCompat;

import java.util.function.BiConsumer;
import java.util.function.Function;

//? if >=1.21
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/** Packet registration and transport. */
public final class RosettaNetwork {
    private RosettaNetwork() {}

    public static Channel channel(String namespace) {
        return new Channel(namespace);
    }

    public static void sendToServer(RosettaPacket packet) {
        PacketBroadcaster.C2S.sendToServer(packet);
    }

    public static void sendToPlayer(RosettaPacket packet, ServerPlayer player) {
        PacketBroadcaster.S2C.sendToPlayer(packet, player);
    }

    @FunctionalInterface
    public interface PacketHandler<T> {
        void handle(T packet, Level level, Player player);
    }

    public static final class Channel {
        private final String namespace;
        private final C2SNetworkContainer serverbound;
        private final S2CNetworkContainer clientbound;

        private Channel(String namespace) {
            this.namespace = namespace;
            this.serverbound = C2SNetworkContainer.of(namespace);
            this.clientbound = S2CNetworkContainer.of(namespace);
        }

        public <T extends RosettaPacket> Channel serverbound(
                String id, Class<T> type, BiConsumer<T, FriendlyByteBuf> writer,
                Function<FriendlyByteBuf, T> reader, PacketHandler<T> handler
        ) {
            register(serverbound, id, type, writer, reader, handler);
            return this;
        }

        public <T extends RosettaPacket> Channel clientbound(
                String id, Class<T> type, BiConsumer<T, FriendlyByteBuf> writer,
                Function<FriendlyByteBuf, T> reader, PacketHandler<T> handler
        ) {
            register(clientbound, id, type, writer, reader, handler);
            return this;
        }

        private <T extends RosettaPacket> void register(
                NetworkContainer container, String id, Class<T> type,
                BiConsumer<T, FriendlyByteBuf> writer, Function<FriendlyByteBuf, T> reader,
                PacketHandler<T> handler
        ) {
            PacketHandler<T> scheduled = (packet, level, player) -> {
                if (level.isClientSide()) {
                    ClientExecutor.execute(() -> handler.handle(packet, level, player));
                } else {
                    level.getServer().execute(() -> handler.handle(packet, level, player));
                }
            };

            //? if >=1.21 {
            var location = RegistryCompat.getLocation(namespace, id);
            var payloadType = new CustomPacketPayload.Type<T>(location);
            var codec = CustomPacketPayload.codec(writer::accept, reader::apply);
            RosettaPacket.registerType(type, payloadType);
            container.registerPacketHandler(new Packet.Handler(type, payloadType, codec,
                    (packet, level, player) -> scheduled.handle((T) packet, level, player)));
            //?} else {
            /*container.registerPacketHandler(id, new Packet.Handler(type, writer, reader,
                    (packet, level, player) -> scheduled.handle((T) packet, level, player)));
            *///?}
        }
    }

    private static final class ClientExecutor {
        private static void execute(Runnable action) {
            net.minecraft.client.Minecraft.getInstance().execute(action);
        }
    }
}
