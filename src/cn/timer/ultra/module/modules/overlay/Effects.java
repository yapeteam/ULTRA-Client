package cn.timer.ultra.module.modules.overlay;

import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.HUDModule;
import cn.timer.ultra.utils.fdp.PotionData;
import cn.timer.ultra.utils.fdp.RenderUtils;
import cn.timer.ultra.utils.fdp.realpha;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Effects extends HUDModule {
    private final Map<Potion, PotionData> potionMap = new HashMap<>();

    public Effects() {
        super("Effects", Keyboard.KEY_NONE, Category.Overlay, 0, 0, 120, 30, "left", "bottom");
        registerOverlay((e) -> {
            GlStateManager.pushMatrix();
            int x = (int) getXPosition();
            int posY = (int) getYPosition();
            for (final PotionEffect potionEffect : mc.thePlayer.getActivePotionEffects()) {
                final Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
                final String name = I18n.format(potion.getName());
                final PotionData potionData;
                if (potionMap.containsKey(potion) && potionMap.get(potion).level == potionEffect.getAmplifier())
                    potionData = potionMap.get(potion);
                else
                    potionMap.put(potion, (potionData = new PotionData(potion, potionEffect.getAmplifier())));
                boolean flag = true;
                for (final PotionEffect checkEffect : mc.thePlayer.getActivePotionEffects())
                    if (checkEffect.getAmplifier() == potionData.level) {
                        flag = false;
                        break;
                    }
                if (flag) potionMap.remove(potion);
                int potionTime, potionMaxTime;
                try {
                    potionTime = Integer.parseInt(Potion.getDurationString(potionEffect).split(":")[0]);
                    potionMaxTime = Integer.parseInt(Potion.getDurationString(potionEffect).split(":")[1]);
                } catch (Exception ignored) {
                    potionTime = 100;
                    potionMaxTime = 1000;
                }
                final int lifeTime = (potionTime * 60 + potionMaxTime);
                if (potionData.getMaxTimer() == 0 || lifeTime > (double) potionData.getMaxTimer())
                    potionData.maxTimer = lifeTime;
                float state = 0.0F;
                if (lifeTime >= 0.0D) state = (float) (lifeTime / (double) ((float) potionData.getMaxTimer()) * 100.0D);
                int y = (int) potionData.getAnimationY();
                final int position = y + 5;
                state = Math.max(state, 2.0F);
                potionData.animationX = (float) getAnimationState(potionData.getAnimationX(), 1.2F * state, Math.max(10.0F, Math.abs(potionData.animationX - 1.2F * state) * 15.0F) * 0.3F);
                potionData.animationY = (float) getAnimationState(potionData.getAnimationY(), posY, Math.max(10.0F, Math.abs(potionData.animationY - posY) * 15.0F) * 0.3F);
                RenderUtils.drawRect(x, y, 120 + x, y + 30F, realpha.reAlpha(new Color(50, 50, 50).getRGB(), 0.5F));
                RenderUtils.drawRect(x, y, potionData.animationX + x, y + 30F, realpha.reAlpha((new Color(0, 0, 0)).brighter().getRGB(), 0.55F));
                RenderUtil.drawShadow(x, y, 120, 30);
                float posY2 = y + 13F;
                UniFontLoaders.msFont16.drawString(name + " " + intToRomanByGreedy(potionEffect.getAmplifier() + 1), 32F + x, posY2 - mc.fontRendererObj.FONT_HEIGHT + 3, new Color(52, 97, 237).getRGB());
                UniFontLoaders.msFont16.drawString(Potion.getDurationString(potionEffect), 32F + x, posY2 + 4.0F, realpha.reAlpha((new Color(200, 200, 200)).getRGB(), 0.5F));
                if (potion.hasStatusIcon()) {
                    GlStateManager.pushMatrix();
                    GL11.glDisable(2929);
                    GL11.glEnable(3042);
                    GL11.glDepthMask(false);
                    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    int statusIconIndex = potion.getStatusIconIndex();
                    mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                    mc.ingameGUI.drawTexturedModalRect(7F + x, position + 2, statusIconIndex % 8 * 18, 198 + statusIconIndex / 8 * 18, 16, 16);
                    GL11.glDepthMask(true);
                    GL11.glDisable(3042);
                    GL11.glEnable(2929);
                    GlStateManager.popMatrix();
                }
                posY += 35;
            }
            GlStateManager.popMatrix();
        });
    }

    private double getAnimationState(double animation, double finalState, double speed) {
        float add = (float) (0.01 * speed);
        if (animation < finalState) {
            if (animation + add < finalState)
                animation += add;
            else
                animation = finalState;
        } else {
            if (animation - add > finalState)
                animation -= add;
            else
                animation = finalState;
        }
        return animation;
    }

    private String intToRomanByGreedy(int num) {
        final int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        final String[] symbols = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < values.length && num >= 0; i++)
            while (values[i] <= num) {
                num -= values[i];
                stringBuilder.append(symbols[i]);
            }

        return stringBuilder.toString();
    }
}
