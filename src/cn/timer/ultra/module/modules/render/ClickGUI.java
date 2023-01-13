package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.Client;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {
    public ClickGUI() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, Category.Render);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(Client.instance.clickGui);
        this.setEnabled(false);
    }
}
