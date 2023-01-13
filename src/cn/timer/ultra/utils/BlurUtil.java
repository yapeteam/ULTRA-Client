package cn.timer.ultra.utils;

import net.minecraft.util.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.shader.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;

public class BlurUtil {
    private static Framebuffer buffer;
    private static final ResourceLocation shader;
    private static ShaderGroup blurShader;

    public static void initFboAndShader() {
        final Minecraft mc = Minecraft.getMinecraft();
        try {
            (blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), BlurUtil.shader)).createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            buffer = BlurUtil.blurShader.mainFramebuffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setStrength(int strength) {
        if (blurShader == null) return;
        blurShader.getListShaders().get(0).getShaderManager().getShaderUniform("Radius").set(strength);
        blurShader.getListShaders().get(1).getShaderManager().getShaderUniform("Radius").set(strength);
    }

    public static void crop(final float x, final float y, final float x2, final float y2) {
        final Minecraft mc = Minecraft.getMinecraft();
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        final int factor = scaledResolution.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((scaledResolution.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

    public static void blur(final float x, final float y, final float x2, final float y2) {
        //GlStateManager.disableAlpha();
        final Minecraft mc = Minecraft.getMinecraft();
        if (BlurUtil.buffer == null || BlurUtil.blurShader == null) {
            initFboAndShader();
        }
        GL11.glEnable(3089);
        crop(x, y, x2, y2);
        BlurUtil.buffer.framebufferHeight = mc.displayHeight;
        BlurUtil.buffer.framebufferWidth = mc.displayWidth;
        GlStateManager.resetColor();
        BlurUtil.blurShader.loadShaderGroup(mc.timer.renderPartialTicks);
        BlurUtil.buffer.bindFramebuffer(true);
        mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(3089);
        //GlStateManager.enableAlpha();
    }

    public static void blur2(final float x, final float y, final float x2, final float y2, final float h, final float w) {
        GlStateManager.disableAlpha();
        blur(x, y, x2 + w, y2 + h);
        GlStateManager.enableAlpha();
    }

    static {
        shader = new ResourceLocation("client/blur/blur.json");
    }
}
