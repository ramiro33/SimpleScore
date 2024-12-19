package com.r4g3baby.hyperboard.commands.subcmds

import com.r4g3baby.hyperboard.HyperBoard
import com.r4g3baby.hyperboard.commands.SubCmd
import org.bukkit.command.CommandSender

class Reload : SubCmd("reload") {
    override fun run(sender: CommandSender, args: Array<out String>) {
        sender.sendMessage("§aRecargando la configuración de HyperBoard...")  // Mensaje de inicio
        HyperBoard.reload()  // Llamada al método de recarga de HyperBoard
        sender.sendMessage("§aRecarga completada.")  // Mensaje de finalización
    }
}
