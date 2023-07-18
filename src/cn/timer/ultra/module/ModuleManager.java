package cn.timer.ultra.module;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventKey;
import cn.timer.ultra.module.modules.cheat.*;
import cn.timer.ultra.module.modules.overlay.*;
import cn.timer.ultra.module.modules.player.FreeLook;
import cn.timer.ultra.module.modules.player.Loli;
import cn.timer.ultra.module.modules.player.MoBendsMod;
import cn.timer.ultra.module.modules.player.WaveyCapes;
import cn.timer.ultra.module.modules.render.*;
import cn.timer.ultra.module.modules.world.ChunkAnimator;
import cn.timer.ultra.module.modules.world.WorldTime;
import cn.timer.ultra.utils.fdp.Rotations;

import java.util.ArrayList;

public class ModuleManager {
    public final ArrayList<Module> modules = new ArrayList<>();

    public void init() {
        //Player
        this.modules.add(new Sprint());
        this.modules.add(new LunarSpoof());
        //Overlay
        this.modules.add(new CPSModule());
        this.modules.add(new FPSModule());
        this.modules.add(new KeyStrokes());
        this.modules.add(new ArmorStatus());
        this.modules.add(new Effects());
        this.modules.add(new MusicPlayerOverlay());
        this.modules.add(new Notifications());
        this.modules.add(new JelloArraylist());
        //Render
        this.modules.add(new ClickGUI());
        this.modules.add(new FreeCam());
        this.modules.add(new FreeLook());
        this.modules.add(new HUD());
        this.modules.add(new Animations());
        this.modules.add(new Crosshair());
        this.modules.add(new Cape());
        this.modules.add(new WaveyCapes());
        this.modules.add(new ItemPhysic());
        this.modules.add(new MoBendsMod());
        this.modules.add(new Loli());
        this.modules.add(new MotionBlur());
        //world
        this.modules.add(new WorldTime());
        this.modules.add(new ChunkAnimator());
        //Cheat
        this.modules.add(new KillAura());
        this.modules.add(new AntiInvisible());
        this.modules.add(new AntiBot());
        this.modules.add(new AimBot());
        this.modules.add(new Target());
        this.modules.add(new ChestStealer());
        this.modules.add(new AutoArmor());
        this.modules.add(new InfiniteAura());
        this.modules.add(new Teams());
        this.modules.add(new Rotations());
        this.modules.add(new SafeWalk());
        this.modules.add(new Scaffold());
        this.modules.add(new FakeLag());
        modules.sort((mod, mod1) -> {
            int char0 = mod.getName().charAt(0);
            int char1 = mod1.getName().charAt(0);
            return -Integer.compare(char1, char0);
        });
    }

    @EventTarget
    public void onKey(EventKey e) {
        for (Module module : this.modules) {
            if (module.getKey() != e.getKey()) continue;
            module.setEnabled(!module.isEnabled());
        }
    }

    public <T extends Module> T getByClass(Class<T> clazz) {
        for (Module module : this.modules) {
            if (module.getClass().getName().equals(clazz.getName()))
                return (T) module;
        }
        return null;
    }

    public ArrayList<Module> getModules() {
        return this.modules;
    }

    public ArrayList<Module> getByCategory(Category cat) {
        ArrayList<Module> returns = new ArrayList<>();
        for (Module module : modules) {
            if (module.getCategory() == cat)
                returns.add(module);
        }
        return returns;
    }
}
