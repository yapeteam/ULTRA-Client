package cn.timer.ultra.gui.ClickUI.component.impl.values;

import cn.timer.ultra.gui.ClickUI.ClickUIScreen;
import cn.timer.ultra.gui.ClickUI.component.Component;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.utils.ultra.render.RoundedUtil;
import cn.timer.ultra.values.Mode;

import java.awt.*;

public class ModeValueComponent implements Component {
    Mode<String> value;
    float x, y;
    int modeIndex;

    public ModeValueComponent(Mode<String> setting) {
        this.value = setting;
        modeIndex = 0;
    }

    private Color setAlpha(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), ClickUIScreen.globalAlpha);
    }

    @Override
    public void draw(float x, float y, float mouseX, float mouseY) {
        this.x = x;
        this.y = y;
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        float width = ClickUIScreen.rightWidth - 10f;
        RoundedUtil.drawRound(lx, y, width, 15, 3, setAlpha(new Color(222, 222, 222)));
        UniFontLoaders.jigsaw18.drawString(value.getName() + ":" + value.getValue(), lx + (width - UniFontLoaders.jigsaw18.getStringWidth(value.getName() + ":" + value.getValue())) / 2f, y + 4, setAlpha(new Color(66, 66, 66)).getRGB());
    }

    @Override
    public void mouseClicked(float mouseX, float mouseY, int mouseButton) {
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        float width = ClickUIScreen.rightWidth - 10f;
        if (isHovered(lx + 5, y, width, 15, mouseX, mouseY)) {
            int maxIndex = value.getModes().length;
            if (modeIndex + 1 >= maxIndex)
                modeIndex = 0;
            else
                modeIndex++;
            value.setValue(value.getModes()[modeIndex]);
        }
    }

    public boolean isHovered(float x, float y, float width, float height, float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void mouseReleased(float mouseX, float mouseY, int state) {

    }
}
