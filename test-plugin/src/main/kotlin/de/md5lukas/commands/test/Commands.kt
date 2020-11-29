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

package de.md5lukas.commands.test

import de.md5lukas.commands.command
import de.md5lukas.commands.playerCommand

object Commands {
    val test = command("test") {
        run = {
            it.sendMessage("You called the root run")
        }
        runArgs = { sender, args ->
            sender.sendMessage("You called the root run with remaining arguments: ${args.joinToString()}")
        }
        description = { "This is a very long and exhaustive description of this command" }
        val nums = (0..10).map(Int::toString).toSet()
        tabCompleter = { nums }

        subcommand("sub") {
            description = { "This is a very long and exhaustive description of this sub command" }
            shortDescription = { "This is a short description of the sub command" }
        }
    }

    val playerTest = playerCommand("player-test") {
        subcommand("health") {
            run = {
                it.sendMessage("Your health is ${it.health}")
            }
            runArgs = { player, args ->
                val newHealth = args[0].toDoubleOrNull()
                if (newHealth == null) {
                    player.sendMessage("You need to provide a valid amount of health")
                } else {
                    player.health = newHealth
                }
            }
            val nums = (0..20 step 5).map(Int::toString).toSet()
            tabCompleter = { nums }

            shortDescription = { "View or change your current health" }
            description = {
                "Use this command without any additional arguments to view your current health or add " +
                        "a number to the end to update your current health"
            }
        }
        subcommand("food") {
            run = {
                it.sendMessage("Your food level is ${it.foodLevel}")
            }
            runArgs = { player, args ->
                val newFoodLevel = args[0].toIntOrNull()
                if (newFoodLevel == null) {
                    player.sendMessage("You need to provide a valid amount for your new food level")
                } else {
                    player.foodLevel = newFoodLevel
                }
            }
            val nums = (0..20 step 5).map(Int::toString).toSet()
            tabCompleter = { nums }

            shortDescription = { "View or change your current food level" }
            description = {
                "Use this command without any additional arguments to view your current food level or add " +
                        "a number to the end to update your current food level"
            }
        }
        subcommand("fly") {
            run = {
                it.allowFlight = !it.allowFlight
                if (it.allowFlight) {
                    it.sendMessage("You can now fly")
                } else {
                    it.sendMessage("You can no longer fly")
                }
            }

            shortDescription = { "Toggle your ability to fly" }
            description = { "Use this command to toggle your ability to fly on and off" }
        }
    }
}