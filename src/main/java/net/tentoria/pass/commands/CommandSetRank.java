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

public class CommandSetRank extends CommandExecute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length < 2){
            if(commandSender instanceof Player) {
                ((Player) commandSender).sendMessage(ChatColor.DARK_RED + "Error >> " + ChatColor.RED + "Please use the correct parameters.");
            }
            return true;
        }

        if(LibFunctions.getPlayer(strings[0]) == null){
            if(commandSender instanceof Player) {
                ((Player) commandSender).sendMessage(ChatColor.DARK_RED + "Error >> " + ChatColor.RED + "Player not found.");
            }
            return true;
        }

        int rankval;

        try {
            rankval = Integer.parseInt(strings[1]);
        } catch (Exception err) {
            if(commandSender instanceof Player) {
                ((Player) commandSender).sendMessage(ChatColor.DARK_RED + "Error >> " + ChatColor.RED + "Please use the correct parameters.");
            } else {
                PassPlugin.getMainPlugin().getLogger().warning("Error > Use correct params");
            }
            return true;
        }

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (PassPlugin.getMainPlugin().playerSQLList.get(player.getUniqueId()).getRank() > 84) {
                if (PassPlugin.getMainPlugin().playerSQLList.get(player.getUniqueId()).getRank() > rankval) {
                    Player player2 = LibFunctions.getPlayer(strings[0]);

                    PlayerSQL psql = PassPlugin.getMainPlugin().playerSQLList.get(player2.getUniqueId());
                    psql.setRank(rankval);

                    player.sendMessage(ChatColor.DARK_AQUA + "Rank >> " + ChatColor.GRAY + "Updated player rank to " + LibFunctions.getRankDisplay(rankval) + "!");
                    player2.sendMessage(ChatColor.DARK_AQUA + "Rank >> " + ChatColor.GRAY + "Your rank was updated to " + LibFunctions.getRankDisplay(rankval) + "!");
                } else {
                    player.sendMessage(ChatColor.DARK_RED + "Permissions >> " + ChatColor.RED + "You cannot give this player a higher or equal rank to you.");
                }
            } else {
                player.sendMessage(ChatColor.DARK_RED + "Permissions >> " + ChatColor.RED + "You do not have permission to use this command.");
            }
        } else {
            Player player2 = LibFunctions.getPlayer(strings[0]);

            PlayerSQL psql = PassPlugin.getMainPlugin().playerSQLList.get(player2.getUniqueId());
            psql.setRank(rankval);

            PassPlugin.getMainPlugin().getLogger().info(ChatColor.DARK_AQUA + "Rank >> " + ChatColor.GRAY + "Updated player rank to " + LibFunctions.getRankDisplay(rankval) + "!");
            player2.sendMessage(ChatColor.DARK_AQUA + "Rank >> " + ChatColor.GRAY + "Your rank was updated to " + LibFunctions.getRankDisplay(rankval) + "!");
        }

        return true;
    }
}
