package cn.timer.ultra.ListedClickUi.component.components;

import cn.timer.ultra.ListedClickUi.component.Component;
import cn.timer.ultra.ListedClickUi.component.Frame;
import cn.timer.ultra.ListedClickUi.component.components.sub.Checkbox;
import cn.timer.ultra.ListedClickUi.component.components.sub.Keybinding;
import cn.timer.ultra.ListedClickUi.component.components.sub.ModeButton;
import cn.timer.ultra.ListedClickUi.component.components.sub.Slider;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import cn.timer.ultra.values.Booleans;
import cn.timer.ultra.values.Mode;
import cn.timer.ultra.values.Numbers;
import cn.timer.ultra.values.Value;

import java.awt.*;
import java.util.ArrayList;

public class Button extends Component {

    public Module mod;
    public Frame parent;
    private boolean isHovered;
    private final ArrayList<Component> subcomponents;
    public boolean open;
    private final int height;

    public Button(Module mod, Frame parent, int offset) {
        this.mod = mod;
        this.parent = parent;
        this.offset = offset;
        this.subcomponents = new ArrayList<>();
        this.open = false;
        height = 12;
        int opY = offset + 12;
        if (mod.getValues() != null) {
            for (Value s : mod.getValues()) {
                if (s instanceof Mode) {
                    this.subcomponents.add(new ModeButton((Mode) s, this, opY));
                    opY += 12;
                }
                if (s instanceof Numbers) {
                    this.subcomponents.add(new Slider((Numbers) s, this, opY));
                    opY += 12;
                }
                if (s instanceof Booleans) {
                    this.subcomponents.add(new Checkbox((Booleans) s, this, opY));
                    opY += 12;
                }
            }
        }
        this.subcomponents.add(new Keybinding(this, opY));
    }

    @Override
    public void setOff(float newOff) {
        offset = newOff;
        float opY = offset + 12;
        for (Component comp : this.subcomponents) {
            comp.setOff(opY);
            opY += 12;
        }
    }

    @Override
    public void renderComponent() {
        Color mainColor = new Color(0, 119, 255, 255);
        RenderUtil.drawRect(parent.getX(), this.parent.getY() + this.offset, parent.getX() + parent.getWidth(), this.parent.getY() + 12 + this.offset, this.isHovered ? (this.mod.isEnabled() ? mainColor.darker().getRGB() : new Color(182, 182, 182, 255).getRGB()) : (this.mod.isEnabled() ? mainColor.getRGB() : new Color(215, 214, 214, 255).getRGB()));
        UniFontLoaders.jello18.drawString(this.mod.getName(), (parent.getX() + 2) + 2, (parent.getY() + offset + 2) + 1, mod.isEnabled() ? new Color(255, 255, 255).getRGB() : new Color(75, 75, 75).getRGB());
        if (this.subcomponents.size() >= 2)
            UniFontLoaders.jello18.drawString(this.open ? "-" : "+", (parent.getX() + parent.getWidth() - 10), parent.getY() + offset + (height - UniFontLoaders.jello18.getHeight()) / 2, mod.isEnabled() ? new Color(255, 255, 255).getRGB() : new Color(75, 75, 75).getRGB());
        if (this.open) {
            if (!this.subcomponents.isEmpty()) {
                for (Component comp : this.subcomponents) {
                    comp.renderComponent();
                }
                RenderUtil.drawRect(parent.getX() + 2, parent.getY() + this.offset + 12, parent.getX() + 3, parent.getY() + this.offset + ((this.subcomponents.size() + 1) * 12), mainColor.getRGB());
            }
        }
    }


    @Override
    public int getHeight() {
        if (this.open) {
            return (12 * (this.subcomponents.size() + 1));
        }
        return 12;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.isHovered = isMouseOnButton(mouseX, mouseY);
        if (!this.subcomponents.isEmpty()) {
            for (Component comp : this.subcomponents) {
                comp.updateComponent(mouseX, mouseY);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && mouseY > 13) {
            this.mod.setEnabled();
        }
        if (isMouseOnButton(mouseX, mouseY) && button == 1 && mouseY > 13) {
            this.open = !this.open;
            this.parent.refresh();
        }
        for (Component comp : this.subcomponents) {
            comp.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Component comp : this.subcomponents) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        for (Component comp : this.subcomponents) {
            comp.keyTyped(typedChar, key);
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > parent.getX() && x < parent.getX() + parent.getWidth() && y > this.parent.getY() + this.offset && y < this.parent.getY() + 12 + this.offset;
    }

}
