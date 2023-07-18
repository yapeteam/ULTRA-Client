package cn.timer.ultra.event.events;

import cn.timer.ultra.event.Event;

import java.awt.*;

public class EventRenderShadow extends Event {

    private final float partialTicks;
    private Color color = new Color(0, 0, 0);

    public EventRenderShadow(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
