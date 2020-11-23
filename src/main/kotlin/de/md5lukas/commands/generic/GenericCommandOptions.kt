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

package de.md5lukas.commands.generic

import de.md5lukas.commands.LambdaSingleton
import org.bukkit.command.CommandSender

open class GenericCommandOptions<T : CommandSender> @PublishedApi internal constructor(name: String) :
    GenericSubCommandOptions<T>(name) {

    init {
        super.guardFailed = LambdaSingleton.guardFailed
    }

    override var guardFailed: ((sender: T) -> Unit)?
        get() = super.guardFailed
        set(value) {
            @Suppress("SetterBackingFieldAssignment")
            super.guardFailed = value
                ?: throw IllegalArgumentException("Cannot set a null value for guardFailed in the root options")
        }

    var notFoundMessage: ((sender: T, fullCommand: String) -> Unit) = LambdaSingleton.notFoundMessage

    var helpListingsPerPage: Int = 10

    var helpFormatter: ((
        sender: T,
        name: String,
        fullCommand: String,
        description: String
    ) -> Unit) = LambdaSingleton.helpFormatter

    var shortHelpFormatter: ((
        sender: T,
        name: String,
        fullCommand: String,
        shortDescription: String
    ) -> Unit) = LambdaSingleton.shortHelpFormatter
}