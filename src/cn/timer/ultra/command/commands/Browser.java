package cn.timer.ultra.command.commands;

import cn.timer.ultra.command.Command;

public class Browser extends Command {
    public Browser() {
        super("browser");
    }

    @Override
    public String execute(String[] args) {
        if (args.length == 1)
            new cn.timer.ultra.cef.Browser(args[0]).open();
        return null;
    }
}
