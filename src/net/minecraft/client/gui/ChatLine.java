package net.minecraft.client.gui;

import cn.timer.ultra.utils.AnimationUtils;
import net.minecraft.util.*;

public class ChatLine
{
    private final int updateCounterCreated;
    private final IChatComponent lineString;
    public float x;
    public AnimationUtils animationUtils;
    private final int chatLineID;
    
    public ChatLine(final int p_i45000_1_, final IChatComponent p_i45000_2_, final int p_i45000_3_) {
        this.animationUtils = new AnimationUtils();
        this.lineString = p_i45000_2_;
        this.updateCounterCreated = p_i45000_1_;
        this.chatLineID = p_i45000_3_;
    }
    
    public IChatComponent getChatComponent() {
        return this.lineString;
    }
    
    public int getUpdatedCounter() {
        return this.updateCounterCreated;
    }
    
    public int getChatLineID() {
        return this.chatLineID;
    }
}
