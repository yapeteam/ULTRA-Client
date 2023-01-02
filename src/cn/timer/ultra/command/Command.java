package cn.timer.ultra.command;

import net.minecraft.client.Minecraft;

public abstract class Command {
    public static Minecraft mc = Minecraft.getMinecraft();
    private String name;

    public Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String execute(String[] args);
}
