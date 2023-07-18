package cn.timer.ultra.ListedClickUi.component.components.sub;

import cn.timer.ultra.ListedClickUi.component.Component;
import cn.timer.ultra.ListedClickUi.component.components.Button;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import org.lwjgl.input.Keyboard;

import java.awt.*;


public class Keybinding extends Component {
    private boolean hovered;
    private boolean binding;
    private final Button parent;
    private int x;
    private float y;

    public Keybinding(Button button, int offset) {
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.getOff();
        this.offset = offset;
    }

    @Override
    public void setOff(float newOff) {
        offset = newOff;
    }

    @Override
    public void renderComponent() {
        RenderUtil.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, this.hovered ? new Color(182, 182, 182, 255).getRGB() : new Color(215, 214, 214, 255).getRGB());
        UniFontLoaders.jello18.drawString(binding ? "Press a key..." : ("Key: " + Keyboard.getKeyName(this.parent.mod.getKey())), (parent.parent.getX() + 6), (parent.parent.getY() + offset + 0) * 1 + 3, new Color(75, 75, 75, 255).getRGB());
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = isMouseOnButton(mouseX, mouseY);
        this.y = parent.parent.getY() + offset;
        this.x = parent.parent.getX();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            this.binding = !this.binding;
        }
    }

    @Override
    public void keyTyped(char typedChar, int key) {
        if (this.binding) {
            if (key == Keyboard.KEY_DELETE) this.parent.mod.setKey(0);
            else this.parent.mod.setKey(key);
            this.binding = false;
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}
