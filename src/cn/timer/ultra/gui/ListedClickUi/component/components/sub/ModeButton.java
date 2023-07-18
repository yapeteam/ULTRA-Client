package cn.timer.ultra.gui.ListedClickUi.component.components.sub;

import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.gui.ListedClickUi.component.Component;
import cn.timer.ultra.gui.ListedClickUi.component.components.Button;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import cn.timer.ultra.values.Mode;

import java.awt.*;

public class ModeButton extends Component {

    private boolean hovered;
    private final Button parent;
    private final Mode value;
    private int x;
    private float y;
    private int modeIndex;

    public ModeButton(Mode value, Button button, int offset) {
        this.value = value;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.getOff();
        this.offset = offset;
        this.modeIndex = 0;
    }

    @Override
    public void setOff(float newOff) {
        this.offset = newOff;
    }

    @Override
    public void renderComponent() {
        RenderUtil.drawRect(parent.parent.getX() + 2, parent.parent.getY() + getOff(), parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + getOff() + 12, this.hovered ? new Color(182, 182, 182, 255).getRGB() : new Color(215, 214, 214, 255).getRGB());
        UniFontLoaders.jello18.drawString(value.getName() + ": " + value.getValue(), (parent.parent.getX() + 6), (parent.parent.getY() + getOff()) + 3, new Color(75, 75, 75, 255).getRGB());
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = isMouseOnButton(mouseX, mouseY);
        this.y = parent.parent.getY() + getOff();
        this.x = parent.parent.getX();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
            int maxIndex = value.getModes().length;
            if (modeIndex + 1 >= maxIndex)
                modeIndex = 0;
            else
                modeIndex++;
            value.setValue(value.getModes()[modeIndex]);
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}
