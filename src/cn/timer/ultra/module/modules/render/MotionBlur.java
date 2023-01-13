package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.module.modules.render.motionblur.MotionBlurResourceManager;
import cn.timer.ultra.values.Numbers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.Map;

public class MotionBlur extends Module {
    //public ModeSetting<MotionBlurType> type = new ModeSetting<>("Mode", MotionBlurType.GL);
    public static Numbers<Double> multiplier = new Numbers<>("FrameMultiplier", 0.0, 10.0, 1.0, 7.0);

    private Map domainResourceManagers;

    public MotionBlur() {
        super("MotionBlur", Keyboard.KEY_NONE, Category.Render);
        addValues(multiplier);
        t = multiplier.getValue();
    }

    @Override
    public void onDisable() {
        mc.entityRenderer.stopUseShader();
    }

    @Override
    public void onEnable() {
        if (this.domainResourceManagers == null) {
            try {
                Field[] var2;
                Field[] fieldArray = var2 = SimpleReloadableResourceManager.class.getDeclaredFields();
                int n = var2.length;
                int n2 = 0;
                while (n2 < n) {
                    Field field = fieldArray[n2];
                    if (field.getType() == Map.class) {
                        field.setAccessible(true);
                        this.domainResourceManagers = (Map) field.get(Minecraft.getMinecraft().getResourceManager());
                        break;
                    }
                    ++n2;
                }
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        }
        if (!this.domainResourceManagers.containsKey("motionblur")) {
            this.domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
        }
        if (MotionBlur.isFastRenderEnabled()) {
            //MiscUtils.addChat(EnumChatFormatting.RED + "MotionBlur: Plz turn off FastRender");
            super.setEnabled(false);
            return;
        }
        applyShader();
    }

    static boolean isFastRenderEnabled() {
        /*try {
            Field fastRender = GameSettings.class.getDeclaredField("ofFastRender");
            return fastRender.getBoolean(Minecraft.getMinecraft().gameSettings);
        }
        catch (Exception var1) {
            return false;
        }*/
        return false;
    }

    public void applyShader() {
        mc.entityRenderer.loadShader(new ResourceLocation("motionblur", "motionblur"));
        mc.entityRenderer.getShaderGroup().createBindFramebuffers(mc.displayWidth, mc.displayHeight);
    }

    double t;

    @EventTarget
    public void update(EventPreUpdate e) {
        if (t != multiplier.getValue()) {
            if (!(Minecraft.getMinecraft().entityRenderer.isShaderActive() || mc.theWorld == null || isFastRenderEnabled())) {
                applyShader();
            }
        }
        t = multiplier.getValue();
        if (this.domainResourceManagers == null) {
            try {
                Field[] var2;
                Field[] fieldArray = var2 = SimpleReloadableResourceManager.class.getDeclaredFields();
                int n = var2.length;
                int n2 = 0;
                while (n2 < n) {
                    Field field = fieldArray[n2];
                    if (field.getType() == Map.class) {
                        field.setAccessible(true);
                        this.domainResourceManagers = (Map) field.get(Minecraft.getMinecraft().getResourceManager());
                        break;
                    }
                    ++n2;
                }
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        }
        if (!this.domainResourceManagers.containsKey("motionblur")) {
            this.domainResourceManagers.put("motionblur", new MotionBlurResourceManager());
        }
        if (MotionBlur.isFastRenderEnabled()) {
            //MiscUtils.addChat(EnumChatFormatting.RED + "Motionblur: " + "Plz turn off FastRender");
            super.setEnabled(false);
        }
    }
}