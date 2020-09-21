package de.md5lukas.commands.dsl.options

import org.bukkit.command.CommandSender

open class SubCommandOptions(val name: String) {

    init {
        if (name.contains(' ')) {
            throw IllegalArgumentException("Command names may not contain spaces (\"$name\")")
        }
    }

    val subCommands: MutableList<SubCommandOptions> = ArrayList()

    fun subcommand(name: String, init: SubCommandOptions.() -> Unit) {
        subCommands.add(SubCommandOptions(name).apply(init))
    }

    var aliases: Set<String> = emptySet()
        set(value) {
            value.forEach {
                if (it.contains(' ')) {
                    throw IllegalArgumentException("Command name aliases may not contain spaces (\"$it\")")
                }
            }
            field = value
        }

    var shortDescription: (sender: CommandSender) -> String = { "No description provided" }
    var description: (sender: CommandSender) -> String = { this.shortDescription(it) }

    var guard: (sender: CommandSender) -> Boolean = { true }

    var guardFailed: (sender: CommandSender) -> Unit = {
        it.sendMessage("You cannot access this command")
    }

    var run: ((sender: CommandSender) -> Unit)? = null

    var runArgs: ((sender: CommandSender, args: List<String>) -> Unit)? = null

    var tabCompleter: ((sender: CommandSender) -> Set<String>) = { emptySet() }
}