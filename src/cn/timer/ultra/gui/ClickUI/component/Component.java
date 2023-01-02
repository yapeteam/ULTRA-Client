package cn.timer.ultra.gui.ClickUI.component;

public interface Component {
    void draw(float x, float y, float mouseX, float mouseY);

    void mouseClicked(float mouseX, float mouseY, int mouseButton);

    void mouseReleased(float mouseX, float mouseY, int state);
}
