package cn.timer.ultra.event.events;

import cn.timer.ultra.event.Event;
import net.minecraft.entity.Entity;

public class EventAttack extends Event {
    private final Entity entity;

    public EventAttack(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }
}
