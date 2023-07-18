package cn.timer.ultra.utils.ultra.render;

import cn.timer.ultra.utils.ultra.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.awt.*;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glUniform1;

public class ShadowUtils {

    public static ShaderUtil gaussianBloom = new ShaderUtil("client/shaders/shadow.frag");

    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void renderShadow(int sourceTexture, int radius, int offset, Color color) {
        framebuffer = GlUtils.createFrameBuffer(framebuffer);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.0f);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; i++) {
            weightBuffer.put(MathUtils.calculateGaussianValue(i, radius));
        }
        weightBuffer.rewind();

        GlUtils.setAlphaLimit(0.0F);

        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        gaussianBloom.init();
        setupUniforms(radius, offset, 0, weightBuffer, color);

        GlUtils.bindTexture(sourceTexture);
        ShaderUtil.drawQuads();
        gaussianBloom.unload();
        framebuffer.unbindFramebuffer();


        mc.getFramebuffer().bindFramebuffer(true);

        gaussianBloom.init();
        setupUniforms(radius, 0, offset, weightBuffer, color);

        GL13.glActiveTexture(GL13.GL_TEXTURE16);
        GlUtils.bindTexture(sourceTexture);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GlUtils.bindTexture(framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        gaussianBloom.unload();

        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableAlpha();

        GlStateManager.bindTexture(0);
    }

    public static void setupUniforms(int radius, int directionX, int directionY, FloatBuffer weights, Color color) {
        gaussianBloom.setUniformi("inTexture", 0);
        gaussianBloom.setUniformi("textureToCheck", 16);
        gaussianBloom.setUniformf("radius", radius);
        gaussianBloom.setUniformf("texelSize", 1.0F / (float) mc.displayWidth, 1.0F / (float) mc.displayHeight);
        gaussianBloom.setUniformf("direction", directionX, directionY);
        gaussianBloom.setUniformf("color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
        glUniform1(gaussianBloom.getUniform("weights"), weights);
    }
}