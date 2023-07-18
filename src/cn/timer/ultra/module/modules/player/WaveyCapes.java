package cn.timer.ultra.module.modules.player;

import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.values.Mode;
import cn.timer.ultra.values.Numbers;
import org.lwjgl.input.Keyboard;

public class WaveyCapes extends Module {
    public static final Mode<String> capeStyle = new Mode<>("Cape Style", new String[]{"Smooth", "Blocky"}, "Smooth");
    public static final Mode<String> capeMovement = new Mode<>("Cape Movement", new String[]{"Basic simulation", "Vanilla"}, "Basic simulation");
    public static final Numbers<Integer> gravity = new Numbers<>("Gravity", 5, 32, 25);
    public static final Numbers<Integer> heightMultiplier = new Numbers<>("Height Multiplier", 4, 16, 6);

    public WaveyCapes() {
        super("WaveyCapes", Keyboard.KEY_NONE, Category.Player);
        addValues(capeStyle, capeMovement, gravity, heightMultiplier);
    }
}
