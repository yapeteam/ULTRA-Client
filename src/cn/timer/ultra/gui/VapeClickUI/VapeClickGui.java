package cn.timer.ultra.gui.VapeClickUI;

import cn.timer.ultra.Client;
import cn.timer.ultra.gui.Font.UniFontRenderer.UniFontLoaders;
import cn.timer.ultra.module.CombatModule;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.module.modules.render.ClickGUI;
import cn.timer.ultra.utils.fdp.RenderUtils;
import cn.timer.ultra.utils.ultra.TimerUtil;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import cn.timer.ultra.utils.ultra.render.Stencil;
import cn.timer.ultra.values.Booleans;
import cn.timer.ultra.values.Mode;
import cn.timer.ultra.values.Numbers;
import cn.timer.ultra.values.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class VapeClickGui extends GuiScreen {
    private boolean close = false;
    private boolean closed;
    private float dragX, dragY;
    private int valuemodx = 0;
    private static float modsRole, modsRoleNow;
    private static float valueRoleNow, valueRole;
    public float lastPercent;
    public float percent;
    public float percent2;
    public float lastPercent2;
    public float outro;
    public float lastOutro;

    @Override
    public void initGui() {
        super.initGui();
        close = false;
        percent = 1.33f;
        lastPercent = 1f;
        percent2 = 1.33f;
        lastPercent2 = 1f;
        outro = 1;
        lastOutro = 1;
        valuetimer.reset();
        modules.clear();
        for (Module module : Client.instance.getModuleManager().getModules()) {
            if (module instanceof CombatModule && Client.instance.getModuleManager().getByClass(ClickGUI.class).vape)
                modules.add((CombatModule) module);
        }
    }


    /*
    主窗口宽度 = 500
    主窗口高度 = 310
    功能列表起始位置 = 100
    功能宽度 = 325(未开values)
    功能起始高度 = 60
     */
    static float windowX = 200, windowY = 200;
    static float width = 500, height = 310;

    static ClickType selectType = ClickType.Home;
    static Module selectMod;

    float[] typeXAnim = new float[]{windowX + 10, windowX + 10, windowX + 10, windowX + 10};

    TimerUtil valuetimer = new TimerUtil();

    public float smoothTrans(double current, double last) {
        return (float) (current + (last - current) / (Minecraft.getDebugFPS() / 10));
    }

    private final ArrayList<CombatModule> modules = new ArrayList<>();

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sResolution = new ScaledResolution(mc);
        ScaledResolution sr = new ScaledResolution(mc);


        float outro = smoothTrans(this.outro, lastOutro);
        if (mc.currentScreen == null) {
            GlStateManager.translate(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, 0);
            GlStateManager.scale(outro, outro, 0);
            GlStateManager.translate(-sr.getScaledWidth() / 2f, -sr.getScaledHeight() / 2f, 0);
        }


        //animation
        percent = smoothTrans(this.percent, lastPercent);
        percent2 = smoothTrans(this.percent2, lastPercent2);


        if (percent > 0.98) {
            GlStateManager.translate(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, 0);
            GlStateManager.scale(percent, percent, 0);
            GlStateManager.translate(-sr.getScaledWidth() / 2f, -sr.getScaledHeight() / 2f, 0);
        } else {
            if (percent2 <= 1) {
                GlStateManager.translate(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, 0);
                GlStateManager.scale(percent2, percent2, 0);
                GlStateManager.translate(-sr.getScaledWidth() / 2f, -sr.getScaledHeight() / 2f, 0);
            }
        }


        if (percent <= 1.5 && close) {
            percent = smoothTrans(this.percent, 2);
            percent2 = smoothTrans(this.percent2, 2);
        }

        if (percent >= 1.4 && close) {
            percent = 1.5f;
            closed = true;
            mc.currentScreen = null;
        }

        drawGradientRect(0, 0, sResolution.getScaledWidth(), sResolution.getScaledHeight(), new Color(0, 0, 0, 30).getRGB(), new Color(107, 147, 255, 100).getRGB());

        //拖动
        if (isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
            if (dragX == 0 && dragY == 0) {
                dragX = mouseX - windowX;
                dragY = mouseY - windowY;
            } else {
                windowX = mouseX - dragX;
                windowY = mouseY - dragY;
            }
        } else if (dragX != 0 || dragY != 0) {
            dragX = 0;
            dragY = 0;
        }


        //绘制主窗口
        RenderUtil.drawRoundedRect(windowX, windowY, windowX + width, windowY + height, 5, new Color(21, 22, 25).getRGB());
        if (selectMod == null) {
            UniFontLoaders.PingFangMedium20.drawString(Client.CLIENT_NAME.toUpperCase(Locale.ROOT), windowX + 20, windowY + height - 20, new Color(77, 78, 84).getRGB());
        }
        //绘制顶部图标
        float typeX = windowX + 20;
        int i = 0;
        for (Enum<?> e : ClickType.values()) {
            if (!isHovered(windowX, windowY, windowX + width, windowY + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                if (typeXAnim[i] != typeX) {
                    typeXAnim[i] += (typeX - typeXAnim[i]) / 20;
                }
            } else {
                if (typeXAnim[i] != typeX) {
                    typeXAnim[i] = typeX;
                }
            }
            if (e != ClickType.Settings) {
                if (e == selectType) {
                    RenderUtil.drawImage(new ResourceLocation("client/vapeclickgui/" + e.name().toLowerCase() + ".png"), typeXAnim[i], windowY + 10, 16, 16, new Color(255, 255, 255));
                    UniFontLoaders.PingFangMedium18.drawString(e.name(), typeXAnim[i] + 20, windowY + 15, new Color(255, 255, 255).getRGB());
                    typeX += (32 + UniFontLoaders.PingFangMedium18.getStringWidth(e.name() + " "));
                } else {
                    RenderUtil.drawImage(new ResourceLocation("client/vapeclickgui/" + e.name().toLowerCase() + ".png"), typeXAnim[i], windowY + 10, 16, 16, new Color(79, 80, 86));
                    typeX += (32);
                }
            } else {
                RenderUtil.drawImage(new ResourceLocation("client/vapeclickgui/" + e.name().toLowerCase() + ".png"), windowX + width - 20, windowY + 10, 16, 16, e == selectType ? new Color(255, 255, 255) : new Color(79, 80, 86));
            }
            i++;
        }


        if (selectType == ClickType.Home) {
            Stencil.write(false);
            RenderUtil.drawRect2(windowX, windowY, width, height, -1);
            Stencil.erase(true);
            if (selectMod != null) {
                if (valuemodx > -80) {
                    valuemodx -= 5;
                }
            } else {
                if (valuemodx < 0) {
                    valuemodx += 5;
                }
            }

            if (selectMod != null) {
                RenderUtil.drawRoundedRect(windowX + 430 + valuemodx, windowY + 60, windowX + width, windowY + height - 20, 3, new Color(32, 31, 35).getRGB());
                RenderUtil.drawRoundedRect(windowX + 430 + valuemodx, windowY + 60, windowX + width, windowY + 85, 3, new Color(39, 38, 42).getRGB());
                RenderUtil.drawImage(new ResourceLocation("client/vapeclickgui/back.png"), windowX + 435 + valuemodx, windowY + 65, 16, 16, new Color(82, 82, 85));
                if (isHovered(windowX + 435 + valuemodx, windowY + 65, windowX + 435 + valuemodx + 16, windowY + 65 + 16, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    selectMod = null;
                    valuetimer.reset();
                }


                //滚动
                int dWheel = Mouse.getDWheel();
                if (isHovered(windowX + 430 + valuemodx, windowY + 60, windowX + width, windowY + height - 20, mouseX, mouseY)) {
                    if (dWheel < 0 && Math.abs(valueRole) + 170 < (selectMod.getValues().size() * 25)) {
                        valueRole -= 32;
                    }
                    if (dWheel > 0 && valueRole < 0) {
                        valueRole += 32;
                    }
                }

                if (valueRoleNow != valueRole) {
                    valueRoleNow += (valueRole - valueRoleNow) / 20;
                    valueRoleNow = (int) valueRoleNow;
                }

                float valuey = windowY + 100 + valueRoleNow;

                if (selectMod == null) {
                    return;
                }

                for (Value v : selectMod.getValues()) {
                    if (v instanceof Booleans) {
                        if (valuey + 4 > windowY + 100) {
                            if (((Boolean) v.getValue())) {
                                UniFontLoaders.PingFangMedium16.drawString(v.getName(), windowX + 445 + valuemodx, valuey + 4, -1);
                                v.optionAnim = 100;
                                RenderUtil.drawRoundedRect(windowX + width - 30, valuey + 2, windowX + width - 10, valuey + 12, 4, new Color(33, 94, 181, (int) (v.optionAnimNow / 100 * 255)).getRGB());
                                RenderUtils.drawCircle(windowX + width - 25 + 10 * (v.optionAnimNow / 100f), valuey + 7, 3.5f, new Color(255, 255, 255).getRGB());
                            } else {
                                UniFontLoaders.PingFangMedium14.drawString(v.getName(), windowX + 445 + valuemodx, valuey + 4, new Color(73, 72, 76).getRGB());
                                v.optionAnim = 0;
                                RenderUtil.drawRoundedRect(windowX + width - 30, valuey + 2, windowX + width - 10, valuey + 12, 4, new Color(59, 60, 65).getRGB());
                                RenderUtil.drawRoundedRect(windowX + width - 29, valuey + 3, windowX + width - 11, valuey + 11, 3, new Color(32, 31, 35).getRGB());
                                RenderUtils.drawCircle(windowX + width - 25 + 10 * (v.optionAnimNow / 100f), valuey + 7, 3.5f, new Color(59, 60, 65).getRGB());
                            }
                            if (isHovered(windowX + width - 30, valuey + 2, windowX + width - 10, valuey + 12, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                                if (valuetimer.delay(300)) {
                                    v.setValue(!(Boolean) v.getValue());
                                    valuetimer.reset();
                                }
                            }
                        }

                        if (v.optionAnimNow != v.optionAnim) {
                            v.optionAnimNow += (v.optionAnim - v.optionAnimNow) / 20;
                        }
                        valuey += 25;
                    }
                }
                for (Value v : selectMod.getValues()) {
                    if (v instanceof Numbers) {
                        if (valuey + 4 > windowY + 100) {

                            float present = ((windowX + width - 11) - (windowX + 450 + valuemodx))
                                    * (((Number) v.getValue()).floatValue() - ((Numbers<?>) v).getMin().floatValue())
                                    / (((Numbers<?>) v).floatMax() - ((Numbers<?>) v).floatMin());

                            UniFontLoaders.PingFangMedium16.drawString(v.getName(), windowX + 445 + valuemodx, valuey + 5, new Color(73, 72, 76).getRGB());
                            UniFontLoaders.PingFangMedium16.drawCenteredString(v.getValue().toString(), windowX + width - 20, valuey + 5, new Color(255, 255, 255).getRGB());
                            RenderUtil.drawRect(windowX + 450 + valuemodx, valuey + 20, windowX + width - 11, valuey + 21.5f, new Color(77, 76, 79).getRGB());
                            RenderUtil.drawRect(windowX + 450 + valuemodx, valuey + 20, windowX + 450 + valuemodx + present, valuey + 21.5f, new Color(43, 116, 226).getRGB());
                            RenderUtils.drawCircle(windowX + 450 + valuemodx + present, valuey + 21f, 5, new Color(32, 31, 35).getRGB());
                            RenderUtils.drawCircle(windowX + 450 + valuemodx + present, valuey + 21f, 5, new Color(32, 31, 35).getRGB());
                            RenderUtils.drawCircle(windowX + 450 + valuemodx + present, valuey + 21f, 4, new Color(44, 115, 224).getRGB());
                            RenderUtils.drawCircle(windowX + 450 + valuemodx + present, valuey + 21f, 4, new Color(44, 115, 224).getRGB());

                            if (isHovered(windowX + 450 + valuemodx, valuey + 18, windowX + width - 11, valuey + 23.5f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                                float render2 = ((Numbers<?>) v).getMin().floatValue();
                                double max = ((Numbers<?>) v).getMax().doubleValue();
                                double inc = 0.1;
                                double valAbs = (double) mouseX - ((double) (windowX + 450 + valuemodx));
                                double perc = valAbs / (((windowX + width - 11) - (windowX + 450 + valuemodx)));
                                perc = Math.min(Math.max(0.0D, perc), 1.0D);
                                double valRel = (max - render2) * perc;
                                double val = render2 + valRel;
                                val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
                                v.setValue(Double.valueOf(val));
                            }
                        }
                        valuey += 25;
                    }
                }
                for (Value v : selectMod.getValues()) {
                    if (v instanceof Mode) {
                        Mode<?> modeValue = (Mode<?>) v;

                        if (valuey + 4 > windowY + 100 & valuey < (windowY + height)) {
                            RenderUtil.drawRoundedRect(windowX + 445 + valuemodx, valuey + 2, windowX + width - 5, valuey + 22, 2, new Color(46, 45, 48).getRGB());
                            RenderUtil.drawRoundedRect(windowX + 446 + valuemodx, valuey + 3, windowX + width - 6, valuey + 21, 2, new Color(32, 31, 35).getRGB());
                            UniFontLoaders.PingFangMedium18.drawString(v.getName() + ":" + modeValue.getValue(), windowX + 455 + valuemodx, valuey + 10, new Color(230, 230, 230).getRGB());
                            UniFontLoaders.PingFangMedium18.drawString(">", windowX + width - 15, valuey + 9, new Color(73, 72, 76).getRGB());
                            if (isHovered(windowX + 445 + valuemodx, valuey + 2, windowX + width - 5, valuey + 22, mouseX, mouseY) && Mouse.isButtonDown(0) && valuetimer.delay(300)) {
                                if (Arrays.binarySearch(modeValue.getModes(), (v.getValue()))
                                        + 1 < modeValue.getModes().length) {
                                    v.setValue(modeValue.getModes()[Arrays.binarySearch(modeValue.getModes(), (v.getValue())) + 1]);
                                } else {
                                    v.setValue(modeValue.getModes()[0]);
                                }
                                valuetimer.reset();
                            }
                        }
                        valuey += 25;
                    }
                }
            }

            float modY = windowY + 70 + modsRoleNow;
            for (CombatModule module : modules) {
                if (isHovered(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    if (valuetimer.delay(300) && modY + 40 > (windowY + 70) && modY < (windowY + height)) {
                        module.setEnabled(!module.isEnabled());
                        valuetimer.reset();
                    }
                } else if (isHovered(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, mouseX, mouseY) && Mouse.isButtonDown(1)) {
                    if (valuetimer.delay(300)) {
                        if (selectMod != module) {
                            valueRole = 0;
                            selectMod = module;
                        } else {
                            selectMod = null;
                        }
                        valuetimer.reset();
                    }
                }

                if (isHovered(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, mouseX, mouseY)) {
                    if (module.isEnabled()) {
                        RenderUtil.drawRoundedRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, 3, new Color(43, 41, 45).getRGB());
                    } else {
                        RenderUtil.drawRoundedRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, 3, new Color(35, 35, 35).getRGB());
                    }
                } else {
                    if (module.isEnabled()) {
                        RenderUtil.drawRoundedRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, 3, new Color(36, 34, 38).getRGB());
                    } else {
                        RenderUtil.drawRoundedRect(windowX + 100 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, 3, new Color(32, 31, 33).getRGB());
                    }
                }
                RenderUtil.drawRoundedRect(windowX + 100 + valuemodx, modY - 10, windowX + 125 + valuemodx, modY + 25, 3, new Color(37, 35, 39).getRGB());
                RenderUtil.drawRoundedRect(windowX + 410 + valuemodx, modY - 10, windowX + 425 + valuemodx, modY + 25, 3, new Color(39, 38, 42).getRGB());
                UniFontLoaders.PingFangMedium20.drawString(".", windowX + 416 + valuemodx, modY - 5, new Color(66, 64, 70).getRGB());
                UniFontLoaders.PingFangMedium20.drawString(".", windowX + 416 + valuemodx, modY - 1, new Color(66, 64, 70).getRGB());
                UniFontLoaders.PingFangMedium20.drawString(".", windowX + 416 + valuemodx, modY + 3, new Color(66, 64, 70).getRGB());

                if (module.isEnabled()) {
                    UniFontLoaders.PingFangMedium18.drawString(module.getName(), windowX + 140 + valuemodx, modY + 5, new Color(220, 220, 220).getRGB());
                    RenderUtil.drawRoundedRect(windowX + 100 + valuemodx, modY - 10, windowX + 125 + valuemodx, modY + 25, 3, new Color(41, 117, 221, (int) (module.optionAnimNow / 100f * 255)).getRGB());
                    RenderUtil.drawImage(new ResourceLocation("client/vapeclickgui/module.png"), windowX + 105 + valuemodx, modY, 16, 16, new Color(220, 220, 220));
                    module.optionAnim = 100;

                    RenderUtil.drawRoundedRect(windowX + 380 + valuemodx, modY + 2, windowX + 400 + valuemodx, modY + 12, 4, new Color(33, 94, 181, (int) (module.optionAnimNow / 100f * 255)).getRGB());
                    RenderUtils.drawCircle(windowX + 385 + 10 * module.optionAnimNow / 100 + valuemodx, modY + 7, 3.5f, new Color(255, 255, 255).getRGB());
                } else {
                    UniFontLoaders.PingFangMedium18.drawString(module.getName(), windowX + 140 + valuemodx, modY + 5, new Color(108, 109, 113).getRGB());
                    RenderUtil.drawImage(new ResourceLocation("client/vapeclickgui/module.png"), windowX + 105 + valuemodx, modY, 16, 16, new Color(92, 90, 94));
                    module.optionAnim = 0;

                    RenderUtil.drawRoundedRect(windowX + 380 + valuemodx, modY + 2, windowX + 400 + valuemodx, modY + 12, 4, new Color(59, 60, 65).getRGB());
                    RenderUtil.drawRoundedRect(windowX + 381 + valuemodx, modY + 3, windowX + 399 + valuemodx, modY + 11, 3, new Color(29, 27, 31).getRGB());
                    RenderUtils.drawCircle(windowX + 385 + 10 * module.optionAnimNow / 100 + valuemodx, modY + 7, 3.5f, new Color(59, 60, 65).getRGB());
                }

                if (module.optionAnimNow != module.optionAnim) {
                    module.optionAnimNow += (module.optionAnim - module.optionAnimNow) / 20;
                }


                modY += 40;
            }
            //滚动
            int dWheel2 = Mouse.getDWheel();
            if (isHovered(windowX + 100 + valuemodx, windowY + 60, windowX + 425 + valuemodx, windowY + height, mouseX, mouseY)) {
                if (dWheel2 < 0 && Math.abs(modsRole) + 220 < (modules.size() * 40)) {
                    modsRole -= 32;
                }
                if (dWheel2 > 0 && modsRole < 0) {
                    modsRole += 32;
                }
            }

            if (modsRoleNow != modsRole) {
                modsRoleNow += (modsRole - modsRoleNow) / 20;
                modsRoleNow = (int) modsRoleNow;
            }


            Stencil.dispose();
        }
        int dWheel2 = Mouse.getDWheel();
        if (isHovered(windowX + 100 + valuemodx, windowY + 60, windowX + 425 + valuemodx, windowY + height, mouseX, mouseY)) {
            if (dWheel2 < 0 && Math.abs(modsRole) + 220 < (modules.size() * 40)) {
                modsRole -= 16;
            }
            if (dWheel2 > 0 && modsRole < 0) {
                modsRole += 16;
            }
        }

        if (modsRoleNow != modsRole) {
            modsRoleNow += (modsRole - modsRoleNow) / 20;
            modsRoleNow = (int) modsRoleNow;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        //顶部图标
        float typeX = windowX + 20;
        for (ClickType e : ClickType.values()) {
            if (e != ClickType.Settings) {
                if (e == selectType) {
                    if (isHovered(typeX, windowY + 10, typeX + 16 + UniFontLoaders.PingFangMedium18.getStringWidth(e.name() + " "), windowY + 10 + 16, mouseX, mouseY)) {
                        selectType = e;
                    }
                    typeX += (32 + UniFontLoaders.PingFangMedium18.getStringWidth(e.name() + " "));
                } else {
                    if (isHovered(typeX, windowY + 10, typeX + 16, windowY + 10 + 16, mouseX, mouseY)) {
                        selectType = e;
                    }
                    typeX += (32);
                }
            } else {
                if (isHovered(windowX + width - 32, windowY + 10, windowX + width, windowY + 10 + 16, mouseX, mouseY)) {
                    selectType = e;
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (!closed && keyCode == Keyboard.KEY_ESCAPE) {
            close = true;
            mc.mouseHelper.grabMouseCursor();
            mc.inGameHasFocus = true;
            return;
        }

        if (close) {
            this.mc.displayGuiScreen(null);
        }

        try {
            super.keyTyped(typedChar, keyCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    @Override
    public void onGuiClosed() {
        System.out.println(2);
    }
}
