package cn.timer.ultra.command;

import cn.timer.ultra.command.commands.Bind;
import cn.timer.ultra.command.commands.Help;
import cn.timer.ultra.command.commands.Toggle;
import cn.timer.ultra.event.EventTarget;
import cn.timer.ultra.event.events.EventSendChat;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class CommandManager {
    private final ArrayList<Command> commands = new ArrayList<>();

    public void init() {
        this.commands.add(new Bind());
        this.commands.add(new Toggle());
        this.commands.add(new Help());
    }

    private Optional<Command> getCommandByName(String name) {
        return this.commands.stream().filter(command -> command.getName().equalsIgnoreCase(name)).findFirst();
    }

    @EventTarget
    public void onSendChat(EventSendChat e) {
        if (e.getMessage().length() > 1 && e.getMessage().contains(".")) {
            e.setCancelled(true);
            String[] args = e.getMessage().toLowerCase().substring(1).split(" ");
            Optional<Command> command = this.getCommandByName(args[0]);
            if (command.isPresent()) {
                command.get().execute(Arrays.copyOfRange(args, 1, args.length));
            } else {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("\247cUnknown command. Try .help for a list of commands"));
            }
        }
    }
    public ArrayList<Command> getCommands(){
        return this.commands;
    }
}
