package cn.timer.ultra;

import cn.timer.ultra.command.CommandManager;
import cn.timer.ultra.config.ConfigManager;
import cn.timer.ultra.event.EventManager;
import cn.timer.ultra.gui.ClickUI.ClickUIScreen;
import cn.timer.ultra.gui.cloudmusic.ui.MusicPlayerUI;
import cn.timer.ultra.module.ModuleManager;
import cn.timer.ultra.utils.ColorUtil;
import cn.timer.ultra.utils.RotationUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.Objects;

public class Client {
    public static final String CLIENT_NAME = "Ultra";
    public static Client instance = new Client();
    public static String NowConfig = "default";
    public ClickUIScreen clickGui;
    public MusicPlayerUI musicPlayerUI;
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private ConfigManager configManager;
    private TrayIcon trayIcon;
    public RotationUtils rotationUtils = new RotationUtils();

    public static void renderMsg(String s) {
    }

    public void Startup() {
        this.commandManager = new CommandManager();
        this.moduleManager = new ModuleManager();
        this.configManager = new ConfigManager();
        this.clickGui = new ClickUIScreen();
        this.musicPlayerUI = new MusicPlayerUI();

        this.commandManager.init();
        this.moduleManager.init();
        this.configManager.init();

        EventManager.instance.register(this.moduleManager, this.commandManager);
        this.configManager.load();

        try {
            this.trayIcon = new TrayIcon(ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/assets/minecraft/client/logob.png"))));
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        this.trayIcon.setImageAutoSize(true);
        this.trayIcon.setToolTip("Ultra Client  ~ ");
        try {
            SystemTray.getSystemTray().add(this.trayIcon);
        } catch (AWTException var3) {
            renderMsg("Unable to add tray icon.");
        }
        this.trayIcon.displayMessage("Ultra Client", "Thank you for using Ultra Client", TrayIcon.MessageType.NONE);
    }

    public void Shutdown() {
        this.configManager.save();
        this.trayIcon.displayMessage("Ultra Client - Notification", "See you soon.", TrayIcon.MessageType.ERROR);
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public final Color getClientColor() {
        return new Color(236, 133, 209);
    }

    public final Color getAlternateClientColor() {
        return new Color(28, 167, 222);
    }

    public Color[] getClientColors() {
        Color firstColor;
        Color secondColor;
        firstColor = mixColors(getClientColor(), getAlternateClientColor());
        secondColor = mixColors(getAlternateClientColor(), getClientColor());
        return new Color[]{firstColor, secondColor};
    }

    private Color mixColors(Color color1, Color color2) {
        return ColorUtil.interpolateColorC(color1, color2, 0);
    }
}
