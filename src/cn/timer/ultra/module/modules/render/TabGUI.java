package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.HUDModule;
import cn.timer.ultra.module.Module;
import org.lwjgl.input.Keyboard;

public class TabGUI extends HUDModule {
    public TabGUI() {
        super("TabGUI", Keyboard.KEY_NONE, Category.Render, 5, 45, 84, 86, "Free", "Free");
    }
}
