package cn.timer.ultra.event.events;

import cn.timer.ultra.event.Event;

public class EventStrafe extends Event {
    private final float strafe;
    private final float forward;
    private final float friction;

    public EventStrafe(float strafe, float forward, float friction) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
    }

    public float getStrafe() {
        return strafe;
    }

    public float getForward() {
        return forward;
    }

    public float getFriction() {
        return friction;
    }
}
