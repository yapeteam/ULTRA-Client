package cn.timer.ultra.utils.fdp;

import net.minecraft.potion.Potion;

public class PotionData {
    public final Potion potion;
    public int maxTimer = 0;
    public float animationX = 0;
    public float animationY = 0;
    public final int level;

    public PotionData(Potion potion, int level) {
        this.potion = potion;
        this.level = level;
    }

    public float getAnimationX() {
        return animationX;
    }

    public float getAnimationY() {
        return animationY;
    }

    public Potion getPotion() {
        return potion;
    }

    public int getMaxTimer() {
        return maxTimer;
    }
}