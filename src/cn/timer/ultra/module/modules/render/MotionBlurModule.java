package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventRender2D;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.values.Numbers;
import com.google.common.base.Throwables;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class MotionBlurModule extends Module {

    private final Numbers<Double> amount;
    private final Numbers<Double> color;

    public MotionBlurModule() {
        super("Motion Blur", Keyboard.KEY_NONE, Category.Render);
        this.amount = new Numbers<>("Amount", 1.0, 10.0, 1.0, 5.0);
        this.color = new Numbers<>("Color", -1.0, 1000.0, 1.0, -1.0);
        addValues(amount, color);
    }

    @EventTarget
    private void onLoad(EventRender2D cBTickEvent) {
        this.drawShader();
    }

    public void bindShader() {
        if (OpenGlHelper.isFramebufferEnabled() && OpenGlHelper.shadersSupported) {
            if (mc.entityRenderer.theShaderGroup != null) {
                mc.entityRenderer.theShaderGroup.deleteShaderGroup();
            }
            try {
                mc.entityRenderer.theShaderGroup = new ShaderGroup(mc.getTextureManager(), mc.entityRenderer.resourceManager, mc.getFramebuffer(), new ResourceLocation("shaders/post/motionblur.json"));
                mc.entityRenderer.theShaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            } catch (Exception ignored) {
            }
        }
    }

    private void drawShader() {
        bindShader();
        ShaderGroup shaderGroup = mc.entityRenderer.getShaderGroup();
        try {
            if (mc.entityRenderer.isShaderActive() && mc.thePlayer != null) {
                for (Shader shader : shaderGroup.listShaders) {
                    ShaderUniform uniform = shader.getShaderManager().getShaderUniform("Phosphor");
                    if (uniform == null) continue;
                    float f = 1.028125f * 0.68085104f + this.amount.getValue().floatValue() / (float) 100 * (float) 3 - 0.7f * 0.014285714f;
                    int n = this.color.getValue().intValue();
                    float f2 = (float) (n >> 16 & 0xFF) / (float) 255;
                    float f3 = (float) (n >> 8 & 0xFF) / (float) 255;
                    float f4 = (float) (n & 0xFF) / (float) 255;
                    uniform.set(f * f2, f * f3, f * f4);
                }
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            Throwables.propagate(illegalArgumentException);
        }
    }

}
