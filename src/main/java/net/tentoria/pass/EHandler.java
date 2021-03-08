package net.tentoria.pass;

import net.tentoria.pass.PlayerHandlers.PlayerSQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import org.bukkit.event.EventHandler;
import org.bukkit.scoreboard.Objective;

import java.util.UUID;

public class EHandler implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        event.setCancelled(true);

        String message = "";

        message = message.concat(LibFunctions.getRankDisplay(PassPlugin.getMainPlugin().playerSQLList.get(event.getPlayer().getUniqueId()).getRank()) + "> " + ChatColor.DARK_GRAY + event.getPlayer().getName()+"> " + ChatColor.GRAY + event.getMessage());

        for(Player player: event.getPlayer().getWorld().getPlayers()){
            if(event.getMessage().toLowerCase().contains("@"+player.getName().toLowerCase())){
                player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2f, 1.2f);
            }
            player.sendMessage(message);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        event.setJoinMessage("");
        PassPlugin.getMainPlugin().lobbyTeam.addEntry(event.getPlayer().getName());

        PlayerSQL psql = new PlayerSQL(event.getPlayer().getUniqueId().toString(), 10, 100, 0);
        PassPlugin.getMainPlugin().playerSQLList.put(event.getPlayer().getUniqueId(), psql);
        psql.updateScoreboard();

        if(!(PassPlugin.getMainPlugin().DBonline)) {
            event.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "==========================================\n" + ChatColor.GOLD + "Hi! The database is currently offline so yikes\nYour tickets and rank will not be loaded\nuntil it's back up. Sorry!\n" + ChatColor.RED + "" + ChatColor.BOLD + "==========================================");
        } else {
            event.getPlayer().sendMessage(ChatColor.RED + "Stats >> " + ChatColor.GRAY + "Loading your profile...");
            boolean load = psql.overrideFromDB();
            if(load){
                event.getPlayer().sendMessage(ChatColor.RED + "Stats >> " + ChatColor.GRAY + "Loaded your profile.");
            } else {
                boolean save = psql.saveToDB();
                if(save) {
                    event.getPlayer().sendMessage(ChatColor.RED + "Stats >> " + ChatColor.GRAY + "Created your profile.");
                } else {
                    event.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "==========================================\n" + ChatColor.GOLD + "Hi! There was an issue loading your profile.\nYour tickets and rank will not be loaded\nuntil it's back up. Sorry!\n" + ChatColor.RED + "" + ChatColor.BOLD + "==========================================");
                }
            }
        }

        psql.updateScoreboard();

    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        event.setQuitMessage("");
        PlayerSQL playerSQL = PassPlugin.getMainPlugin().playerSQLList.get(event.getPlayer().getUniqueId());
        playerSQL.saveToDB();
        PassPlugin.getMainPlugin().playerSQLList.remove(UUID.fromString(playerSQL.getUUID()));
        for(Objective objective:playerSQL.getScoreb().getObjectives()) {
            objective.unregister();
        }
    }
    @EventHandler
    public void onKick(PlayerKickEvent event){
        event.setLeaveMessage("");
        PlayerSQL playerSQL = PassPlugin.getMainPlugin().playerSQLList.get(event.getPlayer().getUniqueId());
        playerSQL.saveToDB();
        PassPlugin.getMainPlugin().playerSQLList.remove(UUID.fromString(playerSQL.getUUID()));
        for(Objective objective:playerSQL.getScoreb().getObjectives()) {
            objective.unregister();
        }
    }

}
