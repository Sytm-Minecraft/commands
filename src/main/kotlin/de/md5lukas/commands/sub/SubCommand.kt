/*
 *     commands-kt - Library to define commands in a Kotlin-ish way
 *     Copyright (C) 2020 Lukas Planz <lukas.planz@web.de>
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.md5lukas.commands.sub

import de.md5lukas.commands.Command
import de.md5lukas.commands.universal.UniversalCommandOptions
import de.md5lukas.commons.collections.PaginationList
import org.bukkit.command.CommandSender
import java.util.*
import kotlin.math.max
import kotlin.math.min

open class SubCommand<T : CommandSender>
@PublishedApi
internal constructor(
    private val rootOptions: UniversalCommandOptions<T>,
    options: SubCommandOptions<T>,
    fullCommand: String
) : Command {

    final override val name = options.name
    final override val fullCommand: String = fullCommand + name

    private val subCommands = options.subCommands.map { SubCommand(rootOptions, it, this.fullCommand) }

    private val aliases = options.aliases
    private val shortDescription = options.shortDescription
    private val description = options.description

    private val guard = options.guard
    private val permissionGuard = options.permissionGuard
    private val guardFailed = options.guardFailed

    private val run = options.run
    private val runArgs = options.runArgs

    private val tabCompleter = options.tabCompleter

    private val helpEntries: PaginationList<((sender: T) -> Unit)> =
        PaginationList(rootOptions.helpListingsPerPage)

    init {
        helpEntries.add(::printHelp)
        subCommands.forEach {
            helpEntries.add(it::printShortHelp)
        }
    }

    protected fun onCommand(sender: T, args: LinkedList<String>, fullCommand: String) {
        if (!checkGuard(sender)) {
            if (guardFailed != null) {
                guardFailed.invoke(sender)
            } else {
                val guardFailed = rootOptions.guardFailed
                    ?: throw IllegalStateException("The guardFailed option is null for this command ($name) and for the root command (${rootOptions.name})")
                guardFailed(sender)
            }
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
        args.removeFirst()
        val newFullCommand = if (fullCommand.isEmpty()) {
            subCommandName
        } else {
            "$fullCommand $subCommandName"
        }
        for (subCommand in subCommands) {
            if (subCommand.nameMatches(subCommandName)) {
                subCommand.onCommand(
                    sender,
                    args,
                    newFullCommand
                )
                return
            }
        }
        if (subCommandName.equals("help", true)) {
            val page = min(
                // Page number from arguments, if present, parse as int as possible, otherwise use 1
                max(
                    args.getOrNull(0)?.toIntOrNull() ?: 1,
                    1
                ),
                // The amount of available pages minus one, because it is not zero-indexed
                helpEntries.pages()
            )

            rootOptions.helpHeader(sender, this, page, helpEntries.pages())

            for (helpEntry in helpEntries) {
                helpEntry(sender)
            }

            return
        }
        if (runArgs == null) {
            rootOptions.notFoundMessage(sender, newFullCommand)
        } else {
            runArgs.invoke(sender, args)
        }
    }

    protected fun onTabComplete(sender: T, args: LinkedList<String>): Set<String> {
        if (!checkGuard(sender)) {
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

    private fun printHelp(sender: T) {
        rootOptions.commandHelpFormatter(sender, name, fullCommand, description(sender))
    }

    private fun printShortHelp(sender: T) {
        rootOptions.commandShortHelpFormatter(sender, name, fullCommand, shortDescription(sender))
    }

    private fun checkGuard(sender: T) =
        (if (permissionGuard != null) sender.hasPermission(permissionGuard) else true) && guard(sender)
}