package cn.timer.ultra.event.events;

import cn.timer.ultra.event.Event;
import net.minecraft.client.gui.GuiScreen;

public class EventDrawGui extends Event {
    private final int mouseX;
    private final int mouseY;
    private final GuiScreen currentScreen;

    public EventDrawGui(int mouseX, int mouseY, GuiScreen currentScreen) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.currentScreen = currentScreen;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public GuiScreen getCurrentScreen() {
        return currentScreen;
    }
}
