package net.tentoria.pass;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class LibFunctions {

    private static Map<Integer, String> ranks;

    static {
        ranks = new HashMap<>();

        ranks.put(10, ChatColor.DARK_GRAY+ "" + ChatColor.BOLD + "MEMBER");
        ranks.put(20, ChatColor.DARK_RED + "" + ChatColor.BOLD + "BRONZE");
        ranks.put(25, ChatColor.GRAY + "" + ChatColor.BOLD + "SILVER");
        ranks.put(30, ChatColor.GOLD + "" + ChatColor.BOLD + "GOLD");
        ranks.put(40, ChatColor.AQUA + "" + ChatColor.BOLD + "PREMIUM");
        ranks.put(50, ChatColor.RED + "" + ChatColor.BOLD + "YOUTUBE");
        ranks.put(55, ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "BUILDER");
        ranks.put(60, ChatColor.GOLD + "" + ChatColor.BOLD + "TRIAL MOD");
        ranks.put(62, ChatColor.GOLD + "" + ChatColor.BOLD + "MOD");
        ranks.put(64, ChatColor.AQUA + "" + ChatColor.BOLD + "PREMIUM"); //Hidden Mod
        ranks.put(65, ChatColor.GOLD + "" + ChatColor.BOLD + "SR.MOD");
        ranks.put(85, ChatColor.RED + "" + ChatColor.BOLD + "MANAGER"); //Leads a team. Build Lead is here as well.
        ranks.put(90, ChatColor.DARK_RED + "" + ChatColor.BOLD + "ADMIN");
        ranks.put(95, ChatColor.DARK_RED + "" + ChatColor.BOLD + "NET ADMIN");
        ranks.put(99, ChatColor.GREEN + "" + ChatColor.BOLD + "OWNER");
    }


    public static Player getPlayer(String name){
        for(Player player: PassPlugin.getMainPlugin().getServer().getOnlinePlayers()){
            if(player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }

    public static String getRankDisplay(int id){

        if(ranks.containsKey(id)){
            return ranks.get(id);
        } else {
            return ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "???";
        }

    }

}
