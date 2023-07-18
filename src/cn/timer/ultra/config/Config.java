package cn.timer.ultra.config;

import cn.timer.ultra.Client;
import net.minecraft.client.Minecraft;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Config {
    private final String name;

    public Config(String name) {
        this.name = name;
    }

    public void load() {
    }

    public void save() {
    }

    protected Path getPath() {
        return Paths.get(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), "versions/" + Client.CLIENT_NAME + "/config/configs", Client.NowConfig, this.name + ".json");
    }

    public String getName() {
        return name;
    }
}
