package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.HUDModule;
import org.lwjgl.input.Keyboard;

public class KeyStrokes extends HUDModule {
    public KeyStrokes() {
        super("KeyStrokes", Keyboard.KEY_NONE, Category.Render, 5, 130, 172 / 2f - 5, 172 / 2f - 5, "Free", "Free");
    }
}
