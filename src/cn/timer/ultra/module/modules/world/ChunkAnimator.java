package cn.timer.ultra.module.modules.world;

import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.ultra.animation.AnimationHandler;
import org.lwjgl.input.Keyboard;

public class ChunkAnimator extends Module {
    public AnimationHandler animation = new AnimationHandler();

    public ChunkAnimator() {
        super("ChunckAnimator", Keyboard.KEY_NONE, Category.World);
    }
}
