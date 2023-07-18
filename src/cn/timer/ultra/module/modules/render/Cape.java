package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventRenderScreen;
import cn.timer.ultra.gui.ClickUI.ClickUIScreen;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.values.Mode;
import net.optifine.player.CapeUtils;
import org.lwjgl.input.Keyboard;

public class Cape extends Module {

    public final Mode<String> mode = new Mode<>("Mode",
            new String[]{"ultra", "novoline", "astolfo", "Endermn", "exhi", "LBDonate"}, "ultra");

    public Cape() {
        super("Cape", Keyboard.KEY_NONE, Category.Render);
        addValues(mode);
    }

    @EventTarget
    private void onUpdate(EventRenderScreen e) {
        if (e.getCurrentScreen().getClass() == ClickUIScreen.class && e.getPartialTicks() > 0.8)
            CapeUtils.ReloadCapesFromLoc();
    }
}
