package cn.timer.ultra.command.commands;

import cn.timer.ultra.Client;
import cn.timer.ultra.command.Command;
import cn.timer.ultra.gui.ClickUI.ClickUIScreen;
import cn.timer.ultra.gui.ListedClickUi.ClickGui;
import cn.timer.ultra.module.modules.render.ClickGUI;

public class Vape extends Command {
    public Vape() {
        super("vape");
    }

    @Override
    public String execute(String[] args) {
        Client.instance.getModuleManager().getByClass(ClickGUI.class).vape = !Client.instance.getModuleManager().getByClass(ClickGUI.class).vape;
        ClickUIScreen.components.clear();
        ClickGui.frames.clear();
        return null;
    }
}
