package cn.timer.ultra.module.modules.overlay;

import cn.timer.ultra.Client;
import cn.timer.ultra.gui.Font.CFontRenderer.CFontLoaders;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.HUDModule;
import cn.timer.ultra.module.Module;
import cn.timer.ultra.utils.ultra.render.GradientUtil;
import cn.timer.ultra.utils.ultra.render.RenderUtil;
import cn.timer.ultra.values.Booleans;
import cn.timer.ultra.values.Numbers;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class JelloArraylist extends HUDModule {
    ResourceLocation wtf = new ResourceLocation("client/shadow/arraylistshadow.png");
    List<Module> modules = new ArrayList<>();
    private final Numbers<Integer> animateSpeed = new Numbers<>("Animate-Speed", 1, 20, 5);
    private final Booleans gradient = new Booleans("Gradient", false);

    public JelloArraylist() {
        super("JelloArraylist", Keyboard.KEY_NONE, Category.Overlay, 50, 100, 0, 0, "free", "free");
        registerOverlay((e) -> {
            this.updateElements(e.getPartialTicks()); //fps async
            this.renderArraylist();
        });
        addValues(animateSpeed, gradient);
    }

    long start;

    @Override
    public void onEnable() {
        start = System.currentTimeMillis();
    }

    public void updateElements(float partialTicks) {
        if (Long.MAX_VALUE - 100 < start) {
            start = 0;
        }
        modules = Client.instance.getModuleManager().getModules()
                .stream()
                .filter(mod -> !mod.getName().equalsIgnoreCase("JelloArraylist"))
                .sorted(new ModComparator())
                .collect(Collectors.toCollection(ArrayList::new));

        float tick = 1F - partialTicks;

        for (Module module : modules) {
            module.setAnimation(module.getAnimation() + ((module.isEnabled() ? animateSpeed.floatValue() : -animateSpeed.floatValue()) * tick));
            module.setAnimation(MathHelper.clamp_float(module.getAnimation(), 0F, 20F));
        }
    }

    public void renderArraylist() {
        AtomicReference<Float> yStart = new AtomicReference<>(getYPosition());
        AtomicReference<Float> xStart = new AtomicReference<>((float) 0);
        height = 0;
        GlStateManager.disableBlend();
        for (Module module : modules) {
            if (module.getAnimation() <= 0F) continue;
            xStart.set(getXPosition() + width - CFontLoaders.jello18.getStringWidth(module.getName()) - 5);

            if (width < CFontLoaders.jello18.getStringWidth(module.getName())) {
                width = CFontLoaders.jello18.getStringWidth(module.getName());
            }

            height += (7.5f + 5.25f) * (module.getAnimation() / 20F);
            GlStateManager.pushMatrix();
            GlStateManager.disableAlpha();
            RenderUtil.drawImage(wtf, xStart.get() - 8 - 2 - 1, yStart.get() + 2 - 2.5f - 1.5f - 1.5f - 1.5f - 6 - 1, CFontLoaders.jello18.getStringWidth(module.getName()) + 20 + 10, (int) (18.5 + 6 + 12 + 2), (module.getAnimation() / 20F) * 0.7f);
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
            yStart.updateAndGet(v -> v + (7.5f + 5.25f) * (module.getAnimation() / 20F));
        }

        yStart.set(getYPosition());
        if (gradient.getValue()) {
            GradientUtil.applyGradientVertical(getXPosition(), getYPosition(), width, height, 1, Client.instance.getClientColor(), Client.instance.getAlternateClientColor(), () -> {
                for (Module module : modules) {
                    if (module.getAnimation() <= 0F) continue;
                    xStart.set(getXPosition() + width - CFontLoaders.jello18.getStringWidth(module.getName()) - 5);
                    GlStateManager.disableBlend();
                    CFontLoaders.jello18.drawString(module.getName(), xStart.get(), yStart.get() + 7.5f, new Color(1F, 1F, 1F, (module.getAnimation() / 20F) * 0.7f).getRGB());
                    GlStateManager.enableAlpha();
                    yStart.updateAndGet(v -> v + (7.5f + 5.25f) * (module.getAnimation() / 20F));
                }
            });
        } else {
            for (Module module : modules) {
                if (module.getAnimation() <= 0F) continue;
                xStart.set(getXPosition() + width - CFontLoaders.jello18.getStringWidth(module.getName()) - 5);
                GlStateManager.disableBlend();
                CFontLoaders.jello18.drawString(module.getName(), xStart.get(), yStart.get() + 7.5f, new Color(1F, 1F, 1F, (module.getAnimation() / 20F) * 0.7f).getRGB());
                GlStateManager.enableAlpha();
                yStart.updateAndGet(v -> v + (7.5f + 5.25f) * (module.getAnimation() / 20F));
            }
        }
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
    }

    static class ModComparator implements Comparator<Module> {
        @Override
        public int compare(Module e1, Module e2) {
            return (CFontLoaders.jello18.getStringWidth(e1.getName()) < CFontLoaders.jello18.getStringWidth(e2.getName()) ? 1 : -1);
        }
    }
}