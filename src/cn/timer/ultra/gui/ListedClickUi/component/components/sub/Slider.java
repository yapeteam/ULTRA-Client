package cn.timer.ultra.gui.ListedClickUi.component.components.sub;

import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.gui.ListedClickUi.component.Component;
import cn.timer.ultra.gui.ListedClickUi.component.components.Button;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import cn.timer.ultra.values.Numbers;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Slider extends Component {

    private boolean hovered;

    private final Numbers value;
    private final Button parent;
    private int x;
    private float y;
    private boolean dragging = false;

    private double renderWidth;
    private final int width = 88;

    public Slider(Numbers value, Button button, int offset) {
        this.value = value;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.getOff();
        this.offset = offset;
    }

    @Override
    public void renderComponent() {
        RenderUtil.drawRect(parent.parent.getX() + 2, parent.parent.getY() + offset, parent.parent.getX() + parent.parent.getWidth(), parent.parent.getY() + offset + 12, this.hovered ? new Color(182, 182, 182, 255).getRGB() : new Color(215, 214, 214, 255).getRGB());
        RenderUtil.drawRect2(parent.parent.getX() + 2, parent.parent.getY() + offset, (float) (parent.parent.getX() + renderWidth - parent.parent.getX()), 12, new Color(150, 150, 150, 128).getRGB());
        UniFontLoaders.jello18.drawString(this.value.getName() + ": " + Double.parseDouble(value.getValue().toString()), (parent.parent.getX() + 6), (parent.parent.getY() + offset) + 3, new Color(75, 75, 75, 255).getRGB());
    }

    @Override
    public void setOff(float newOff) {
        offset = newOff;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered = isMouseOnButtonD(mouseX, mouseY) || isMouseOnButtonI(mouseX, mouseY);
        this.y = parent.parent.getY() + offset;
        this.x = parent.parent.getX();

        double diff = Math.min(width, Math.max(0, mouseX - this.x));

        double min = value.floatMin();
        double max = value.floatMax();

        renderWidth = (width - 2) * (Double.parseDouble(value.getValue().toString()) - min) / (max - min);
        if (dragging) {
            double newValue;
            if (diff == 0) {
                newValue = value.floatMin();
            } else {
                newValue = roundToPlace(((diff / width) * (max - min) + min));
            }
            if (value.getValue() instanceof Integer) {
                value.setValue(Integer.parseInt(String.valueOf((int) newValue)));
            } else if (value.getValue() instanceof Double) {
                value.setValue(newValue);
            } else if (value.getValue() instanceof Float) {
                value.setValue(Float.parseFloat(String.valueOf((float) newValue)));
            }
        }
    }

    private static double roundToPlace(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (isMouseOnButtonD(mouseX, mouseY) && button == 0 && this.parent.open) {
            dragging = true;
        }
        if (isMouseOnButtonI(mouseX, mouseY) && button == 0 && this.parent.open) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        dragging = false;
    }

    public boolean isMouseOnButtonD(int x, int y) {
        return x > this.x && x < this.x + (parent.parent.getWidth() / 2 + 1) && y > this.y && y < this.y + 12;
    }

    public boolean isMouseOnButtonI(int x, int y) {
        return x > this.x + parent.parent.getWidth() / 2 && x < this.x + parent.parent.getWidth() && y > this.y && y < this.y + 12;
    }
}