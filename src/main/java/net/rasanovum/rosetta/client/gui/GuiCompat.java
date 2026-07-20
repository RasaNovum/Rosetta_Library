package net.rasanovum.rosetta.client.gui;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import com.mojang.math.Axis;

import java.util.List;

/** GUI drawing helpers. */
public class GuiCompat {

    private GuiCompat() {}

    public static void drawString(GuiGraphics guiGraphics, Font font, String text, int x, int y, int color, boolean shadow) {
        //? if >=26.1 {
        /*guiGraphics.text(font, text, x, y, opaque(color), shadow);
        *///?} else {
        guiGraphics.drawString(font, text, x, y, color, shadow);
        //?}
    }

    public static void drawString(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int color, boolean shadow) {
        //? if >=26.1 {
        /*guiGraphics.text(font, text, x, y, opaque(color), shadow);
        *///?} else {
        guiGraphics.drawString(font, text, x, y, color, shadow);
        //?}
    }

    public static void drawWidgetString(GuiGraphics guiGraphics, AbstractWidget widget, Font font, Component text, int x, int y, int color, boolean shadow) {
        //? if >=26.1 {
        /*guiGraphics.text(font, text.copy().withStyle(style -> style.withColor(color & 0xFFFFFF)), x, y, opaque(color), shadow);
        *///?} else {
        guiGraphics.drawString(font, text, x, y, color, shadow);
        //?}
    }

    public static void drawWidgetString(GuiGraphics guiGraphics, AbstractWidget widget, Font font, String text, int x, int y, int color, boolean shadow) {
        //? if >=26.1 {
        /*guiGraphics.text(font, Component.literal(text).withStyle(style -> style.withColor(color & 0xFFFFFF)), x, y, opaque(color), shadow);
        *///?} else {
        guiGraphics.drawString(font, text, x, y, color, shadow);
        //?}
    }

    public static void drawString(GuiGraphics guiGraphics, Font font, FormattedCharSequence text, int x, int y, int color, boolean shadow) {
        //? if >=26.1 {
        /*guiGraphics.text(font, text, x, y, opaque(color), shadow);
        *///?} else {
        guiGraphics.drawString(font, text, x, y, color, shadow);
        //?}
    }

    public static void drawCenteredString(GuiGraphics guiGraphics, Font font, Component text, int x, int y, int color) {
        //? if >=26.1 {
        /*guiGraphics.text(font, text, x - font.width(text) / 2, y, opaque(color), false);
        *///?} else {
        guiGraphics.drawCenteredString(font, text, x, y, color);
        //?}
    }

    public static void drawCenteredString(GuiGraphics guiGraphics, Font font, String text, int x, int y, int color) {
        //? if >=26.1 {
        /*guiGraphics.text(font, text, x - font.width(text) / 2, y, opaque(color), false);
        *///?} else {
        guiGraphics.drawCenteredString(font, text, x, y, color);
        //?}
    }

    public static void hLine(GuiGraphics guiGraphics, int x1, int x2, int y, int color) {
        //? if >=26.1 {
        /*guiGraphics.horizontalLine(x1, x2, y, color);
        *///?} else {
        guiGraphics.hLine(x1, x2, y, color);
        //?}
    }

    public static void vLine(GuiGraphics guiGraphics, int x, int y1, int y2, int color) {
        //? if >=26.1 {
        /*guiGraphics.verticalLine(x, y1, y2, color);
        *///?} else {
        guiGraphics.vLine(x, y1, y2, color);
        //?}
    }

    public static void renderOutline(GuiGraphics guiGraphics, int x, int y, int width, int height, int color) {
        //? if >=26.1 {
        /*guiGraphics.outline(x, y, width, height, color);
        *///?} else {
        guiGraphics.renderOutline(x, y, width, height, color);
        //?}
    }

    public static void blit(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int u, int v, int width, int height, int texWidth, int texHeight) {
        //? if >=26.1 {
        /*guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, texWidth, texHeight);
        *///?} else {
        guiGraphics.blit(texture, x, y, u, v, width, height, texWidth, texHeight);
        //?}
    }

    public static void blit(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int u, int v, int width, int height, int texWidth, int texHeight, int color) {
        //? if >=26.1 {
        /*guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, texture, x, y, u, v, width, height, texWidth, texHeight, color);
        *///?} else {
        applyShaderColor(color);
        guiGraphics.blit(texture, x, y, u, v, width, height, texWidth, texHeight);
        resetShaderColor();
        //?}
    }

    public static void blit(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, int texWidth, int texHeight) {
        //? if >=26.1 {
        /*guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, texture, x, y, (float) u, (float) v, width, height, uWidth, vHeight, texWidth, texHeight);
        *///?} else {
        guiGraphics.blit(texture, x, y, width, height, u, v, uWidth, vHeight, texWidth, texHeight);
        //?}
    }

