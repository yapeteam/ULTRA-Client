package cn.timer.ultra.module.modules.overlay;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventTick;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.gui.cloudmusic.MusicManager;
import cn.timer.ultra.gui.cloudmusic.ui.MusicOverlayRenderer;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.HUDModule;
import cn.timer.ultra.values.Booleans;
import org.lwjgl.input.Keyboard;

public class MusicPlayerOverlay extends HUDModule {
    private final Booleans visualize = new Booleans("Visualize", true);

    public MusicPlayerOverlay() {
        super("MusicPlayerOverlay", Keyboard.KEY_NONE, Category.Overlay, 0, 0, 150, 30, "free", "free");
        registerOverlay((e) -> MusicOverlayRenderer.INSTANCE.renderOverlay(getXPosition(), getYPosition()));
        addValues(visualize);
    }

    @EventTarget
    private void onTick(EventTick e) {
        MusicManager.INSTANCE.visualize = visualize.getValue();
        if (MusicOverlayRenderer.INSTANCE.getDisplay() != null)
            width = Math.max(UniFontLoaders.PingFangMedium18.getStringWidth(MusicOverlayRenderer.INSTANCE.getDisplay()[0]), UniFontLoaders.PingFangMedium18.getStringWidth(MusicOverlayRenderer.INSTANCE.getDisplay()[1])) + 33;
    }
}
