package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.Client;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import org.lwjgl.input.Keyboard;

public class MusicPlayer extends Module {
    public MusicPlayer() {
        super("MusicPlayer", Keyboard.KEY_NONE, Category.Render);
    }

    @Override
    public void onEnable() {
        setEnabled(false);
        mc.displayGuiScreen(Client.instance.musicPlayerUI);
    }
}
