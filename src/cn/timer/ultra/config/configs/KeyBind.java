package cn.timer.ultra.config.configs;

import cn.timer.ultra.Client;
import cn.timer.ultra.config.Config;
import cn.timer.ultra.module.Module;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class KeyBind extends Config {
    public KeyBind() {
        super("KeyBind");
    }

    @Override
    public void load() {
        try {
            final JsonObject jsonObject = (JsonObject) new Gson().fromJson(new String(Files.readAllBytes(this.getPath()), StandardCharsets.UTF_8), (Class<?>) JsonObject.class);
            for (final Module module : Client.instance.getModuleManager().modules) {
                if (jsonObject.has(module.getName())) {
                    final JsonObject modJsonObject = jsonObject.get(module.getName()).getAsJsonObject();
                    if (modJsonObject.has("key") || modJsonObject.get("key").getAsInt() == 0) {
                        module.setKey(modJsonObject.get("key").getAsInt());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        final JsonObject jsonObject = new JsonObject();
        for (final Module module : Client.instance.getModuleManager().modules) {
            final JsonObject modJsonObject = new JsonObject();
            modJsonObject.addProperty("key", module.getKey());
            jsonObject.add(module.getName(), modJsonObject);
        }
        try {
            Files.write(this.getPath(), new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
