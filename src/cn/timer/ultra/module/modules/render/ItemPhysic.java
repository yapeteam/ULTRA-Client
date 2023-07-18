package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import org.lwjgl.input.Keyboard;

public class ItemPhysic extends Module {
    public ItemPhysic() {
        super("ItemPhysic", Keyboard.KEY_NONE, Category.Render);
    }
}
