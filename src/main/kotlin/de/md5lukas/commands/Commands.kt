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

package de.md5lukas.commands

import org.bukkit.command.CommandSender

inline fun command(name: String, init: CommandOptions.() -> Unit): Command {
    return Command(CommandOptions(name).apply(init))
}

@DslMarker
internal annotation class CommandDsl

internal object LambdaSingleton {

    val notFoundMessage: ((sender: CommandSender, fullCommand: String) -> Unit) = { _, _ ->
        TODO("Not found message not implemented yet")
    }

    val helpFormatter: ((
        sender: CommandSender,
        name: String,
        fullCommand: String,
        description: String
    ) -> Unit) = { sender, fullCommand, _, description ->
        sender.sendMessage(
            "&e$fullCommand\n" +
                    "&7$description"
        )
    }

    val shortHelpFormatter: ((
        sender: CommandSender,
        name: String,
        fullCommand: String,
        shortDescription: String
    ) -> Unit) = { sender, fullCommand, _, shortDescription ->
        sender.sendMessage("&e$fullCommand &8- &7$shortDescription")
    }

    val shortDescription: (sender: CommandSender) -> String = { "No short description provided" }
    val description: (sender: CommandSender) -> String = { "No description provided" }

    val guard: (sender: CommandSender) -> Boolean = { true }

    val guardFailed: (sender: CommandSender) -> Unit = {
        it.sendMessage("You cannot access this command")
    }

    val tabCompleter: ((sender: CommandSender) -> Set<String>) = { emptySet() }
}