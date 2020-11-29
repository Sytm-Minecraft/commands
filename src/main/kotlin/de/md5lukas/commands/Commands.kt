/*
 *     commands - Library to define commands in a Kotlin-ish way
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

package de.md5lukas.commands

import de.md5lukas.commands.player.PlayerCommand
import de.md5lukas.commands.player.PlayerCommandOptions
import de.md5lukas.commands.universal.UniversalCommand
import de.md5lukas.commands.universal.UniversalCommandOptions
import org.bukkit.command.CommandSender

/**
 * Creates a command using the [name] and initializes it with the given options
 *
 * @param name The name of the command
 * @param init The initializer for the options
 * @return A command that can be registered in a plugin
 */
inline fun command(name: String, init: UniversalCommandOptions<CommandSender>.() -> Unit): ExecutableCommand {
    return UniversalCommand(UniversalCommandOptions<CommandSender>(name).apply(init))
}

/**
 * Creates a player only command using the [name] and initializes it with the given options
 *
 * @param name The name of the command
 * @param init The initializer for the options
 * @return A command that can be registered in a plugin
 */
inline fun playerCommand(name: String, init: PlayerCommandOptions.() -> Unit): ExecutableCommand {
    return PlayerCommand(PlayerCommandOptions(name).apply(init))
}

@DslMarker
internal annotation class CommandDsl

internal object LambdaSingleton {

    val notFoundMessage: ((sender: CommandSender, fullCommand: String) -> Unit) = { sender, fullCommand ->
        sender.sendMessage("The command $fullCommand could not be found")
    }

    val helpHeader: ((
        sender: CommandSender,
        command: Command,
        currentPage: Int,
        allPages: Int,
    ) -> Unit) = { sender, command, currentPage, allPages ->
        sender.sendMessage("&6Help for &e${command.fullCommand}&7 Page &e$currentPage&7/&e$allPages")
    }

    val commandHelpFormatter: ((
        sender: CommandSender,
        command: Command,
        description: String
    ) -> Unit) = { sender, command, description ->
        sender.sendMessage(
            "&e${command.fullCommand}\n" +
                    "&7$description"
        )
    }

    val commandShortHelpFormatter: ((
        sender: CommandSender,
        command: Command,
        shortDescription: String
    ) -> Unit) = { sender, command, shortDescription ->
        sender.sendMessage("&e${command.fullCommand} &8- &7$shortDescription")
    }

    val shortDescription: (sender: CommandSender) -> String = { "No short description provided" }
    val description: (sender: CommandSender) -> String = { "No description provided" }

    val guard: (sender: CommandSender) -> Boolean = { true }

    val guardFailed: (sender: CommandSender) -> Unit = {
        it.sendMessage("You cannot access this command")
    }

    val tabCompleter: (sender: CommandSender) -> Set<String> = { emptySet() }

    val notAPlayerMessage: (sender: CommandSender) -> Unit = {
        it.sendMessage("You are not a player")
    }
}