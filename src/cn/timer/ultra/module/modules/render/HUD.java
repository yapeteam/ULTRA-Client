package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.Client;
import cn.timer.ultra.gui.Font.FontLoaders;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.ColorUtils;
import cn.timer.ultra.utils.GradientUtil;
import cn.timer.ultra.utils.RenderUtil;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventRender2D;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

public class HUD extends Module {
    public HUD() {
        super("HUD", Keyboard.KEY_NONE, Category.Render);
        this.setEnabled(true);
    }

    @EventTarget
    public void onRender(EventRender2D e) {
        int width = new ScaledResolution(mc).getScaledWidth();
        int height = new ScaledResolution(mc).getScaledHeight();
        Color[] clientColors = Client.instance.getClientColors();
        GradientUtil.applyGradientHorizontal(5, 25, (float) FontLoaders.logo.getWidth("ULTRA"), FontLoaders.logo.getHeight(), 1, clientColors[0], clientColors[1], () -> {
            RenderUtil.setAlphaLimit(0);
            FontLoaders.logo.drawString("ULTRA", 5, 25, 0xffffffff);
        });
        ArrayList<Module> modulesHasEnabled = new ArrayList<>();
        for (Module module : Client.instance.getModuleManager().modules) {
            if (!module.isEnabled()) continue;
            modulesHasEnabled.add(module);
        }
        modulesHasEnabled.sort((compare1, compare2) -> FontLoaders.jello18.getStringWidth(compare2.getName() + compare2.getSuffix()) - FontLoaders.jello18.getStringWidth(compare1.getName() + compare1.getSuffix()));
        int y = 1;
        for (Module module : modulesHasEnabled) {
            int moduleWidth = FontLoaders.jello18.getStringWidth(module.getName() + module.getSuffix());
            if (!module.getSuffix().equals(""))
                FontLoaders.jello18.drawString(module.getSuffix(), width - FontLoaders.jello18.getStringWidth(module.getSuffix()), y, new Color(200, 200, 200).getRGB());
            FontLoaders.jello18.drawString(module.getName(), width - moduleWidth - 1, y, ColorUtils.rainbow(y * -20000000L, 1).getRGB());
            y += FontLoaders.jello18.FONT_HEIGHT;
        }
    }
}
