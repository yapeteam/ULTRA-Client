package cn.timer.ultra.module.modules.overlay;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventClick;
import cn.timer.ultra.event.events.EventKey;
import cn.timer.ultra.event.events.EventTick;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.HUDModule;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.jello.CircleManager;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import cn.timer.ultra.utils.ultra.render.Stencil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class KeyStrokes extends HUDModule {
    public KeyStrokes() {
        super("KeyStrokes", Keyboard.KEY_NONE, Category.Overlay, 5, 130, 172 / 2f - 5, 172 / 2f - 5, "Free", "Free");
        registerOverlay((e) -> {
            GlStateManager.enableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            float keyStrokeX = getXPosition();
            float keyStrokeY = getYPosition();
            mc.getTextureManager().bindTexture(new ResourceLocation("Jello/keystrokes.png"));
            Gui.drawModalRectWithCustomSizedTexture(keyStrokeX - 5.5f, keyStrokeY - 5.5f, 0, 0, 172 / 2f, 172 / 2f, 172 / 2f, 172 / 2f);

            Stencil.write(false);
            RenderUtil.drawRect(keyStrokeX + 26.5f - 1, keyStrokeY, keyStrokeX + 35 + 15.5f - 1, keyStrokeY + 25 - 1, 0xb2000000);
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Wcircles.drawCircles();
            Stencil.dispose();

            Stencil.write(false);
            RenderUtil.drawRect(keyStrokeX, keyStrokeY + 26.5f - 1, keyStrokeX + 25 - 1, keyStrokeY + 30 + 5 + 15.5f - 1, 0xb2000000);
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Acircles.drawCircles();
            Stencil.dispose();

            Stencil.write(false);
            RenderUtil.drawRect(keyStrokeX + 51 / 2f, keyStrokeY + 26.5f - 1, keyStrokeX + 25 + 51 / 2f - 1, keyStrokeY + 30 + 5 + 15.5f - 1, 0xb2000000);
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Scircles.drawCircles();
            Stencil.dispose();

            Stencil.write(false);
            RenderUtil.drawRect(keyStrokeX + 51 / 2f + 51 / 2f, keyStrokeY + 26.5f - 1, keyStrokeX + 25 + 51 / 2f + 51 / 2f - 1, keyStrokeY + 30 + 5 + 15.5f - 1, 0xb2000000);
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Dcircles.drawCircles();
            Stencil.dispose();

            Stencil.write(false);
            RenderUtil.drawRect(keyStrokeX, keyStrokeY + 26.5f + 51 / 2f - 1, keyStrokeX + 74 / 2f, keyStrokeY + 26.5f + 51 / 2f + 24 - 1, 0xb2000000);
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Lcircles.drawCircles();
            Stencil.dispose();

            Stencil.write(false);
            RenderUtil.drawRect(keyStrokeX + 77 / 2f, keyStrokeY + 26.5f + 51 / 2f - 1, keyStrokeX + 74 / 2f + 76 / 2f, keyStrokeY + 26.5f + 51 / 2f + 24 - 1, 0xb2000000);
            Stencil.erase(true);
            GlStateManager.enableBlend();
            Rcircles.drawCircles();
            Stencil.dispose();
            GlStateManager.enableAlpha();
        });
    }

    public static CircleManager Wcircles = new CircleManager();
    public static CircleManager Acircles = new CircleManager();
    public static CircleManager Scircles = new CircleManager();
    public static CircleManager Dcircles = new CircleManager();
    public static CircleManager Lcircles = new CircleManager();
    public static CircleManager Rcircles = new CircleManager();

    @EventTarget
    public void onKey(EventKey e) {
        KeyStrokes module = Client.instance.getModuleManager().getByClass(KeyStrokes.class);
        float x = module.getXPosition();
        float y = module.getYPosition();
        if (e.getKey() == mc.gameSettings.keyBindForward.getKeyCode()) {
            Wcircles.addCircle(x + 24 + 1 + 24 / 2f, y + 24 / 2f, 26, 5, mc.gameSettings.keyBindForward.getKeyCode(), CircleManager.KEY_TYPE.KeyBoard);
        }
        if (e.getKey() == mc.gameSettings.keyBindLeft.getKeyCode()) {
            Acircles.addCircle(x + 24 / 2f, y + 24 + 1 + 24 / 2f, 26, 5, mc.gameSettings.keyBindLeft.getKeyCode(), CircleManager.KEY_TYPE.KeyBoard);
        }
        if (e.getKey() == mc.gameSettings.keyBindBack.getKeyCode()) {
            Scircles.addCircle(x + 24 + 1 + 24 / 2f, y + 24 + 1 + 24 / 2f, 26, 5, mc.gameSettings.keyBindBack.getKeyCode(), CircleManager.KEY_TYPE.KeyBoard);
        }
        if (e.getKey() == mc.gameSettings.keyBindRight.getKeyCode()) {
            Dcircles.addCircle(x + 24 + 1 + 24 + 1 + 24 / 2f, y + 24 + 1 + 24 / 2f, 26, 5, mc.gameSettings.keyBindRight.getKeyCode(), CircleManager.KEY_TYPE.KeyBoard);
        }
    }

    @EventTarget
    private void onClick(EventClick e) {
        Module module = Client.instance.getModuleManager().getByClass(KeyStrokes.class);
        float x = ((KeyStrokes) module).getXPosition();
        float y = ((KeyStrokes) module).getYPosition();
        if (e.getMouseButton() == 0) {
            Lcircles.addCircle(x + 37 / 2f, y + 24 + 1 + 24 + 1 + 24, 35, 5, mc.gameSettings.keyBindAttack.getKeyCode(), CircleManager.KEY_TYPE.Mouse);
        }
        if (e.getMouseButton() == 1) {
            Rcircles.addCircle(x + 37 + 1.5 + 37 / 2f, y + 24 + 1 + 24 + 1 + 24, 35, 5, mc.gameSettings.keyBindUseItem.getKeyCode(), CircleManager.KEY_TYPE.Mouse);
        }
    }

    @EventTarget
    private void onTick(EventTick e) {
        Wcircles.runCircles();
        Acircles.runCircles();
        Scircles.runCircles();
        Dcircles.runCircles();
        Lcircles.runCircles();
        Rcircles.runCircles();
    }
}
