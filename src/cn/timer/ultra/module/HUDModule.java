package cn.timer.ultra.module;

import cn.timer.ultra.values.Mode;
import cn.timer.ultra.values.Numbers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public abstract class HUDModule extends Module {
    public boolean dragging = false;
    public float width;
    public float height;
    public int alpha = 0;
    public float dragX;
    public float dragY;
    public final Mode<String> horizontalFacing = new Mode<>("HorizontalFacing", new String[]{"Left", "Middle", "Right", "Free"}, "Free");
    public final Mode<String> verticalFacing = new Mode<>("VerticalFacing", new String[]{"Top", "Middle", "Bottom", "Free"}, "Free");
    public final Numbers<Float> subXPosition;
    public final Numbers<Float> subYPosition;
    private Overlay overlay;

    public HUDModule(String name, int key, Category category, float xPosition, float yPosition, float width, float height, String horizontalFacing, String verticalFacing) {
        super(name, key, category);
        ScaledResolution sr = new ScaledResolution(mc);
        this.subXPosition = new Numbers<>("posX", 0f, sr.getScaledWidth() - width, xPosition);
        this.subYPosition = new Numbers<>("posY", 0f, sr.getScaledHeight() - height, yPosition);
        this.width = width;
        this.height = height;
        for (String horizontalFacing1 : this.horizontalFacing.getModes()) {
            if (horizontalFacing1.equalsIgnoreCase(horizontalFacing)) {
                this.horizontalFacing.setValue(horizontalFacing1);
            }
        }
        for (String verticalFacing1 : this.verticalFacing.getModes()) {
            if (verticalFacing1.equalsIgnoreCase(verticalFacing)) {
                this.verticalFacing.setValue(verticalFacing1);
            }
        }
        addValues(this.subXPosition, this.subYPosition, this.horizontalFacing, this.verticalFacing);
    }

    public void registerOverlay(Overlay overlay) {
        this.overlay = overlay;
    }

    public Overlay getOverlay() {
        return this.overlay;
    }

    public float getXPosition() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        switch (horizontalFacing.getValue().toLowerCase()) {
            case "left": {
                return 0;
            }
            case "middle": {
                return (sr.getScaledWidth() - width) / 2f;
            }
            case "right": {
                return sr.getScaledWidth() - width;
            }
        }
        return subXPosition.floatValue();
    }

    public float getYPosition() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        switch (verticalFacing.getValue().toLowerCase()) {
            case "top": {
                return 0;
            }
            case "middle": {
                return (sr.getScaledHeight() - height) / 2f;
            }
            case "bottom": {
                return sr.getScaledHeight() - height;
            }
        }
        return subYPosition.floatValue();
    }
}
