package cn.timer.ultra.module.modules.movement;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.values.Mode;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {
    private final Mode<String> mode = new Mode<>("Mode", new String[]{"Simple", "AllDirection"}, "AllDirection");
    public Sprint() {
        super("Sprint", Keyboard.KEY_NONE, Category.Movement);
        this.addValues(this.mode);
    }

    @EventTarget
    public void onUpdate(EventPreUpdate e) {
        this.setSuffix(this.mode.getValue());
        if (mc.thePlayer.moveForward == 0.0F && (mode.isCurrentMode("Simple") || mc.thePlayer.moveStrafing == 0.0F)) {
            mc.thePlayer.setSprinting(false);
            return;
        }
        mc.thePlayer.setSprinting(true);
    }
}
