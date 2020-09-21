package de.md5lukas.commands.dsl

import de.md5lukas.commands.BaseCommand
import de.md5lukas.commands.dsl.options.BaseCommandOptions

inline fun command(name: String, init: BaseCommandOptions.() -> Unit): BaseCommand {
    return BaseCommand(BaseCommandOptions(name).apply(init))
}