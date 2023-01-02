package cn.timer.ultra.module;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventKey;
import cn.timer.ultra.module.modules.combat.*;
import cn.timer.ultra.module.modules.movement.*;
import cn.timer.ultra.module.modules.player.*;
import cn.timer.ultra.module.modules.render.*;
import cn.timer.ultra.module.modules.world.*;

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
        this.modules.add(new MusicPlayer());
        this.modules.add(new MusicOverlay());
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
        ArrayList<Module> rmodules = new ArrayList<>();
        for (Module module : modules)
            if (module.getCategory() == currentCategory)
                rmodules.add(module);
        return rmodules;
    }
}
