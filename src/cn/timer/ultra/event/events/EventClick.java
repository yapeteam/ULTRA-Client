package cn.timer.ultra.event.events;

import cn.timer.ultra.event.Event;

public class EventClick extends Event {
    private final int mouseButton;

    public EventClick(int mouseButton) {
        this.mouseButton = mouseButton;
    }

    public int getMouseButton() {
        return mouseButton;
    }
}
