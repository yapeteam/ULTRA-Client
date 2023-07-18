package cn.timer.ultra.module.modules.player;

import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventPreUpdate;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.values.Booleans;
import cn.timer.ultra.values.Mode;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class FreeLook extends Module {
    public static boolean perspectiveToggled = false;
    private static float cameraYaw = 0;
    private static float cameraPitch = 0;
    private static int previousPerspective = 0;
    private static final Mode<String> mode = new Mode<>("Perspective", new String[]{"First Person", "Forward", "Back"}, "Back");
    private final Booleans hold = new Booleans("Hold", true);
    private final Mode<String> key = new Mode<>("Key", new String[]{"H", "F"}, "F");


    public FreeLook() {
        super("Freelook", Keyboard.KEY_NONE, Category.Player);
        addValues(mode, hold, key);
    }

    @Override
    public void onEnable() {
        perspectiveToggled = true;
        if (mc.thePlayer == null) {
            return;
        }
        cameraYaw = mc.thePlayer.rotationYaw;
        cameraPitch = mc.thePlayer.rotationPitch;
        previousPerspective = mc.gameSettings.thirdPersonView;
        if (mode.getValue().equalsIgnoreCase("Back")) {
            mc.gameSettings.thirdPersonView = 1;
        } else if (mode.getValue().equalsIgnoreCase("Forward")) {
            mc.gameSettings.thirdPersonView = 2;
        } else if (mode.getValue().equalsIgnoreCase("First Person")) {
            mc.gameSettings.thirdPersonView = 0;
        }
    }

    @Override
    public void onDisable() {
        perspectiveToggled = false;
        mc.gameSettings.thirdPersonView = previousPerspective;
    }

    @EventTarget
    public void onUpdate(EventPreUpdate e) {
        if (mc.thePlayer != null && mc.theWorld != null) {
            if (Keyboard.isKeyDown(Keyboard.getKeyIndex(key.getValue())) && mc.currentScreen == null && !perspectiveToggled) {
                perspectiveToggled = true;
                if (mc.thePlayer == null) {
                    return;
                }
                cameraYaw = mc.thePlayer.rotationYaw;
                cameraPitch = mc.thePlayer.rotationPitch;
                previousPerspective = mc.gameSettings.thirdPersonView;
                if (mode.getValue().equalsIgnoreCase("Back")) {
                    mc.gameSettings.thirdPersonView = 1;
                } else if (mode.getValue().equalsIgnoreCase("Forward")) {
                    mc.gameSettings.thirdPersonView = 2;
                } else if (mode.getValue().equalsIgnoreCase("First Person")) {
                    mc.gameSettings.thirdPersonView = 0;
                }
            }
            if (perspectiveToggled && !Keyboard.isKeyDown(Keyboard.getKeyIndex(key.getValue())) && hold.getValue() && mc.currentScreen == null) {
                perspectiveToggled = false;
                mc.gameSettings.thirdPersonView = previousPerspective;
            }
        }
    }

    public static float getCameraYaw() {
        return perspectiveToggled ? cameraYaw : mc.thePlayer.rotationYaw;
    }

    public static float getCameraPitch() {
        return perspectiveToggled ? cameraPitch : mc.thePlayer.rotationPitch;
    }

    public static float getCameraPrevYaw() {
        return perspectiveToggled ? cameraYaw : mc.thePlayer.prevRotationYaw;
    }

    public static float getCameraPrevPitch() {
        return perspectiveToggled ? cameraPitch : mc.thePlayer.prevRotationPitch;
    }

    public static boolean overrideMouse() {
        if (mc.inGameHasFocus && Display.isActive()) {
            if (!perspectiveToggled) {
                return true;
            }
            mc.mouseHelper.mouseXYChange();
            float f1 = mc.gameSettings.mouseSensitivity * 0.6f + 0.2f;
            float f2 = f1 * f1 * f1 * 8.0f;
            float f3 = mc.mouseHelper.deltaX * f2;
            float f4 = mc.mouseHelper.deltaY * f2;
            cameraYaw += f3 * 0.15f;
            cameraPitch += f4 * 0.15f;
            if (cameraPitch > 90.0f) {
                cameraPitch = 90.0f;
            }
            if (cameraPitch < -90.0f) {
                cameraPitch = -90.0f;
            }
        }
        return false;
    }

    public static float getNameTagYaw() {
        return mc.gameSettings.thirdPersonView == 2 ? -getCameraYaw() - 180 : -getCameraYaw();
    }

    public static float getNameTagPitch() {
        return mc.gameSettings.thirdPersonView == 2 ? -getCameraPitch() : getCameraPitch();
    }
}
