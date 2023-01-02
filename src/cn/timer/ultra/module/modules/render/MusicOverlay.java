package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventRender2D;
import cn.timer.ultra.gui.cloudmusic.ui.MusicOverlayRenderer;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import org.lwjgl.input.Keyboard;

public class MusicOverlay extends Module {

    public MusicOverlay() {
        super("MusicOverlay", Keyboard.KEY_NONE, Category.Render);
    }

    @EventTarget
    public void onRender2D(EventRender2D e) {
        MusicOverlayRenderer.INSTANCE.renderOverlay();
    }
}
