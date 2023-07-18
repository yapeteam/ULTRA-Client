package cn.timer.ultra.utils.mobends.animation;

import cn.timer.ultra.utils.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

public abstract class Animation {
    public abstract void animate(EntityLivingBase argEntity, ModelBase argModel, EntityData argData);

    public abstract String getName();
}
