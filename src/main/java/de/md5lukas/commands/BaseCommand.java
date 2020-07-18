package de.md5lukas.commands;

import com.google.common.base.Preconditions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that is the root of all sub-commands and gets registered as the command executor and tab completer
 */
public abstract class BaseCommand extends SubCommand implements CommandExecutor, TabCompleter {

    /**
     * The name of this command, the same as in the plugin.yml. The name in the plugin.yml has to be lowercase
     *
     * @param name The name of this command
     */
    public BaseCommand(String name) {
        super(name);
    }

    @Override
    public final boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.onCommand(sender, new LinkedList<>(Arrays.asList(args)));
        return true;
    }

    @Override
    public final List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return this.onTabComplete(sender, new LinkedList<>(Arrays.asList(args)));
    }

    /**
     * Registers this command as the executor and tab completer for the given command name
     *
     * @param plugin The plugin to use to register this command
     */
    @SuppressWarnings("ConstantConditions")
    public final void register(JavaPlugin plugin) {
        Preconditions
                .checkNotNull(plugin.getCommand(getName()), "It seems like the command with the name %s has not been registered in the plugin.yml", getName());
        plugin.getCommand(getName()).setExecutor(this);
        plugin.getCommand(getName()).setTabCompleter(this);
    }
}
