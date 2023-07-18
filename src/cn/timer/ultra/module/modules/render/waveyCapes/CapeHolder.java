package cn.timer.ultra.module.modules.render.waveyCapes;

import cn.timer.ultra.module.modules.player.WaveyCapes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public interface CapeHolder {
    StickSimulation getSimulation();

    default void updateSimulation(final EntityPlayer abstractClientPlayer, final int partCount) {
        StickSimulation simulation = this.getSimulation();
        boolean dirty = false;
        if (simulation.points.size() != partCount) {
            simulation.points.clear();
            simulation.sticks.clear();
            for (int i = 0; i < partCount; ++i) {
                StickSimulation.Point point = new StickSimulation.Point();
                point.position.y = -i;
                point.locked = (i == 0);
                simulation.points.add(point);
                if (i > 0) {
                    simulation.sticks.add(new StickSimulation.Stick(simulation.points.get(i - 1), point, 1.0f));
                }
            }
            dirty = true;
        }
        if (dirty) {
            for (int i = 0; i < 10; ++i) {
                this.simulate(abstractClientPlayer);
            }
        }
    }

    default void simulate(final EntityPlayer abstractClientPlayer) {
        StickSimulation simulation = this.getSimulation();
        if (simulation.points.isEmpty()) {
            return;
        }
        simulation.points.get(0).prevPosition.copy(simulation.points.get(0).position);
        double d = abstractClientPlayer.chasingPosX - abstractClientPlayer.posX;
        double m = abstractClientPlayer.chasingPosZ - abstractClientPlayer.posZ;
        float n = abstractClientPlayer.prevRenderYawOffset + abstractClientPlayer.renderYawOffset - abstractClientPlayer.prevRenderYawOffset;
        double o = Math.sin(n * 0.017453292f);
        double p = -Math.cos(n * 0.017453292f);
        float heightMul = WaveyCapes.heightMultiplier.getValue().floatValue();
        double fallHack = MathHelper.clamp_double(simulation.points.get(0).position.y - abstractClientPlayer.posY * heightMul, 0.0, 1.0);
        StickSimulation.Vector2 position = simulation.points.get(0).position;
        position.x += (float) (d * o + m * p + fallHack);
        simulation.points.get(0).position.y = (float) (abstractClientPlayer.posY * heightMul + (abstractClientPlayer.isSneaking() ? -4 : 0));
        simulation.simulate();
    }
}
