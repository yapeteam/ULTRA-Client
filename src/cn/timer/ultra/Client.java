package cn.timer.ultra;

import cn.timer.ultra.command.CommandManager;
import cn.timer.ultra.config.ConfigManager;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventKey;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.gui.ClickUI.ClickUIScreen;
import cn.timer.ultra.gui.ListedClickUi.ClickGui;
import cn.timer.ultra.module.ModuleManager;
import cn.timer.ultra.utils.fdp.RotationUtils;
import cn.timer.ultra.utils.ultra.color.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.awt.*;

public class Client {
    public static final String CLIENT_NAME;
    public static final String CLIENT_ID;
    public static Client instance = new Client();
    public static String NowConfig = "default";
    public ClickUIScreen clickGui;
    public ClickGui listedClickGui;
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private ConfigManager configManager;
    private TrayIcon trayIcon;
    public static RotationUtils rotationUtils = new RotationUtils();

    private static String toStr(char[] data) {
        StringBuilder str = new StringBuilder();
        for (char c : data) {
            str.append(c);
        }
        return str.toString();
    }

    static {
        char[] data1 = new char[]{'U', 'l', 't', 'r', 'a', '-', 'C', 'l', 'i', 'e', 'n', 't'};
        CLIENT_NAME = toStr(data1);
        char[] data2 = new char[]{'b', 'a', 'e', 'b', '6', '3', '4', '4', '-', '8', '1', '2', '9', '-', '4', '3', '4', '0', '-', 'b', '9', 'a', '7', '-', 'd', '7', '2', 'd', '4', 'e', '8', 'd', '3', '6', 'd', '3'};
        CLIENT_ID = toStr(data2);
    }

    public static void renderMsg(String s) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(s));
    }

    /*public void Startup() throws Exception {
        AntiLeak.Check();

        this.commandManager = new CommandManager();
        this.moduleManager = new ModuleManager();
        this.configManager = new ConfigManager();
        this.clickGui = new ClickUIScreen();
        this.listedClickGui = new ClickGui();

        this.commandManager.init();
        this.moduleManager.init();
        this.configManager.init();
        NotificationManager.instance.init();

        EventManager.instance.register(instance, this.moduleManager, this.commandManager);
        this.configManager.load();

        try {
            this.trayIcon = new TrayIcon(ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream("/assets/minecraft/client/logo.png"))));
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
        this.getConfigManager().save();
        this.trayIcon.displayMessage("Ultra Client - Notification", "See you soon.", TrayIcon.MessageType.ERROR);
    }*/

    @EventTarget
    private void onUpdate(EventPreUpdate e) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;
        if (mc.gameSettings.keyBindAttack.isKeyDown() && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            mc.thePlayer.FakeSwingItem();
        }
    }

    @EventTarget
    private void key(EventKey e) {
        if (!InjectChecker.doCheck()) {
            System.err.println("纪狗气死我了");
            System.exit(1);
        }
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public ConfigManager getConfigManager() {
        return this.configManager;
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

    public boolean isBlurEnabled() {
        return true;
    }
}
