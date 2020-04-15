package de.md5lukas.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseCommand extends SubCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return this.onCommand(sender, new LinkedList<>(Arrays.asList(args)));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return this.onTabComplete(sender, new LinkedList<>(Arrays.asList(args)));
    }
}
