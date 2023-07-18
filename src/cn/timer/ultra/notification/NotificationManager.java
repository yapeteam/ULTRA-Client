package cn.timer.ultra.notification;

import cn.timer.ultra.event.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.ArrayList;

public class NotificationManager {
    public static NotificationManager instance = new NotificationManager();
    private final ArrayList<Notification> notifications = new ArrayList<>();
    private long time = 5000;
    private long backTime = 4500;
    private float height = 25;

    public void init() {
        EventManager.instance.register(this);
    }

    public void setBackTime(long backTime) {
        this.backTime = backTime;
    }

    public long getBackTime() {
        return backTime;
    }

    public void update() {
        for (int i = 0; i < this.notifications.size(); i++) {
            Notification notification = notifications.get(i);
            if (notification.getTimer().time() >= backTime) {
                notification.setBack(true);
            }
            if (notification.getTimer().time() >= time) {
                this.notifications.remove(notification);
            }
            notification.update(i);
        }
    }

    public void render() {
        update();
        if (!this.notifications.isEmpty()) {
            for (Notification notification : this.notifications) {
                notification.render();
            }
        }
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void add(Notification notification) {
        float width = new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth();
        notification.setCurrentX(width - notification.getWidth() - 2);
        notification.setTargetX(width - notification.getWidth() - 2);
        notification.setCurrentY(-getHeight());
        this.notifications.add(notification);
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}