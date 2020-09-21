package de.md5lukas.commands

import de.md5lukas.commands.dsl.options.BaseCommandOptions
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class BaseCommand(options: BaseCommandOptions) : SubCommand(options, options), CommandExecutor, TabCompleter {

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val linkedArgs = LinkedList<String>()
        args.forEach { linkedArgs.add(it) }

        this.onCommand(sender, linkedArgs, "")

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        val linkedArgs = LinkedList<String>()
        args.forEach { linkedArgs.add(it) }

        return ArrayList(this.onTabComplete(sender, linkedArgs))
    }

    fun register(plugin: JavaPlugin) {
        val pc = plugin.getCommand(name)
            ?: throw IllegalStateException("The plugin ${plugin.name} has not a command registered with the name $name")
        pc.setExecutor(this)
        pc.tabCompleter = this
    }
}