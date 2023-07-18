package cn.timer.ultra.module;

import cn.timer.ultra.event.EventManager;
import cn.timer.ultra.notification.Notification;
import cn.timer.ultra.notification.NotificationManager;
import cn.timer.ultra.values.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Module {
    private String name;//名字
    private int key;//按键
    private String suffix;//后缀
    private boolean enabled;
    public static Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<Value<?>> values = new ArrayList<>();
    private final Category category;

    public Module(String name, int key, Category category) {
        this.name = name;
        this.key = key;
        //类型
        this.category = category;
        this.suffix = null;
    }

    public String getSuffix() {
        return suffix == null ? "" : suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = " " + suffix;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("random.click"), 1.0F));
        if (enabled) {
            EventManager.instance.register(this);
            NotificationManager.instance.add(new Notification(this.name + " toggled", Notification.Type.Success));
            this.onEnable();
        } else {
            EventManager.instance.unregister(this);
            NotificationManager.instance.add(new Notification(this.name + " toggled", Notification.Type.Error));
            this.onDisable();
        }
    }

    public void setEnabled() {
        this.enabled = !this.enabled;
        if (enabled) {
            EventManager.instance.register(this);
            NotificationManager.instance.add(new Notification(this.name + " toggled", Notification.Type.Success));
            this.onEnable();
        } else {
            EventManager.instance.unregister(this);
            NotificationManager.instance.add(new Notification(this.name + " toggled", Notification.Type.Error));
            this.onDisable();
        }
    }

    public void setConEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            EventManager.instance.register(this);
            this.onEnable();
        } else {
            EventManager.instance.unregister(this);
            this.onDisable();
        }
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addValues(Value<?>... values) {
        Collections.addAll(this.values, values);
    }

    public ArrayList<Value<?>> getValues() {
        return values;
    }

    float animation = 0;

    public float getAnimation() {
        return animation;
    }

    public void setAnimation(float animation) {
        this.animation = animation;
    }
}
