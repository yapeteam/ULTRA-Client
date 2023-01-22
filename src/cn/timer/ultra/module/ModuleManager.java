package cn.timer.ultra.module;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventDrawGui;
import cn.timer.ultra.event.events.EventKey;
import cn.timer.ultra.event.events.EventRender2D;
import cn.timer.ultra.module.modules.cheat.AntiInvisible;
import cn.timer.ultra.module.modules.cheat.Criticals;
import cn.timer.ultra.module.modules.cheat.KillAura;
import cn.timer.ultra.module.modules.player.*;
import cn.timer.ultra.module.modules.render.*;

import java.util.ArrayList;

public class ModuleManager {
    public final ArrayList<Module> modules = new ArrayList<>();

    public void init() {
        //Player
        this.modules.add(new MicrosoftAuthLogin());
        this.modules.add(new Derp());
        //Render
        this.modules.add(new ClickGUI());
        this.modules.add(new FreeCam());
        this.modules.add(new SnapLook());
        this.modules.add(new FreeLook());
        this.modules.add(new HUD());
        this.modules.add(new CPSModule());
        this.modules.add(new FPSModule());
        this.modules.add(new MusicPlayer());
        this.modules.add(new MusicOverlay());
        this.modules.add(new MotionBlur());
        this.modules.add(new Rotations());
        this.modules.add(new KeyStrokes());
        this.modules.add(new TabGUI());
        this.modules.add(new ArmorStatus());
        //Cheat
        this.modules.add(new KillAura());
        this.modules.add(new Criticals());
        this.modules.add(new AntiInvisible());
    }

    @EventTarget
    public void onKey(EventKey e) {
        for (Module module : this.modules) {
            if (module.getKey() != e.getKey()) continue;
            module.setEnabled(!module.isEnabled());
        }
    }

    public Module getModuleByName(String name) {
        for (Module module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public ArrayList<Module> getByCategory(Category currentCategory) {
        ArrayList<Module> modules = new ArrayList<>();
        for (Module module : this.modules)
            if (module.getCategory() == currentCategory)
                modules.add(module);
        return modules;
    }

    @EventTarget
    private void HUD_onDraw(EventRender2D drawEvent) {
        this.modules.forEach(module -> {
            if (module instanceof HUDModule && module.isEnabled()) {
                ((HUDModule) module).drawHUD();
            }
        });
    }

    @EventTarget
    private void HUD_onGuiScreen(EventDrawGui e) {
        this.modules.forEach(module -> {
            if (module instanceof HUDModule && module.isEnabled()) {
                ((HUDModule) module).onGuiScreen(e);
            }
        });
    }

    public Module getByClass(Class<? extends Module> MClass) {
        for (Module module : this.modules) {
            if (module.getClass() == MClass)
                return module;
        }
        return null;
    }
}
