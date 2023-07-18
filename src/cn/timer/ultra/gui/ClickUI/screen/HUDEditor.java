package cn.timer.ultra.gui.ClickUI.screen;

import cn.timer.ultra.Client;
import cn.timer.ultra.gui.ClickUI.ClickUIScreen;
import cn.timer.ultra.gui.ClickUI.Panels;
import cn.timer.ultra.gui.ClickUI.component.impl.ModuleComponent;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.module.HUDModule;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.soar.ClickEffect;
import cn.timer.ultra.utils.ultra.MathUtils;
import cn.timer.ultra.utils.ultra.render.RoundedUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HUDEditor extends GuiScreen {
    private final GuiScreen parent;
    private final List<ClickEffect> clickEffects = new ArrayList<>();

    public HUDEditor(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (Module module : Client.instance.getModuleManager().getModules()) {
            if (module instanceof HUDModule) {
                if (module.isEnabled())
                    update((HUDModule) module, mouseX, mouseY);
            }
        }
        if (clickEffects.size() > 0) {
            Iterator<ClickEffect> clickEffectIterator = clickEffects.iterator();
            while (clickEffectIterator.hasNext()) {
                ClickEffect clickEffect = clickEffectIterator.next();
                clickEffect.draw();
                if (clickEffect.canRemove()) clickEffectIterator.remove();
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        clickEffects.add(new ClickEffect(mouseX, mouseY));
    }

    public void updateAlpha(boolean selected, HUDModule module) {
        module.alpha = fade(selected ? 255 : 0, module.alpha, 10);
    }

    private int fade(int target, int current, int speed) {
        if (MathUtils.getDifference(target, current) <= speed) {
            return target;
        }
        return target > current ? current + speed : current - speed;
    }

    public boolean isHovering(int mouseX, int mouseY, HUDModule module) {
        return mouseX >= module.getXPosition() && mouseY >= module.getYPosition() && mouseX <= module.getXPosition() + module.width && mouseY <= module.getYPosition() + module.height;
    }

    public boolean isHoveringOnSettings(int mouseX, int mouseY, HUDModule module) {
        return mouseX >= module.getXPosition() && mouseY >= module.getYPosition() && mouseX <= module.getXPosition() + UniFontLoaders.icon20.getStringWidth("N") && mouseY <= module.getYPosition() + UniFontLoaders.icon20.getHeight();
    }

    public void drawOutline(int mouseX, int mouseY, HUDModule module) {
        if (module.alpha != 0) {
            RoundedUtil.drawRoundOutline(module.getXPosition() - 5, module.getYPosition() - 5, module.width + 10, module.height + 10, 5, 2, new Color(0, 0, 0, 0), new Color(255, 255, 255, module.alpha));
            UniFontLoaders.icon20.drawString("N", module.getXPosition(), module.getYPosition(), isHoveringOnSettings(mouseX, mouseY, module) ? Client.instance.getClientColor().getRGB() : new Color(255, 255, 255, module.alpha).getRGB());
        }
    }

    public void update(HUDModule module, int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        module.subXPosition.setMax(sr.getScaledWidth() - module.width);
        module.subYPosition.setMax(sr.getScaledHeight() - module.height);
        drawOutline(mouseX, mouseY, module);
        updateAlpha(isHovering(mouseX, mouseY, module), module);
        if (isHovering(mouseX, mouseY, module)) {
            String str = module.getName() + ":";
            String str1 = String.format("PosX: %.1f", module.getXPosition());
            String str2 = String.format("PosY: %.1f", module.getYPosition());
            RoundedUtil.drawRound(mouseX, mouseY, Math.max(Math.max(UniFontLoaders.PingFangMedium18.getStringWidth(str1), UniFontLoaders.PingFangMedium18.getStringWidth(str2)), UniFontLoaders.PingFangMedium18.getStringWidth(str)) + 10, UniFontLoaders.PingFangMedium18.getHeight() * 3 + 14, 4, new Color(255, 255, 255, (int) (module.alpha * 0.9f)));
            UniFontLoaders.PingFangLight18.drawString(str, mouseX + 5, mouseY + 5, 0);
            UniFontLoaders.PingFangLight18.drawString(str1, mouseX + 5, mouseY + 5 + UniFontLoaders.PingFangMedium18.getHeight() + 2, 0);
            UniFontLoaders.PingFangLight18.drawString(str2, mouseX + 5, mouseY + 5 + (UniFontLoaders.PingFangMedium18.getHeight() + 2) * 2, 0);
        }
        if (isHoveringOnSettings(mouseX, mouseY, module) && Mouse.isButtonDown(0) && noDrag()) {
            Client.instance.clickGui.setCurrentPanel(Panels.Modules);
            ClickUIScreen.currentModule = new ModuleComponent(module);
            mc.displayGuiScreen(parent);
        }
        if (isHovering(mouseX, mouseY, module) && Mouse.isButtonDown(0) && noDrag()) {
            module.dragging = true;
            module.dragX = mouseX - module.subXPosition.floatValue();
            module.dragY = mouseY - module.subYPosition.floatValue();
        }
        if (!Mouse.isButtonDown(0)) {
            module.dragging = false;
        }
        if (module.dragging) {
            module.horizontalFacing.setValue("free");
            module.verticalFacing.setValue("free");
            if (module.horizontalFacing.getValue().equals("free"))
                module.subXPosition.setValue((float) mouseX - module.dragX);
            if (module.verticalFacing.getValue().equals("free"))
                module.subYPosition.setValue((float) mouseY - module.dragY);
            if (module.subXPosition.floatValue() < 0) {
                module.subXPosition.setValue(0f);
                module.horizontalFacing.setValue("left");
            } else if (module.subXPosition.floatValue() + module.width > sr.getScaledWidth()) {
                module.subXPosition.setValue(sr.getScaledWidth() - module.width);
                module.horizontalFacing.setValue("right");
            }
            if (module.subYPosition.floatValue() < 0) {
                module.subYPosition.setValue(0f);
                module.verticalFacing.setValue("top");
            } else if (module.subYPosition.floatValue() + module.height > sr.getScaledHeight()) {
                module.subYPosition.setValue(sr.getScaledHeight() - module.height);
                module.verticalFacing.setValue("bottom");
            }
        }
    }

    private boolean noDrag() {
        for (Module module : Client.instance.getModuleManager().modules) {
            if (module instanceof HUDModule) {
                if (((HUDModule) module).dragging) return false;
            }
        }
        return !Client.instance.clickGui.dragging && !Client.instance.clickGui.sizeDragging;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parent);
        }
    }
}
