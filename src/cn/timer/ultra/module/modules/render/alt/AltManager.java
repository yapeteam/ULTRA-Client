package cn.timer.ultra.module.modules.render.alt;

import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import javafx.scene.web.WebEngine;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AltManager extends Module {
    public static String code = null, msg = null;

    public AltManager() {
        super("AltManager", Keyboard.KEY_NONE, Category.Render);
    }

    public static LoginThread t;

    @Override
    public void onEnable() {
        this.setEnabled(false);
        //mc.displayGuiScreen(Client.altmanagerUI);
        t = new LoginThread();
        t.start();
        GuiTipScreen.su = false;
        Minecraft.getMinecraft().displayGuiScreen(new GuiTipScreen("请在浏览器中登录", "code已复制到剪贴板"));
        try {
            Desktop.getDesktop().browse(new URI("https://www.microsoft.com/link"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
