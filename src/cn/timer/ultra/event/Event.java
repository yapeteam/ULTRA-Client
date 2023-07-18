package cn.timer.ultra.event;

import net.minecraft.client.Minecraft;

public abstract class Event {
    private boolean cancelled;//用于取消行动
    private Type type;
    public static Minecraft mc = Minecraft.getMinecraft();

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        Pre,
        Post, Send, Receive
    }
}
