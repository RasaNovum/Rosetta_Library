package net.rasanovum.rosetta.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/** Loader-neutral HUD and world render callbacks. */
public final class ClientRenderHooks {
    private static final List<Callbacks> CALLBACKS = new CopyOnWriteArrayList<>();

    private ClientRenderHooks() {}

    public static void register(Callbacks callbacks) {
        if (callbacks == null) throw new IllegalArgumentException("callbacks cannot be null");
        CALLBACKS.add(callbacks);
    }

    public static void renderHud(GuiGraphics graphics, float tickDelta) {
        CALLBACKS.forEach(c -> c.renderHud(graphics, tickDelta));
    }

    public static void renderWorld(PoseStack poseStack, ClientLevel level, Player player, float tickDelta, Frustum frustum) {
        CALLBACKS.forEach(c -> c.renderWorld(poseStack, level, player, tickDelta, frustum));
    }

    public interface Callbacks {
        default void renderHud(GuiGraphics graphics, float tickDelta) {}
        default void renderWorld(PoseStack poseStack, ClientLevel level, Player player, float tickDelta, Frustum frustum) {}
    }
}
