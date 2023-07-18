package cn.timer.ultra.event.events;

import cn.timer.ultra.event.Event;

public class EventPostUpdate extends Event {
    private float yaw, pitch;
    private double y;
    private boolean onGround;

    public EventPostUpdate(float yaw, float pitch, double y, boolean onGround) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.onGround = onGround;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public double getY() {
        return y;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
