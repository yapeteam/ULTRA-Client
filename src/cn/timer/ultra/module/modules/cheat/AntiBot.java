package cn.timer.ultra.module.modules.cheat;

import cn.timer.ultra.Client;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventTick;
import cn.timer.ultra.module.Category;
import cn.timer.ultra.module.CombatModule;
import cn.timer.ultra.values.Mode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Objects;

public class AntiBot extends CombatModule {
    private static Mode<String> mode;

    public AntiBot() {
        super("AntiBot", 0, Category.Cheat);
        this.addValues(AntiBot.mode);
    }

    @EventTarget
    public void onTick(final EventTick e) {
        setSuffix(AntiBot.mode.getValue());
    }

    public static double getEntitySpeed(final Entity entity) {
        final double xDif = entity.posX - entity.prevPosX;
        final double zDif = entity.posZ - entity.prevPosZ;
        return Math.sqrt(xDif * xDif + zDif * zDif) * 20.0;
    }

    public static boolean isServerBot(final Entity entity) {
        if (Client.instance.getModuleManager().getByClass(AntiBot.class).isEnabled()) {
            if (Objects.equals(AntiBot.mode.getValue(), antibotmode.Hypixel.name())) {
                for (final Entity ignored : AntiBot.mc.theWorld.loadedEntityList) {
                    if (!entity.getDisplayName().getFormattedText().startsWith("§") || entity.isInvisible() || entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")) {
                        return true;
                    }
                }
            } else if (Objects.equals(AntiBot.mode.getValue(), antibotmode.Mineplex.name())) {
                for (final EntityPlayer object : AntiBot.mc.theWorld.playerEntities) {
                    if (object != null && object != AntiBot.mc.thePlayer) {
                        if (!object.getName().startsWith("Body #") && object.getMaxHealth() != 20.0f) {
                            continue;
                        }
                        return true;
                    }
                }
            } else if (Objects.equals(AntiBot.mode.getValue(), antibotmode.Syuu.name())) {
                for (final Entity ignored : AntiBot.mc.theWorld.loadedEntityList) {
                    if (entity == AntiBot.mc.thePlayer) {
                        continue;
                    }
                    if (!(entity instanceof EntityPlayer)) {
                        continue;
                    }
                    final EntityPlayer entityPlayer = (EntityPlayer) entity;
                    if (entityPlayer.isInvisible() && entityPlayer.getHealth() > 1000.0f && getEntitySpeed(entityPlayer) > 20.0) {
                        return true;
                    }
                }
            } else
                return Objects.equals(AntiBot.mode.getValue(), antibotmode.Vanilla.name()) && (!entity.getDisplayName().getFormattedText().startsWith("\u00a7") || entity.isInvisible() || entity.getDisplayName().getFormattedText().toLowerCase().contains("npc"));
        }
        return false;
    }

    static {
        AntiBot.mode = new Mode<>("Mode", new String[]{
                antibotmode.Hypixel.name(),
                antibotmode.Mineplex.name(),
                antibotmode.Syuu.name(),
                antibotmode.Vanilla.name()
        }, antibotmode.Hypixel.name());
    }

    enum antibotmode {
        Hypixel,
        Mineplex,
        Syuu,
        Vanilla
    }
}
