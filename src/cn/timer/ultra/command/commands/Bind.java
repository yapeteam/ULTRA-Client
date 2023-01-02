package cn.timer.ultra.command.commands;

import cn.timer.ultra.Client;
import cn.timer.ultra.command.Command;
import cn.timer.ultra.module.Module;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;

public class Bind extends Command {
    public Bind() {
        super("bind");
    }

    @Override
    public String execute(String[] args) {
        if (args.length != 2) {
            mc.thePlayer.addChatMessage(new ChatComponentText("\247cUsage: .bind <Module> <Key>"));
        } else {
            Module mod = null;
            for (Module module : Client.instance.getModuleManager().modules) {
                if (!module.getName().equalsIgnoreCase(args[0])) continue;
                mod = module;
            }
            if (mod == null)
                mc.thePlayer.addChatMessage(new ChatComponentText("\247cUnknown module. Type .modules for a list of modules"));
            else {
                mod.setKey(Keyboard.getKeyIndex(args[1].toUpperCase()));
                mc.thePlayer.addChatMessage(new ChatComponentText("\247aBound " + mod.getName() + " to " + args[1].toUpperCase()));
            }
        }
        return null;
    }
}
