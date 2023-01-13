package cn.timer.ultra.module.modules.player;

import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.module.modules.player.alt.GuiTipScreen;
import cn.timer.ultra.module.modules.player.alt.LoginThread;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.net.URI;

public class MicrosoftAuthLogin extends Module {
    public static String code = null, msg = null;

    public MicrosoftAuthLogin() {
        super("MicrosoftAuthLogin", Keyboard.KEY_NONE, Category.Player);
    }

    public static LoginThread t;

    @Override
    public void onEnable() {
        this.setEnabled(false);
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
