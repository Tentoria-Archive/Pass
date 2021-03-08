package net.tentoria.pass.commands;

import net.minecraft.server.v1_8_R3.CommandExecute;
import net.tentoria.pass.LibFunctions;
import net.tentoria.pass.PassPlugin;
import net.tentoria.pass.PlayerHandlers.PlayerSQL;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetTickets extends CommandExecute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length < 2){
            if(commandSender instanceof Player) {
                ((Player) commandSender).sendMessage(ChatColor.DARK_RED + "Error >> " + ChatColor.RED + "Please use the correct parameters.");
            }
            return true;
        }

        if(LibFunctions.getPlayer(strings[0]) == null){
            ((Player) commandSender).sendMessage(ChatColor.DARK_RED + "Error >> " + ChatColor.RED + "Player not found. Are they online? Contact a dev to manually edit the value.");
            return true;
        }

        int tickets;

        try {
            tickets = Integer.parseInt(strings[1]);
        } catch (Exception err) {
            if(commandSender instanceof Player) {
                ((Player) commandSender).sendMessage(ChatColor.DARK_RED + "Error >> " + ChatColor.RED + "Please use the correct parameters.");
            } else {
                PassPlugin.getMainPlugin().getLogger().warning("Error > Use correct params");
            }
            return true;
        }

        if(commandSender instanceof Player) {
                Player player = (Player) commandSender;
                if(PassPlugin.getMainPlugin().playerSQLList.get(player.getUniqueId()).getRank() > 89) {
                    Player player2 = LibFunctions.getPlayer(strings[0]);

                    PlayerSQL psql = PassPlugin.getMainPlugin().playerSQLList.get(player2.getUniqueId());

                    psql.setTickets(tickets);

                    player.sendMessage(ChatColor.GOLD + "Tickets >> " + ChatColor.YELLOW + "Updated ticket count.");
                    player2.sendMessage(ChatColor.GOLD + "Tickets >> " + ChatColor.YELLOW + "An Admin has updated your ticket count.");
                }
        } else {
                Player player2 = LibFunctions.getPlayer(strings[0]);

                PlayerSQL psql = PassPlugin.getMainPlugin().playerSQLList.get(player2.getUniqueId());

                psql.setTickets(tickets);

                PassPlugin.getMainPlugin().getLogger().info(ChatColor.GOLD + "Tickets >> " + ChatColor.YELLOW + "Updated ticket count.");
                player2.sendMessage(ChatColor.GOLD + "Tickets >> " + ChatColor.YELLOW + "An Admin has updated your ticket count.");
        }

        return true;
    }
}
