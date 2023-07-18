package cn.timer.ultra.module.modules.render;

import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.values.Mode;
import cn.timer.ultra.values.Numbers;
import org.lwjgl.input.Keyboard;

public class Animations extends Module {
    private final Numbers<Float> itemPosXValue = new Numbers<>("ItemPosX", -1.0F, 1.0F, 0.56F);
    private final Numbers<Float> itemPosYValue = new Numbers<>("ItemPosY", -1.0F, 1.0F, -0.52F);
    private final Numbers<Float> itemPosZValue = new Numbers<>("ItemPosZ", -1.0F, 1.0F, -0.72F);
    private final Numbers<Float> itemScaleValue = new Numbers<>("ItemScale", 0.0f, 2.0f, 0.4f);
    private final Numbers<Float> translateXValue = new Numbers<>("TranslateX", 0.0f, 1.5f, 0.0f);
    private final Numbers<Float> translateYValue = new Numbers<>("TranslateY", 0.0f, 0.5f, 0.0f);
    private final Numbers<Float> translateZValue = new Numbers<>("TranslateZ", -2.0f, 0.0f, 0.0f);
    private final Numbers<Float> rotateXValue = new Numbers<>("RotateX", -180f, 180f, 0.0f);
    private final Numbers<Float> rotateYValue = new Numbers<>("RotateY", -180f, 180f, 0.0f);
    private final Numbers<Float> rotateZValue = new Numbers<>("RotateZ", -180f, 180f, 0.0f);
    private final Mode<String> blockingModeValue = new Mode<>("BlockingMode", new String[]{
            "1.7", "Akrien", "Avatar", "ETB", "Exhibition", "Push", "Reverse",
            "Shield", "SigmaNew", "SigmaOld", "Slide", "SlideDown", "HSlide",
            "Swong", "VisionFX", "Swank", "Jello", "Rotate", "Liquid", "None"
    }, "1.7");

    public Animations() {
        super("Animations", Keyboard.KEY_NONE, Category.Render);
        addValues(itemPosXValue, itemPosYValue, itemPosZValue, itemScaleValue, translateXValue, translateYValue, translateZValue, rotateXValue, rotateYValue, rotateZValue, blockingModeValue);
        setEnabled(true);
    }

    @Override
    public void onDisable() {
        this.setEnabled(true);
    }

    public Numbers<Float> getItemPosXValue() {
        return this.itemPosXValue;
    }

    public Numbers<Float> getItemPosYValue() {
        return this.itemPosYValue;
    }

    public Numbers<Float> getItemPosZValue() {
        return this.itemPosZValue;
    }

    public Numbers<Float> getItemScaleValue() {
        return this.itemScaleValue;
    }

    public Numbers<Float> getTranslateXValue() {
        return this.translateXValue;
    }

    public Numbers<Float> getTranslateYValue() {
        return this.translateYValue;
    }

    public Numbers<Float> getTranslateZValue() {
        return this.translateZValue;
    }

    public Numbers<Float> getRotateXValue() {
        return this.rotateXValue;
    }

    public Numbers<Float> getRotateYValue() {
        return this.rotateYValue;
    }

    public Numbers<Float> getRotateZValue() {
        return this.rotateZValue;
    }

    public Mode<String> getBlockingModeValue() {
        return this.blockingModeValue;
    }
}
