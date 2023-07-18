package cn.timer.ultra.module.modules.cheat;

import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.CombatModule;
import org.lwjgl.input.Keyboard;

public class LunarSpoof extends CombatModule {
    public LunarSpoof() {
        super("LunarSpoof", Keyboard.KEY_NONE, Category.Cheat);
    }
}
