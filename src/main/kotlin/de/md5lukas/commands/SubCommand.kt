package de.md5lukas.commands

import de.md5lukas.commands.dsl.options.BaseCommandOptions
import de.md5lukas.commands.dsl.options.SubCommandOptions
import de.md5lukas.commons.collections.PaginationList
import org.bukkit.command.CommandSender
import java.util.*

open class SubCommand(
    private val rootOptions: BaseCommandOptions,
    options: SubCommandOptions,
    fullCommand: String
) {

    private val fullCommand: String = fullCommand + options.name

    private val subCommands = options.subCommands.map { SubCommand(rootOptions, it, this.fullCommand) }

    protected val name = options.name
    private val aliases = options.aliases
    private val shortDescription = options.shortDescription
    private val description = options.description

    private val guard = options.guard
    private val guardFailed = options.guardFailed

    private val run = options.run
    private val runArgs = options.runArgs

    private val tabCompleter = options.tabCompleter

    private val helpEntries: PaginationList<((sender: CommandSender) -> Unit)> =
        PaginationList(rootOptions.helpListingsPerPage)

    init {
        helpEntries.add(::printHelp)
        subCommands.forEach {
            helpEntries.add(it::printShortHelp)
        }
    }

    protected fun onCommand(sender: CommandSender, args: LinkedList<String>, fullCommand: String) {
        if (!guard(sender)) {
            guardFailed(sender)
            return
        }
        if (args.isEmpty()) {
            if (run == null) {
                rootOptions.notFoundMessage(sender, fullCommand)
            } else {
                run.invoke(sender)
            }
            return
        }
        val subCommandName = args.first().toLowerCase()
        val newFullCommand = if (fullCommand.isEmpty()) {
            subCommandName
        } else {
            "$fullCommand $subCommandName"
        }
        for (subCommand in subCommands) {
            if (subCommand.nameMatches(subCommandName)) {
                args.removeFirst()
                subCommand.onCommand(
                    sender,
                    args,
                    newFullCommand
                )
                return
            }
        }
        if (runArgs == null) {
            rootOptions.notFoundMessage(sender, newFullCommand)
        } else {
            runArgs.invoke(sender, args)
        }
    }

    protected fun onTabComplete(sender: CommandSender, args: LinkedList<String>): Set<String> {
        if (!guard(sender)) {
            return emptySet()
        }
        when (args.size) {
            0 -> return emptySet()
            1 -> {
                return subCommands.map { it.name }.toMutableSet().also {
                    it.addAll(tabCompleter(sender))
                }
            }
            else -> {
                val subCommandName = args.removeFirstOrNull()?.toLowerCase()
                if (subCommandName != null) {
                    for (subCommand in subCommands) {
                        if (subCommand.nameMatches(subCommandName)) {
                            return subCommand.onTabComplete(sender, args)
                        }
                    }
                }
                return emptySet()
            }
        }
    }

    private fun nameMatches(string: String): Boolean {
        return name == string || aliases.contains(string)
    }

    private fun printHelp(sender: CommandSender) {
        rootOptions.helpFormatter(sender, name, fullCommand, description(sender))
    }

    private fun printShortHelp(sender: CommandSender) {
        rootOptions.shortHelpFormatter(sender, name, fullCommand, shortDescription(sender))
    }
}