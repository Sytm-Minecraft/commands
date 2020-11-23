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

import de.md5lukas.commands.generic.GenericCommandOptions
import de.md5lukas.commands.generic.GenericSubCommand
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import org.bukkit.command.Command as BukkitCommand

class UniversalCommand @PublishedApi internal constructor(options: GenericCommandOptions<CommandSender>) :
    GenericSubCommand<CommandSender>(options, options, ""), de.md5lukas.commands.generic.Command {

    override fun onCommand(
        sender: CommandSender,
        command: BukkitCommand,
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
        command: BukkitCommand,
        alias: String,
        args: Array<out String>
    ): MutableList<String> {
        val linkedArgs = LinkedList<String>()
        args.forEach { linkedArgs.add(it) }

        return ArrayList(this.onTabComplete(sender, linkedArgs))
    }

    override fun register(plugin: JavaPlugin) {
        val pc = plugin.getCommand(name)
            ?: throw IllegalStateException("The plugin ${plugin.name} has not a command registered with the name $name")
        pc.setExecutor(this)
        pc.tabCompleter = this
    }
}