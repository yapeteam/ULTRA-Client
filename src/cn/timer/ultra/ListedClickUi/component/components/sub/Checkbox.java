package cn.timer.ultra.ListedClickUi.component.components.sub;

import cn.timer.ultra.ListedClickUi.component.Component;
import cn.timer.ultra.ListedClickUi.component.components.Button;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import cn.timer.ultra.values.Booleans;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Checkbox extends Component {

    private boolean hovered;
    private final Booleans op;
    private final Button parent;
    private int x;
    private float y;

    public Checkbox(Booleans option, Button button, int offset) {
        this.op = option;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.getOff();
        this.offset = offset;
    }

    @Override
    public void renderComponent() {
        RenderUtil.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + (parent.parent.getWidth()), parent.parent.getY() + offset + 12, this.hovered ? new Color(182, 182, 182, 255).getRGB() : new Color(215, 214, 214, 255).getRGB());
        UniFontLoaders.jello18.drawString(this.op.getName(), (parent.parent.getX()) + 15, parent.parent.getY() + offset + 2, new Color(75, 75, 75, 255).getRGB());
        String img = "icons/clickgui/disabled";
        final float x = parent.parent.getX() + 2 + 5 - 1;
        final float y = parent.parent.getY() + offset + 3;
        final float width = parent.parent.getX() + 9 + 3 - x;
        final float height = parent.parent.getY() + offset + 9 - y;
        RenderUtil.drawTexturedRect(x, y, width, height, img);
        if (Boolean.parseBoolean(this.op.getValue().toString())) {
            img = "icons/clickgui/enabled";
            Color c = new Color(0, 208, 255);
            GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1);
            RenderUtil.drawTexturedRect(x, y, width, height, img);
            GL11.glColor4f(1, 1, 1, 1);
        }
    }

    @Override
    public void setOff(float newOff) {
        offset = newOff;
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
            this.op.setValue(!Boolean.parseBoolean(this.op.getValue().toString()));
        }
    }

    public boolean isMouseOnButton(int x, int y) {
        return x > this.x && x < this.x + 88 && y > this.y && y < this.y + 12;
    }
}
