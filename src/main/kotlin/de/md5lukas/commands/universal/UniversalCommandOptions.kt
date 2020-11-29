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

package de.md5lukas.commands.universal

import de.md5lukas.commands.Command
import de.md5lukas.commands.LambdaSingleton
import de.md5lukas.commands.sub.SubCommandOptions
import org.bukkit.command.CommandSender

open class UniversalCommandOptions<T : CommandSender> @PublishedApi internal constructor(name: String) :
    SubCommandOptions<T>(name) {

    init {
        super.guardFailed = LambdaSingleton.guardFailed
    }

    override var guardFailed: ((sender: T) -> Unit)?
        get() = super.guardFailed
        set(value) {
            super.guardFailed = value
                ?: throw IllegalArgumentException("Cannot set a null value for guardFailed in the root options")
        }

    /**
     * This callback gets called if a player tried to call a command that has not been registered and no [runArgs]
     * handler has been registered for that command
     */
    var notFoundMessage: ((
        /**
         * The sender who issued the command
         */
        sender: T,
        /**
         * The full command that the sender has entered
         */
        fullCommand: String
    ) -> Unit) = LambdaSingleton.notFoundMessage

    /**
     * The amount of help listings per page
     *
     * @throws IllegalArgumentException If the new value is not 1 or greater
     */
    var helpListingsPerPage: Int = 10
        set(value) {
            if (value >= 1) {
                field = value
            } else {
                throw IllegalArgumentException("The value for helpListingsPerPage must be 1 or greater")
            }
        }

    /**
     * Callback that gets called before the rest of the current help page is printed.
     * Can be used to show the current page
     */
    var helpHeader: ((
        /**
         * The sender who issued the command
         */
        sender: T,
        /**
         * The current command that the help menu has been requested from
         */
        command: Command,
        /**
         * The current page the sender is viewing.
         *
         * Starts at 1
         */
        currentPage: Int,
        /**
         * The maximum amount of help pages available.
         *
         * Starts at 1
         */
        allPages: Int,
    ) -> Unit) = LambdaSingleton.helpHeader

    /**
     * Formatter for the help entry of a command to show the full description
     */
    var commandHelpFormatter: ((
        /**
         * The sender who issued the command
         */
        sender: T,
        /**
         * The current command the player is viewing
         */
        command: Command,
        /**
         * The full description retrieved via [description]
         */
        description: String
    ) -> Unit) = LambdaSingleton.commandHelpFormatter

    /**
     * Formatter for the help entry of a command to show the short description
     */
    var commandShortHelpFormatter: ((
        /**
         * The sender who issued the command
         */
        sender: T,
        /**
         * The current command the player is viewing
         */
        command: Command,
        /**
         * The short description retrieved via [shortDescription]
         */
        shortDescription: String
    ) -> Unit) = LambdaSingleton.commandShortHelpFormatter
}