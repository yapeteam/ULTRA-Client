package cn.timer.ultra.module.modules.overlay;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventClick;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.HUDModule;
import cn.timer.ultra.values.Booleans;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class CPSModule extends HUDModule {
    private final List<Long> L;
    private final List<Long> R;
    private final Booleans showBackground;

    public CPSModule() {
        super("CPS", Keyboard.KEY_NONE, Category.Overlay, 0, 0, 0, mc.fontRendererObj.FONT_HEIGHT, "Free", "Free");
        this.L = new ArrayList<>();
        this.R = new ArrayList<>();
        this.showBackground = new Booleans("Show Background", true);
        addValues(showBackground);
        registerOverlay((e) -> {
            GL11.glPushMatrix();
            if (this.showBackground.getValue()) {
                Gui.drawRect((int) getXPosition() - 1, (int) getYPosition() - 1, (int) (getXPosition() + width + 3), (int) (getYPosition() + height + 1), 0x6F000000);
                String string = this.L.size() + " | " + this.R.size() + " CPS";
                mc.fontRendererObj.drawString(string, (int) getXPosition(), (int) getYPosition(), 0xffffffff);
                this.width = mc.fontRendererObj.getStringWidth(string);
            } else {
                String string = "[" + this.L.size() + " | " + this.R.size() + " CPS]";
                mc.fontRendererObj.drawString(string, (int) getXPosition(), (int) getYPosition(), 0xffffffff, true);
                this.width = mc.fontRendererObj.getStringWidth(string);
            }
            GL11.glPopMatrix();
        });
    }

    @EventTarget
    private void onTick(EventPreUpdate e) {
        this.L.removeIf(l -> l < System.currentTimeMillis() - 1000L);
        this.R.removeIf(t -> t < System.currentTimeMillis() - 1000L);
    }

    @EventTarget
    private void onClick(EventClick event) {
        if (event.getMouseButton() == 0) {
            this.L.add(System.currentTimeMillis());
        }
        if (event.getMouseButton() == 1) {
            this.R.add(System.currentTimeMillis());
        }
    }
}
