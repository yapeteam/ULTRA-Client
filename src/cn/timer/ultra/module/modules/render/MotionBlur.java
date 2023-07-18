package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.values.Mode;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.util.Objects;

public class MotionBlur extends Module {
    public enum Modes {
        Little, Normal, Large;

        public static Modes get(String name) {
            for (Modes value : Modes.values()) {
                if (value.name().equalsIgnoreCase(name)) return value;
            }
            return null;
        }
    }

    private final Mode<String> mode;

    public MotionBlur() {
        super("MotionBlur", Keyboard.KEY_NONE, Category.Render);
        String[] modes = new String[Modes.values().length];
        for (int i = 0; i < Modes.values().length; i++) {
            modes[i] = Modes.values()[i].name();
        }
        this.mode = new Mode<>("Mode", modes, Modes.Normal.name());
        addValues(mode);
    }

    @Override
    public void onDisable() {
        Module.mc.entityRenderer.theShaderGroup = null;
    }

    @Override
    public void onEnable() {
        if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
            if (Module.mc.entityRenderer.theShaderGroup != null) {
                Module.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
            }
            switch (Objects.requireNonNull(Modes.get(mode.getValue()))) {
                case Little:
                    Module.mc.entityRenderer.loadShader(new ResourceLocation("MotionBlur/Little.json"));
                    break;
                case Normal:
                    Module.mc.entityRenderer.loadShader(new ResourceLocation("MotionBlur/Normal.json"));
                    break;
                case Large:
                    Module.mc.entityRenderer.loadShader(new ResourceLocation("MotionBlur/Large.json"));
                    break;
            }
        }
    }

    @EventTarget
    public void onUpdate(EventPreUpdate e) {
        setSuffix(mode.getValue());
        if (Module.mc.entityRenderer.theShaderGroup == null
                || !Module.mc.entityRenderer.theShaderGroup.getShaderGroupName().contains("MotionBlur")) {
            if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
                if (Module.mc.entityRenderer.theShaderGroup != null) {
                    Module.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
                }
                switch (mode.getValue()) {
                    case "Little":
                        Module.mc.entityRenderer.loadShader(new ResourceLocation("MotionBlur/Little.json"));
                        break;
                    case "Normal":
                        Module.mc.entityRenderer.loadShader(new ResourceLocation("MotionBlur/Normal.json"));
                        break;
                    case "Large":
                        Module.mc.entityRenderer.loadShader(new ResourceLocation("MotionBlur/Large.json"));
                        break;
                }
            }
        }
    }
}
