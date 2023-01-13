package cn.timer.ultra.values;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Mode<V extends String> extends Value<V> {
    private final V[] modes;

    public Mode(String name, V[] modes, V mode) {
        super(name);
        this.modes = modes;
        this.setValue(mode);
    }

    public boolean isCurrentMode(String mode) {
        return this.getValue().equalsIgnoreCase(mode);
    }

    public V[] getModes() {
        return modes;
    }

    public void setMode(V mode) {
        for (V m : this.modes) {
            if (m.equals(mode)) {
                this.setValue(mode);
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Non mode!"));
            }
        }
    }
}
