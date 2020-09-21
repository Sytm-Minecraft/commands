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