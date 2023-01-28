package cn.timer.ultra.event.events;

import cn.timer.ultra.event.Event;

public class EventRenderShadow extends Event {

	private final float partialTicks;
	
	public EventRenderShadow(float partialTicks) {
		this.partialTicks = partialTicks;
	}

	public float getPartialTicks() {
		return partialTicks;
	}
}
