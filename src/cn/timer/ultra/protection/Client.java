package cn.timer.ultra.protection;

import cn.timer.ultra.command.CommandManager;
import cn.timer.ultra.config.ConfigManager;
import cn.timer.ultra.event.EventManager;
import cn.timer.ultra.gui.ClickUI.ClickUIScreen;
import cn.timer.ultra.gui.ListedClickUi.ClickGui;
import cn.timer.ultra.module.ModuleManager;
import cn.timer.ultra.notification.NotificationManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Objects;

public class Client {
    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    private static void setCliPrivate(String name, Object val) throws NoSuchFieldException, IllegalAccessException {
        getClientField(name).set(getClientField("instance").get(null), val);
    }

    private static Field getClientField(String name) throws NoSuchFieldException {
        return getField(cn.timer.ultra.Client.class, name);
    }

    private static <T> T getFieldAsClass(Class<T> clazz, String name) throws NoSuchFieldException, IllegalAccessException {
        return (T) getClientField(name).get(getClientField("instance").get(null));
    }

    public static void Startup() throws Exception {
        ProtectionLoader.getLoadedClass().get("AntiLeak").getMethod("Check").invoke(null);

        setCliPrivate("commandManager", new CommandManager());
        setCliPrivate("moduleManager", new ModuleManager());
        setCliPrivate("configManager", new ConfigManager());
        setCliPrivate("clickGui", new ClickUIScreen());
        setCliPrivate("listedClickGui", new ClickGui());

        getFieldAsClass(CommandManager.class, "commandManager").init();
        getFieldAsClass(ModuleManager.class, "moduleManager").init();
        getFieldAsClass(ConfigManager.class, "configManager").init();
        NotificationManager.instance.init();

        EventManager.instance.register(getClientField("instance").get(null), getClientField("moduleManager").get(getClientField("instance").get(null)), getClientField("commandManager").get(getClientField("instance").get(null)));
        getFieldAsClass(ConfigManager.class, "configManager").load();

        try {
            setCliPrivate("trayIcon", new TrayIcon(ImageIO.read(Objects.requireNonNull(cn.timer.ultra.Client.class.getResourceAsStream("/assets/minecraft/client/logo.png")))));
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        ((TrayIcon) getClientField("trayIcon").get(getClientField("instance").get(null))).setImageAutoSize(true);
        ((TrayIcon) getClientField("trayIcon").get(getClientField("instance").get(null))).setToolTip("Ultra Client  ~ ");
        try {
            SystemTray.getSystemTray().add((TrayIcon) getClientField("trayIcon").get(getClientField("instance").get(null)));
        } catch (AWTException var3) {
            System.err.println("Unable to add tray icon.");
        }

        ((TrayIcon) getClientField("trayIcon").get(getClientField("instance").get(null))).displayMessage("Ultra Client", "Thank you for using Ultra Client", TrayIcon.MessageType.NONE);
    }

    public static void Shutdown() throws NoSuchFieldException, IllegalAccessException {
        cn.timer.ultra.Client.instance.getConfigManager().save();
        ((TrayIcon) getClientField("trayIcon").get(getClientField("instance").get(null))).displayMessage("Ultra Client - Notification", "See you soon.", TrayIcon.MessageType.ERROR);
    }
}
