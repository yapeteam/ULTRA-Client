package cn.timer.ultra.config.configs;

import cn.timer.ultra.Client;
import cn.timer.ultra.config.Config;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.values.Booleans;
import cn.timer.ultra.values.Mode;
import cn.timer.ultra.values.Numbers;
import cn.timer.ultra.values.Value;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Values extends Config {
    public Values() {
        super("Values");
    }

    @Override
    public void load() {
        try {
            final JsonObject jsonObject = (JsonObject) new Gson().fromJson(new String(Files.readAllBytes(this.getPath()), StandardCharsets.UTF_8), (Class<?>) JsonObject.class);
            for (final Module module : Client.instance.getModuleManager().modules) {
                if (jsonObject.has(module.getName())) {
                    final JsonObject settingJsonObject = jsonObject.get(module.getName()).getAsJsonObject();
                    for (Value<?> value : module.getValues()) {
                        if (settingJsonObject.has(value.getName())) {
                            if (value instanceof Numbers) {
                                if (value.getValue() instanceof Integer) {
                                    ((Numbers<Integer>) value).setValue(settingJsonObject.get(value.getName()).getAsInt());
                                }
                                if (value.getValue() instanceof Float) {
                                    ((Numbers<Float>) value).setValue(settingJsonObject.get(value.getName()).getAsFloat());
                                }
                                if (value.getValue() instanceof Double) {
                                    ((Numbers<Double>) value).setValue(settingJsonObject.get(value.getName()).getAsDouble());
                                }
                                if (value.getValue() instanceof Long) {
                                    ((Numbers<Long>) value).setValue(settingJsonObject.get(value.getName()).getAsLong());
                                }
                            } else if (value instanceof Mode) {
                                if (value.getValue() instanceof String) {
                                    ((Mode<String>) value).setValue(settingJsonObject.get(value.getName()).getAsString());
                                }
                            } else if (value instanceof Booleans) {
                                ((Booleans) value).setValue(settingJsonObject.get(value.getName()).getAsBoolean());
                            }
                        }
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
            final JsonObject settingJsonObject = new JsonObject();
            for (final Value<?> value : module.getValues()) {
                if (value instanceof Numbers) {
                    if (((Numbers<?>) value).getValue() instanceof Integer) {
                        settingJsonObject.addProperty(value.getName(), ((Numbers<Integer>) value).floatValue());
                    }
                    if (((Numbers<?>) value).getValue() instanceof Float) {
                        settingJsonObject.addProperty(value.getName(), ((Numbers<Float>) value).floatValue());
                    }
                    if (((Numbers<?>) value).getValue() instanceof Double) {
                        settingJsonObject.addProperty(value.getName(), ((Numbers<Double>) value).floatValue());
                    }
                    if (((Numbers<?>) value).getValue() instanceof Long) {
                        settingJsonObject.addProperty(value.getName(), ((Numbers<Long>) value).floatValue());
                    }
                } else if (value instanceof Booleans) {
                    settingJsonObject.addProperty(value.getName(), ((Booleans) value).getValue());
                } else if (value instanceof Mode) {
                    if (((Mode<?>) value).getValue() instanceof String) {
                        settingJsonObject.addProperty(value.getName(), ((Mode<String>) value).getValue());
                    }
                }
            }
            jsonObject.add(module.getName(), settingJsonObject);
        }
        try {
            Files.write(this.getPath(), new GsonBuilder().setPrettyPrinting().create().toJson(jsonObject).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
