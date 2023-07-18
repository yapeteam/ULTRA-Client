// 
// Decompiled by Procyon v0.5.30
// 

package cn.timer.ultra.utils.simple;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class NetUtils {
    public static void sendPacket(final Packet packet) {
        Minecraft.getMinecraft().getNetHandler().getNetworkManager().dispatchPacket(packet, null);
    }
}
