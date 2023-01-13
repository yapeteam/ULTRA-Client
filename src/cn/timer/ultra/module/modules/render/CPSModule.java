package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.event.events.EventRender2D;
import cn.timer.ultra.gui.Font.FontLoaders;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.HUDModule;
import cn.timer.ultra.values.Booleans;
import cn.timer.ultra.values.Numbers;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class CPSModule extends HUDModule {
    private final List<Long> clicks;
    private final Booleans showBackground;

    public CPSModule() {
        super("CPS", Keyboard.KEY_NONE, Category.Render, 0, 0, 0, mc.fontRendererObj.FONT_HEIGHT, "Free", "Free");
        this.clicks = new ArrayList<>();
        this.showBackground = new Booleans("Show Background", true);
        addValues(showBackground);
    }

    @Override
    public void drawHUD() {
        GL11.glPushMatrix();
        if (this.showBackground.getValue()) {
            Gui.drawRect((int) getXPosition() - 1, (int) getYPosition() - 1, (int) (getXPosition() + width + 1), (int) (getYPosition() + height + 1), 0x6F000000);
            String string = this.clicks.size() + " CPS";
            mc.fontRendererObj.drawString(string, (int) getXPosition(), (int) getYPosition(), 0xffffffff);
            this.width = mc.fontRendererObj.getStringWidth(string);
        } else {
            String string = "[" + this.clicks.size() + " CPS]";
            mc.fontRendererObj.drawString(string, (int) getXPosition(), (int) getYPosition(), 0xffffffff, true);
            this.width = mc.fontRendererObj.getStringWidth(string);
        }
        GL11.glPopMatrix();
    }

    @EventTarget
    private void onTick(EventPreUpdate cBTickEvent) {
        this.clicks.removeIf(l -> l < System.currentTimeMillis() - 1000L);
    }

    @EventTarget
    private void onClick(EventPreUpdate cBClickEvent) {
        if (Mouse.isButtonDown(0)) {
            this.clicks.add(System.currentTimeMillis());
        }
    }
}
