package cn.timer.ultra;

import cn.timer.ultra.protection.ProtectionLoader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Initializer {
    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    public static void Startup() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ProtectionLoader.getLoadedClass().get("Client").getMethod("Startup").invoke(null);
    }

    public static void Shutdown() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        ProtectionLoader.getLoadedClass().get("Client").getMethod("Shutdown").invoke(null);
    }
}
