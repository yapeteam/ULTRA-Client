package cn.timer.ultra.config;

import cn.timer.ultra.Client;
import cn.timer.ultra.config.configs.Enabled;
import cn.timer.ultra.config.configs.KeyBind;
import cn.timer.ultra.config.configs.Values;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ConfigManager {
    public ArrayList<Config> configs = new ArrayList<>();
    public Path filePath = Paths.get(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), "versions\\" + Client.CLIENT_NAME + "\\config");

    public void init() {
        this.configs.add(new Enabled());
        this.configs.add(new KeyBind());
        this.configs.add(new Values());
    }

    public void load() {
        for (final Config config : this.configs) {
            if (!config.getPath().toFile().exists()) {
                try {
                    Files.createDirectories(config.getPath().getParent());
                    config.getPath().toFile().createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                config.save();
            }
        }
        this.configs.forEach(Config::load);
    }

    public void save() {
        for (final Config config : this.configs) {
            if (!config.getPath().toFile().exists()) {
                try {
                    Files.createDirectories(config.getPath().getParent());
                    config.getPath().toFile().createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            config.save();
        }
    }

    public Path getFilePath() {
        return filePath;
    }
}
