package net.tentoria.pass.commands;

import net.minecraft.server.v1_8_R3.CommandExecute;
import net.tentoria.pass.PassPlugin;
import net.tentoria.pass.PlayerHandlers.PlayerSQL;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGetTickets extends CommandExecute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            PlayerSQL psql = PassPlugin.getMainPlugin().playerSQLList.get(player.getUniqueId());

            player.sendMessage(ChatColor.GOLD + "Tickets >> "+ChatColor.YELLOW+"You have "+ChatColor.GOLD+""+ChatColor.BOLD+psql.getTickets()+ChatColor.YELLOW+" tickets!");
        }

        return true;
    }
}
