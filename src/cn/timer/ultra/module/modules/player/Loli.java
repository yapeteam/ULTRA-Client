package cn.timer.ultra.module.modules.player;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.Event;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventModel;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.notification.Notification;
import cn.timer.ultra.notification.NotificationManager;
import net.minecraft.client.model.ModelBiped;
import org.lwjgl.input.Keyboard;

public class Loli extends Module {
    public int y;

    public Loli() {
        super("Loli", Keyboard.KEY_NONE, Category.Player);
    }

    @Override
    public void onEnable() {
        this.y = 0;
        Module module = Client.instance.getModuleManager().getByClass(MoBendsMod.class);
        if (module.isEnabled()) {
            module.setConEnabled(false);
            NotificationManager.instance.add(new Notification("Loli does not support MoBends!", Notification.Type.Info));
        }
    }

    @EventTarget
    public void onEmote(final EventModel event) {
        if (event.type == Event.Type.Post && event.entity == Loli.mc.thePlayer) {
            this.setBiped(event.getBiped());
        }
    }

    public void setBiped(final ModelBiped biped) {
        if (mc.gameSettings.thirdPersonView > 0) {
            biped.bipedRightArm.rotateAngleX = 0.5F;
            biped.bipedRightArm.rotateAngleY = -2.25F;
            biped.bipedLeftArm.rotateAngleX = 0.5F;
            biped.bipedLeftArm.rotateAngleY = 2.25F;
            biped.aimedBow = true;
            biped.isChild = true;
        }
    }
}
