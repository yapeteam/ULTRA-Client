package cn.timer.ultra.ListedClickUi.component;

public class Component {
    protected float offset;

    public void renderComponent() {

    }

    public void updateComponent(int mouseX, int mouseY) {

    }

    public void mouseClicked(int mouseX, int mouseY, int button) {

    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    public void keyTyped(char typedChar, int key) {
    }

    public void setOff(float newOff) {
        this.offset = newOff;
    }

    public float getOff() {
        return this.offset;
    }

    public int getHeight() {
        return 0;
    }
}
