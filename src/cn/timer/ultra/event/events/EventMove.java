package cn.timer.ultra.event.events;

import cn.timer.ultra.event.Event;

public class EventMove extends Event {
    private double x, y, z;

    public EventMove(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setMotion(double moveSpeed) {
        mc.thePlayer.motionX = -Math.sin(getDirection()) * moveSpeed;
        mc.thePlayer.motionZ = Math.cos(getDirection()) * moveSpeed;
    }

    public void setMoveSpeed(double moveSpeed) {
        this.setX(-Math.sin(getDirection()) * moveSpeed);
        this.setZ(Math.cos(getDirection()) * moveSpeed);
    }

    private double getDirection() {
        float yaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0.0F) {
            yaw += 180.0F;
        }
        float forward = 1.0F;
        if (!(!(mc.thePlayer.moveForward < 0.0F) && mc.thePlayer.moveForward > 0.0F) && mc.thePlayer.moveStrafing > 0.0F) {
            yaw -= 90.0F * forward;
        } else if (mc.thePlayer.moveStrafing < 0.0F) {
            yaw += 90.0F * forward;
        }
        return Math.toRadians(yaw);
    }
}
