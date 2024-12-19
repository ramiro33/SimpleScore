package com.r4g3baby.hyperboard.commands

import com.r4g3baby.hyperboard.commands.subcmds.Reload
import com.r4g3baby.hyperboard.commands.subcmds.Help
import com.r4g3baby.hyperboard.commands.subcmds.Toggle
import com.r4g3baby.hyperboard.commands.subcmds.Version
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class MainCmd : CommandExecutor, TabExecutor {
    // Lista de subcomandos que manejan los diferentes comandos de HyperBoard
    private val subCmds = listOf(
        Reload(), Help(), Toggle(), Version()  // Reemplaza o agrega subcomandos aquí
    )

    // Maneja el comando principal /hb y los subcomandos
    override fun onCommand(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        // Si hay argumentos, intenta encontrar el subcomando
        if (args.isNotEmpty()) {
            for (subCmd in subCmds) {
                if (subCmd.name.equals(args[0], true)) {
                    if (sender.hasPermission(subCmd.permission)) {
                        // Ejecuta el subcomando si el jugador tiene el permiso correspondiente
                        subCmd.run(sender, args.sliceArray(1..args.lastIndex))
                    } else {
                        // Muestra mensaje de error si el jugador no tiene permisos
                        sender.sendMessage("§cNo tienes permiso para este comando.")
                    }
                    return true
                }
            }

            // Si el subcomando no se encuentra, muestra mensaje de error o ayuda
            if (!args[0].equals("help", true)) {
                sender.sendMessage("§cComando no encontrado.")
            } else {
                showHelp(sender)
            }
        } else {
            showHelp(sender)
        }

        return true
    }

    // Maneja la autocompletación de comandos
    override fun onTabComplete(sender: CommandSender, cmd: Command, label: String, args: Array<out String>): List<String> {
        return when {
            args.size == 1 -> subCmds.filter {
                it.name.startsWith(args[0], true) && sender.hasPermission(it.permission)
            }.map { it.name }
            args.size > 1 -> subCmds.find {
                it.name.equals(args[0], true) && sender.hasPermission(it.permission)
            }?.onTabComplete(sender, args.sliceArray(1..args.lastIndex)) ?: emptyList()
            else -> emptyList()
        }
    }

    // Muestra la ayuda de los subcomandos disponibles
    private fun showHelp(sender: CommandSender) {
        val commands = subCmds.filter { sender.hasPermission(it.permission) }
        if (commands.isNotEmpty()) {
            val builder = StringBuilder("§aComandos disponibles:")
            commands.forEach { cmd ->
                builder.appendLine().append("§6/${cmd.name} - ${cmd.description}")
            }
            sender.sendMessage(builder.toString())
        } else {
            sender.sendMessage("§cNo tienes permisos para ningún comando.")
        }
    }
}
