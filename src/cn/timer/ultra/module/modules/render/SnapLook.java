package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventTick;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import org.lwjgl.input.Keyboard;

public class SnapLook extends Module {
    public boolean backCam = false;

    public SnapLook() {
        super("SnapLook", Keyboard.KEY_NONE, Category.Render);
    }

    @EventTarget
    public void onTick(EventTick e) {
        if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
            backCam = false;
            mc.gameSettings.thirdPersonView = 1;
        } else if (!backCam) {
            backCam = true;
            mc.gameSettings.thirdPersonView = 0;
        }
    }
}
