package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventRender2D;
import cn.timer.ultra.gui.ClickUI.Panels;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.HUDModule;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.ultra.animation.AnimationUtils;
import cn.timer.ultra.utils.ultra.render.GradientUtil;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class HUD extends Module {
    public HUD() {
        super("HUD", Keyboard.KEY_NONE, Category.Render);
        this.setEnabled(true);
        for (int i = 0; i < Panels.values().length; i++) {
            CatA[i] = new AnimationUtils();
        }
    }

    AnimationUtils[] CatA = new AnimationUtils[Panels.values().length];

    @EventTarget
    public void onRender(EventRender2D e) {
        Color[] clientColors = Client.instance.getClientColors();
        GradientUtil.applyGradientHorizontal(5, 25, (float) UniFontLoaders.logo.getStringWidth("ULTRA"), UniFontLoaders.logo.getHeight(), 1, clientColors[0], clientColors[1], () -> {
            RenderUtil.setAlphaLimit(0);
            UniFontLoaders.logo.drawString("ULTRA", 5, 25, 0xffffffff);
        });
        for (Module module : Client.instance.getModuleManager().modules) {
            if (module instanceof HUDModule && module.isEnabled()) {
                ((HUDModule) module).getOverlay().run(e);
            }
        }
    }
}
