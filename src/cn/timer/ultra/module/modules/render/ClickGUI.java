package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventRender2D;
import cn.timer.ultra.event.events.EventRenderScreen;
import cn.timer.ultra.gui.VapeClickUI.VapeClickGui;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.values.Booleans;
import cn.timer.ultra.values.Mode;
import org.lwjgl.input.Keyboard;

public class ClickGUI extends Module {
    private final Booleans motionBlur = new Booleans("Use Motion Blur", false);
    private final Mode<String> mode = new Mode<>("Mode", new String[]{"Ultra", "Listed", "Vape"}, "Ultra");
    public boolean vape;
    int mouseX, mouseY;

    public ClickGUI() {
        super("ClickGUI", Keyboard.KEY_RSHIFT, Category.Render);
        addValues(motionBlur, mode);
    }

    @Override
    public void onEnable() {
        if (mode.getValue().equals("Ultra"))
            mc.displayGuiScreen(Client.instance.clickGui);
        if (mode.getValue().equals("Listed"))
            mc.displayGuiScreen(Client.instance.listedClickGui);
        if (mode.getValue().equals("Vape")) {
            mc.displayGuiScreen(new VapeClickGui());
            setConEnabled(false);
            if (!vape)
                mode.setMode("Ultra");
        }
    }

    @EventTarget
    private void onRender(EventRender2D e) {
        if (vape) return;
        if (mc.currentScreen != Client.instance.clickGui) return;
        if (motionBlur.getValue())
            Client.instance.clickGui.doRender(this.mouseX, this.mouseY);
    }

    @EventTarget
    private void onGui(EventRenderScreen e) {
        this.mouseX = e.getMouseX();
        this.mouseY = e.getMouseY();
    }

    public Booleans getMotionBlur() {
        return motionBlur;
    }
}
