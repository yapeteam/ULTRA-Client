package cn.timer.ultra.event.events;

import cn.timer.ultra.event.Event;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

public class EventModel extends Event {
    public ModelBiped biped;
    public Type type;
    public Entity entity;

    public EventModel(final ModelBiped biped, final Entity entity, final Type types) {
        this.biped = biped;
        this.type = types;
        this.entity = entity;
    }

    public ModelBiped getBiped() {
        return this.biped;
    }
}
