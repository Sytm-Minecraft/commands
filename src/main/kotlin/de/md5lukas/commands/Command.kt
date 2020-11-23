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

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class Command @PublishedApi internal constructor(options: CommandOptions) : SubCommand(options, options, ""), CommandExecutor, TabCompleter {

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