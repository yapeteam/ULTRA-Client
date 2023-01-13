package cn.timer.ultra.module;

import cn.timer.ultra.event.EventManager;
import cn.timer.ultra.values.Value;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Collections;

public abstract class Module {
    private String name;//名字
    private int key;//按键
    private final Category category;//类型
    private String suffix;//后缀
    private boolean enabled;
    public static Minecraft mc = Minecraft.getMinecraft();
    private final ArrayList<Value<?>> values = new ArrayList<>();

    public Module(String name, int key, Category category) {
        this.name = name;
        this.key = key;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            this.onEnable();
            EventManager.instance.register(this);
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

    public Category getCategory() {
        return category;
    }
}
