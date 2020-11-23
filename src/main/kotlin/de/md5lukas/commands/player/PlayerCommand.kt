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

package de.md5lukas.commands.player

import de.md5lukas.commands.ExecutableCommand
import de.md5lukas.commands.sub.SubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import org.bukkit.command.Command as BukkitCommand

class PlayerCommand @PublishedApi internal constructor(private val options: PlayerCommandOptions) :
    SubCommand<Player>(options, options, ""), ExecutableCommand {

    override fun onCommand(
        sender: CommandSender,
        command: BukkitCommand,
        label: String,
        args: Array<out String>
    ): Boolean {
        val linkedArgs = LinkedList<String>()
        args.forEach { linkedArgs.add(it) }

        if (sender is Player) {
            this.onCommand(sender, linkedArgs, "")
        } else {
            options.notAPlayerMessage(sender)
        }

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

        return if (sender is Player) ArrayList(this.onTabComplete(sender, linkedArgs)) else mutableListOf()
    }

    override fun register(plugin: JavaPlugin) {
        val pc = plugin.getCommand(name)
            ?: throw IllegalStateException("The plugin ${plugin.name} has not a command registered with the name $name")
        pc.setExecutor(this)
        pc.tabCompleter = this
    }
}