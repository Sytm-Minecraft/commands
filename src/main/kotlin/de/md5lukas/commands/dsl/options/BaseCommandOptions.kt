package de.md5lukas.commands.dsl.options

import org.bukkit.command.CommandSender

class BaseCommandOptions(name: String) : SubCommandOptions(name) {

    var notFoundMessage: ((sender: CommandSender, fullCommand: String) -> Unit) = { _, _ ->
        TODO("Not found message not implemented yet")
    }

    var helpListingsPerPage: Int = 10

    var helpFormatter: ((
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

    var shortHelpFormatter: ((
        sender: CommandSender,
        name: String,
        fullCommand: String,
        shortDescription: String
    ) -> Unit) = { sender, fullCommand, _, shortDescription ->
        sender.sendMessage("&e$fullCommand &8- &7$shortDescription")
    }
}