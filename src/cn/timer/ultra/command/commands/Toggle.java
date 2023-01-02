package cn.timer.ultra.command.commands;

import cn.timer.ultra.Client;
import cn.timer.ultra.command.Command;
import cn.timer.ultra.module.Module;
import net.minecraft.util.ChatComponentText;

public class Toggle extends Command {

    public Toggle() {
        super("Toggle");
    }

    @Override
    public String execute(String[] args) {
        if (args.length != 1) {
            mc.thePlayer.addChatMessage(new ChatComponentText("\247cUsage: .toggle <Module>"));
        } else {
            Module mod = null;
            for (Module module : Client.instance.getModuleManager().modules) {
                if (!module.getName().equalsIgnoreCase(args[0])) continue;
                mod = module;
                break;
            }
            if (mod == null) {
                mc.thePlayer.addChatMessage(new ChatComponentText("\247cUnknown module. Type .modules for a list of modules"));
            } else {
                mod.setEnabled(!mod.isEnabled());
                mc.thePlayer.addChatMessage(new ChatComponentText("\247aToggled " + mod.getName()));
            }
        }
        return null;
    }
}
