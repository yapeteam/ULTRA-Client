package cn.timer.ultra.gui.ClickUI.component.impl;

import cn.timer.ultra.gui.ClickUI.ClickUIScreen;
import cn.timer.ultra.gui.ClickUI.component.Component;
import cn.timer.ultra.gui.ClickUI.component.impl.values.BooleanValueComponent;
import cn.timer.ultra.gui.ClickUI.component.impl.values.ModeValueComponent;
import cn.timer.ultra.gui.ClickUI.component.impl.values.NumberValueComponent;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.ultra.animation.AnimationUtils;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import cn.timer.ultra.utils.ultra.render.RoundedUtil;
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
        for (Value<?> value : module.getValues()) {
            if (value instanceof Booleans) {
                subComponents.add(new BooleanValueComponent((Booleans) value));
            } else if (value instanceof Numbers) {
                subComponents.add(new NumberValueComponent((Numbers) value));
            } else if (value instanceof Mode) {
                subComponents.add(new ModeValueComponent((Mode) value));
            }
        }
    }

    private float process = 0;
    private final AnimationUtils ani = new AnimationUtils();

    private Color setAlpha(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), ClickUIScreen.globalAlpha);
    }

    @Override
    public void draw(float x, float y, float mouseX, float mouseY) {
        this.x = x;
        this.y = y;
        process = ani.animate((module.isEnabled() ? 6 : 0), process, 0.1f);
        float boxwidth = ClickUIScreen.width - ClickUIScreen.leftWidth - ClickUIScreen.rightWidth - 10;
        RoundedUtil.drawRound(x, y, boxwidth, 26, 3, ClickUIScreen.boxColor);
        UniFontLoaders.jigsaw18.drawString(module.getName(), x + 10, y + 10, module.isEnabled() ? setAlpha(new Color(0)).getRGB() : setAlpha(new Color(131, 131, 131)).getRGB());

        RoundedUtil.drawRound(x + boxwidth - 20, y + 9, 14, 8, 3, module.isEnabled() ? setAlpha(new Color(24, 144, 255)) : setAlpha(new Color(191, 191, 191)));

        RenderUtil.circle(x + boxwidth - 16 + process, y + 13, 3, new Color(255, 255, 255, ClickUIScreen.globalAlpha).getRGB());
    }

    public void drawSubComponents(float x, float y, float mouseX, float mouseY) {
        for (Component component : subComponents) {
            component.draw(x, y, mouseX, mouseY);
            y += 22;
        }
    }

    @Override
    public void mouseClicked(float mouseX, float mouseY, int mouseButton) {
        if (mouseX >= x && mouseX <= x + ClickUIScreen.width - ClickUIScreen.leftWidth - ClickUIScreen.rightWidth - 10 && mouseY >= y && mouseY <= y + 27) {
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
