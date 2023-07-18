package cn.timer.ultra.event.events;

import cn.timer.ultra.event.Event;
import net.minecraft.network.Packet;

public class EventPacket extends Event {
    private final Packet packet;

    public EventPacket(Packet packet, Type type) {
        this.packet = packet;
        this.setType(type);
    }

    public Packet getPacket() {
        return packet;
    }
}
