package cn.timer.ultra.event.events;


import cn.timer.ultra.event.Event;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class EventRenderCape
        extends Event {
    private static ResourceLocation capeLocation;
    private final EntityPlayer player;

    public EventRenderCape(ResourceLocation capeLocation, EntityPlayer player) {
        this.capeLocation = capeLocation;
        this.player = player;
    }

    public static ResourceLocation getLocation() {
        return capeLocation;
    }

    public void setLocation(ResourceLocation location) {
        capeLocation = location;
    }

    public EntityPlayer getPlayer() {
        return player;
    }
}

