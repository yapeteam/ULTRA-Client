package cn.timer.ultra.event.events;

import cn.timer.ultra.event.Event;

public class EventSendChat extends Event {
    private String message;

    public EventSendChat(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
