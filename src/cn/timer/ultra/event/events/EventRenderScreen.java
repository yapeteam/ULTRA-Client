package cn.timer.ultra.event.events;

import cn.timer.ultra.event.Event;
import net.minecraft.client.gui.GuiScreen;

public class EventRenderScreen extends Event {
    private final int mouseX;
    private final int mouseY;
    private final float partialTicks;
    private final GuiScreen currentScreen;

    public EventRenderScreen(int mouseX, int mouseY, GuiScreen currentScreen, float partialTicks) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.partialTicks = partialTicks;
        this.currentScreen = currentScreen;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public GuiScreen getCurrentScreen() {
        return currentScreen;
    }
}
