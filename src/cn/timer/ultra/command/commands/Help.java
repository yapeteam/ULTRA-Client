package cn.timer.ultra.command.commands;

import cn.timer.ultra.Client;
import cn.timer.ultra.command.Command;
import net.minecraft.util.ChatComponentText;

public class Help extends Command {
    public Help() {
        super("help");
    }

    @Override
    public String execute(String[] args) {
        Client.instance.getCommandManager().getCommands().forEach(command -> mc.thePlayer.addChatMessage(new ChatComponentText(command.getName())));
        return null;
    }
}
