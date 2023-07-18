package cn.timer.ultra.module.modules.cheat;

import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.CombatModule;
import org.lwjgl.input.Keyboard;

public class AntiInvisible extends CombatModule {
    public AntiInvisible() {
        super("AntiInvisible", Keyboard.KEY_NONE, Category.Cheat);
    }
}
