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

import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabExecutor
import org.bukkit.plugin.java.JavaPlugin

/**
 * A executable command is a command that can be registered in a plugin
 */
interface ExecutableCommand : CommandExecutor, TabExecutor, Command {

    /**
     * Helper function that registers this command as the command and tab executor
     *
     * @param plugin The plugin to register the command for
     * @throws IllegalStateException If the plugin has command registered with the [name]
     */
    fun register(plugin: JavaPlugin)
}