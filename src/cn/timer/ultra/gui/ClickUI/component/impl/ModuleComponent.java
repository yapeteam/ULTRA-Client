package cn.timer.ultra.gui.ClickUI.component.impl;

import cn.timer.ultra.gui.ClickUI.ClickUIScreen;
import cn.timer.ultra.gui.ClickUI.component.Component;
import cn.timer.ultra.gui.ClickUI.component.impl.values.*;
import cn.timer.ultra.gui.Font.FontLoaders;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.RenderUtil;
import cn.timer.ultra.values.Booleans;
import cn.timer.ultra.values.Mode;
import cn.timer.ultra.values.Numbers;
import cn.timer.ultra.values.Value;

import java.awt.*;
import java.util.ArrayList;

public class ModuleComponent implements Component {
    public final Module module;
    ArrayList<Component> subComponents = new ArrayList<>();
    float x, y;

    public ModuleComponent(Module mod) {
        this.module = mod;
        for (Value<?> setting : module.getValues()) {
            if (setting instanceof Booleans) {
                subComponents.add(new BooleanValueComponent((Booleans) setting));
            } else if (setting instanceof Numbers) {
                subComponents.add(new NumberValueComponent((Numbers) setting));
            } else if (setting instanceof Mode) {
                subComponents.add(new ModeValueComponent((Mode) setting));
            }
        }
    }

    @Override
    public void draw(float x, float y, float mouseX, float mouseY) {
        this.x = x;
        this.y = y;
        float boxwidth = ClickUIScreen.width - ClickUIScreen.leftWidth - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) - 10;
        RenderUtil.drawRoundedRect(x, y, x + boxwidth, y + 26, 3, ClickUIScreen.boxColor.getRGB());
        FontLoaders.jigsaw18.drawString(module.getName(), x + 10, y + 10, module.isEnabled() ? new Color(0, 0, 0).getRGB() : new Color(131, 131, 131).getRGB());
        RenderUtil.drawRoundedRect2(x + boxwidth - 20, y + 9, x + boxwidth - 6, y + 17, module.isEnabled() ? new Color(24, 144, 255).getRGB() : new Color(191, 191, 191).getRGB());
        RenderUtil.circle(x + boxwidth - 16 + (module.isEnabled() ? 6 : 0), y + 13, 3, new Color(255, 255, 255).getRGB());
    }

    public void drawSubComponents(float x, float y, float mouseX, float mouseY) {
        for (Component component : subComponents) {
            component.draw(x, y, mouseX, mouseY);
            y += 20;
        }
    }

    @Override
    public void mouseClicked(float mouseX, float mouseY, int mouseButton) {
        if (mouseX >= x && mouseX <= x + ClickUIScreen.width - ClickUIScreen.leftWidth - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) - 10 && mouseY >= y && mouseY <= y + 27) {
            if (mouseButton == 0 && ClickUIScreen.y + ClickUIScreen.topHeight < mouseY) {
                module.setEnabled(!module.isEnabled());
            } else if (mouseButton == 1) {
                if (ClickUIScreen.currentModule == this)
                    ClickUIScreen.currentModule = null;
                else
                    ClickUIScreen.currentModule = this;
            }
        }
    }

    @Override
    public void mouseReleased(float mouseX, float mouseY, int state) {
        for (Component component : subComponents) {
            component.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void mouseClickedSubComponents(int mouseX, int mouseY, int mouseButton) {
        for (Component component : subComponents) {
            component.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
}