    public static void blit(GuiGraphics guiGraphics, ResourceLocation texture, int x, int y, int width, int height, int u, int v, int uWidth, int vHeight, int texWidth, int texHeight, int color) {
        //? if >=26.1 {
        /*guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, texture, x, y, (float) u, (float) v, width, height, uWidth, vHeight, texWidth, texHeight, color);
        *///?} else {
        applyShaderColor(color);
        guiGraphics.blit(texture, x, y, width, height, u, v, uWidth, vHeight, texWidth, texHeight);
        resetShaderColor();
        //?}
    }

    public static void renderTooltip(GuiGraphics guiGraphics, Font font, Component tooltip, int x, int y) {
        //? if >=26.1 {
        /*guiGraphics.setTooltipForNextFrame(font, tooltip, x, y);
        *///?} else {
        guiGraphics.renderTooltip(font, tooltip, x, y);
        //?}
    }

    public static void renderComponentTooltip(GuiGraphics guiGraphics, Font font, List<Component> tooltip, int x, int y) {
        //? if >=26.1 {
        /*guiGraphics.setComponentTooltipForNextFrame(font, tooltip, x, y);
        *///?} else {
        guiGraphics.renderComponentTooltip(font, tooltip, x, y);
        //?}
    }

    public static void renderComponentHoverEffect(GuiGraphics guiGraphics, Font font, Style style, int x, int y) {
        if (style == null || style.getHoverEvent() == null) return;

        //? if >=26.1 {
        /*if (style.getHoverEvent() instanceof HoverEvent.ShowText showText) {
            guiGraphics.setTooltipForNextFrame(font, showText.value(), x, y);
        }
        *///?} else {
        guiGraphics.renderComponentHoverEffect(font, style, x, y);
        //?}
    }

    public static Style firstHoverStyle(Component component) {
        for (Component sibling : component.toFlatList()) {
            Style style = sibling.getStyle();
            if (style.getHoverEvent() != null) return style;
        }
        return null;
    }

    public static void pushPose(GuiGraphics guiGraphics) {
        //? if >=26.1 {
        /*guiGraphics.pose().pushMatrix();
        *///?} else {
        guiGraphics.pose().pushPose();
        //?}
    }

    public static void popPose(GuiGraphics guiGraphics) {
        //? if >=26.1 {
        /*guiGraphics.pose().popMatrix();
        *///?} else {
        guiGraphics.pose().popPose();
        //?}
    }

    public static void translate(GuiGraphics guiGraphics, float x, float y) {
        //? if >=26.1 {
        /*guiGraphics.pose().translate(x, y);
        *///?} else {
        guiGraphics.pose().translate(x, y, 0);
        //?}
    }

    public static void scale(GuiGraphics guiGraphics, float sx, float sy) {
        //? if >=26.1 {
        /*guiGraphics.pose().scale(sx, sy);
        *///?} else {
        guiGraphics.pose().scale(sx, sy, 1.0f);
        //?}
    }

    public static void rotateDegrees(GuiGraphics guiGraphics, float degrees) {
        //? if >=26.1 {
        /*guiGraphics.pose().rotate((float) Math.toRadians(degrees));
        *///?} else {
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(degrees));
        //?}
    }

    public static void nextStratum(GuiGraphics guiGraphics) {
        //? if >=26.1
        /*guiGraphics.nextStratum();*/
    }

    public static void enableBlend() {
        //? if <26.1
        com.mojang.blaze3d.systems.RenderSystem.enableBlend();
    }

    public static void disableBlend() {
        //? if <26.1
        com.mojang.blaze3d.systems.RenderSystem.disableBlend();
    }

    public static void setShaderColor(float r, float g, float b, float a) {
        //? if <26.1
        com.mojang.blaze3d.systems.RenderSystem.setShaderColor(r, g, b, a);
    }

    public static void defaultBlendFunc() {
        //? if <26.1
        com.mojang.blaze3d.systems.RenderSystem.defaultBlendFunc();
    }

    public static int argb(float r, float g, float b, float a) {
        int ai = Math.max(0, Math.min(255, Math.round(a * 255.0f)));
        int ri = Math.max(0, Math.min(255, Math.round(r * 255.0f)));
        int gi = Math.max(0, Math.min(255, Math.round(g * 255.0f)));
        int bi = Math.max(0, Math.min(255, Math.round(b * 255.0f)));
        return (ai << 24) | (ri << 16) | (gi << 8) | bi;
    }

    private static int withAlpha(int color, float alpha) {
        int sourceAlpha = (color >>> 24) & 0xFF;
        if (sourceAlpha == 0) sourceAlpha = 0xFF;
        int combinedAlpha = Math.max(0, Math.min(255, Math.round(sourceAlpha * alpha)));
        return (combinedAlpha << 24) | (color & 0xFFFFFF);
    }

    private static int opaque(int color) {
        return ((color >>> 24) & 0xFF) == 0 ? color | 0xFF000000 : color;
    }

    private static void applyShaderColor(int color) {
        float a = ((color >>> 24) & 0xFF) / 255.0f;
        float r = ((color >>> 16) & 0xFF) / 255.0f;
        float g = ((color >>> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        enableBlend();
        setShaderColor(r, g, b, a);
    }

    private static void resetShaderColor() {
        setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        disableBlend();
    }
}
