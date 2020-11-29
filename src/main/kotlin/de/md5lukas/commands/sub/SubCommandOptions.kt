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

package de.md5lukas.commands.sub

import de.md5lukas.commands.CommandDsl
import de.md5lukas.commands.LambdaSingleton
import org.bukkit.command.CommandSender

@CommandDsl
open class SubCommandOptions<T : CommandSender> @PublishedApi internal constructor(val name: String) {

    init {
        if (' ' in name) {
            throw IllegalArgumentException("Command names may not contain spaces (\"$name\")")
        }
    }

    @PublishedApi
    internal val subCommands: MutableList<SubCommandOptions<T>> = ArrayList()

    /**
     * Adds a subcommand to this command
     *
     * @param name The name of the subcommand
     * @param init The initializer for the options
     */
    inline fun subcommand(name: String, init: SubCommandOptions<T>.() -> Unit) {
        subCommands.add(SubCommandOptions<T>(name).apply(init))
    }

    /**
     * Aliases for the command
     *
     * @throws IllegalArgumentException If any of the aliases contains a space
     */
    var aliases: Set<String> = emptySet()
        set(value) {
            value.forEach {
                if (' ' in it) {
                    throw IllegalArgumentException("Command name aliases may not contain spaces (\"$it\")")
                }
            }
            field = value
        }

    /**
     * Callback function that is used to get the short description of a command.
     * The sender is provided for i18n purposes
     */
    var shortDescription: (sender: T) -> String = LambdaSingleton.shortDescription

    /**
     * Callback function that is used to get the full description of a command.
     * The sender is provided for i18n purposes
     */
    var description: (sender: T) -> String = LambdaSingleton.description

    /**
     * Guard callback that gets called every time someone tries to execute/tab-complete this command.
     * Return `true` grant access, `false` to deny it.
     *
     * You should not send the sender any messages here, because he will receive them too if he tried to tab-complete
     * this command. For that there is [guardFailed] that gets called only on attempted command execution to send
     * the command issuer a message for example
     */
    var guard: (sender: T) -> Boolean = LambdaSingleton.guard

    /**
     * If the only purpose of a guard is to check for a permission, this can be used instead.
     */
    var permissionGuard: String? = null

    /**
     * Handler that gets called when someone called a command (not tab-completed) and failed the guard.
     *
     * On the root command options it cannot be set to a null value
     *
     * @throws IllegalArgumentException If this is the root options and the new value is `null`
     */
    open var guardFailed: ((sender: T) -> Unit)? = null

    /**
     * This handler function is called if the command has no more additional arguments passed to it
     */
    var run: ((sender: T) -> Unit)? = null

    /**
     * This handler function is called if no more subcommands are left or no subcommand matches the provided arguments.
     */
    var runArgs: ((sender: T, args: List<String>) -> Unit)? = null

    /**
     * This method can return additional elements that should be added to the tab completions
     */
    var tabCompleter: ((sender: T) -> Set<String>) = LambdaSingleton.tabCompleter
}