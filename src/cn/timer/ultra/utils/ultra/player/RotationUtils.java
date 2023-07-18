package cn.timer.ultra.utils.ultra.player;

public class RotationUtils {
    public Rotation rotation = new Rotation(0, 0);
    public Rotation prevRotation = new Rotation(0, 0);

    public void setRotation(Rotation rotation1) {
        prevRotation.setRotation(rotation);
        rotation.setRotation(rotation1);
    }

    public void setYaw(float yaw) {
        prevRotation.setYaw(rotation.getYaw());
        rotation.setYaw(yaw);
    }

    public void setPitch(float pitch) {
        prevRotation.setPitch(rotation.getPitch());
        rotation.setPitch(pitch);
    }

    public boolean rotating = false;
}
