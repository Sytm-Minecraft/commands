package de.md5lukas.commands;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class SubCommand {

    private List<SubCommand> subCommands;

    public SubCommand() {
        subCommands = new ArrayList<>();
    }

    public boolean guard(CommandSender sender, List<String> args) {
        return true;
    }

    public SubCommand addSubCommand(SubCommand command) {
        subCommands.add(command);
        return this;
    }

    public abstract String getCommand();

    public List<String> getAliases() {
        return Collections.emptyList();
    }

    //<editor-fold desc="onCommand">
    public final boolean onCommand(CommandSender sender, LinkedList<String> args) {
        if (!guard(sender, args)) {
            return false;
        }
        if (args.isEmpty()) {
            return run(sender);
        }
        String subCommandString = args.getFirst().toLowerCase();
        for (SubCommand subCommand : subCommands) {
            if (subCommand.getCommand().equals(subCommandString) || subCommand.getAliases().contains(subCommandString)) {
                return subCommand.onCommand(sender, args);
            }
        }
        return run(sender, args);
    }

    public boolean run(CommandSender sender) {
        return false;
    }

    public boolean run(CommandSender sender, List<String> args) {
        return false;
    }
    //</editor-fold>

    //<editor-fold desc="onTabComplete">
    public final List<String> onTabComplete(CommandSender sender, LinkedList<String> args) {
        if (!guard(sender, args)) {
            return Collections.emptyList();
        }
        if (args.isEmpty()) {
            return tabComplete(sender);
        }
        String subCommandString = args.getFirst().toLowerCase();
        for (SubCommand subCommand : subCommands) {
            if (subCommand.getCommand().equals(subCommandString) || subCommand.getAliases().contains(subCommandString)) {
                return subCommand.onTabComplete(sender, args);
            }
        }
        List<String> completions = subCommands.stream().map(SubCommand::getCommand).collect(Collectors.toList());
        completions.addAll(tabComplete(sender, args));
        return completions;
    }

    public List<String> tabComplete(CommandSender sender) {
        return Collections.emptyList();
    }

    public List<String> tabComplete(CommandSender sender, List<String> args) {
        return Collections.emptyList();
    }
    //</editor-fold>
}
