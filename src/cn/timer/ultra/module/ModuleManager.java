package cn.timer.ultra.module;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventDrawGui;
import cn.timer.ultra.event.events.EventKey;
import cn.timer.ultra.event.events.EventRender2D;
import cn.timer.ultra.module.modules.chest.combat.*;
import cn.timer.ultra.module.modules.chest.movement.*;
import cn.timer.ultra.module.modules.chest.player.*;
import cn.timer.ultra.module.modules.chest.world.*;
import cn.timer.ultra.module.modules.player.*;
import cn.timer.ultra.module.modules.render.*;

import java.util.ArrayList;

public class ModuleManager {
    public final ArrayList<Module> modules = new ArrayList<>();

    public void init() {
        //Combat
        this.modules.add(new Criticals());
        this.modules.add(new KillAura());
        this.modules.add(new Velocity());
        //Movement
        this.modules.add(new Fly());
        this.modules.add(new InvMove());
        this.modules.add(new NoSlow());
        this.modules.add(new Speed());
        this.modules.add(new Sprint());
        this.modules.add(new Step());
        //Player
        this.modules.add(new NoFall());
        //Render
        this.modules.add(new ClickGUI());
        this.modules.add(new FreeCam());
        this.modules.add(new HUD());
        this.modules.add(new CPSModule());
        this.modules.add(new FPSModule());
        this.modules.add(new MusicPlayer());
        this.modules.add(new MusicOverlay());
        this.modules.add(new MicrosoftAuthLogin());
        //World
        this.modules.add(new Scaffold());
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
        ArrayList<Module> Cmodules = new ArrayList<>();
        for (Module module : modules)
            if (module.getCategory() == currentCategory)
                Cmodules.add(module);
        return Cmodules;
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
}
