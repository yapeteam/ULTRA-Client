package cn.timer.ultra.module.modules.overlay;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.event.events.EventRender2D;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.notification.Notification;
import cn.timer.ultra.notification.NotificationManager;
import cn.timer.ultra.utils.ultra.TimerUtil;
import cn.timer.ultra.values.Booleans;
import org.lwjgl.input.Keyboard;

public class Notifications extends Module {
    private final Booleans lowHealth = new Booleans("Low Health", true);

    public Notifications() {
        super("Notifications", Keyboard.KEY_NONE, Category.Overlay);
        addValues(lowHealth);
    }

    @EventTarget
    private void onRender(EventRender2D e) {
        NotificationManager.instance.render();
    }

    private final TimerUtil timerUtil = new TimerUtil();

    @EventTarget
    private void onUpdate(EventPreUpdate e) {
        if (mc.thePlayer.getHealth() <= 10 && timerUtil.delay(10000) && lowHealth.getValue()) {
            NotificationManager.instance.add(new Notification("Low Health", Notification.Type.Warning));
            timerUtil.reset();
        } else if (!lowHealth.getValue()) {
            timerUtil.reset();
        }
    }
}
