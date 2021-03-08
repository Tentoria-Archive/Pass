package net.tentoria.pass;

import net.tentoria.pass.PlayerHandlers.PlayerSQL;
import net.tentoria.pass.commands.CommandGetTickets;
import net.tentoria.pass.commands.CommandSetRank;
import net.tentoria.pass.commands.CommandSetTickets;
import net.tentoria.pass.oldpackages.SQLDB;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;

public class PassPlugin extends JavaPlugin{

    private static PassPlugin plugin;

    public LinkedHashMap<UUID, PlayerSQL> playerSQLList;

    public Team lobbyTeam;
    public Scoreboard scoreboards;

    public Map<String, String> strings;

    public boolean DBonline;

    public static PassPlugin getMainPlugin(){
        return plugin;
    }


    @Override
    public void onEnable() {
        plugin = this;
        playerSQLList = new LinkedHashMap<UUID, PlayerSQL>();
        strings = new HashMap<>();

        getCommand("tickets").setExecutor(new CommandGetTickets());
        getCommand("settickets").setExecutor(new CommandSetTickets());
        getCommand("setrank").setExecutor(new CommandSetRank());

        ScoreboardManager scoremanage = Bukkit.getScoreboardManager();
        scoreboards = scoremanage.getNewScoreboard();

        lobbyTeam = scoreboards.registerNewTeam("player");
        lobbyTeam.setAllowFriendlyFire(false);
        lobbyTeam.setCanSeeFriendlyInvisibles(true);
        lobbyTeam.setPrefix(ChatColor.LIGHT_PURPLE.toString());

        EHandler ehandle = new EHandler();
        getServer().getPluginManager().registerEvents(ehandle, this);


        DBonline = true;

        if(new File("sqlcfg.txt").exists()) {
            File file = new File("sqlcfg.txt");
            try {
                Reader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String line = "";

                while((line = bufferedReader.readLine()) != null) {
                    String[] configEntry = line.split("=");
                        strings.put(configEntry[0], configEntry[1]);
                }
                bufferedReader.close();
            } catch (Exception err){
                getLogger().info("DB-LOGIN ERROR [Config]:\n");
                err.printStackTrace();
                DBonline = false;
            }
        } else {
            getLogger().info("DB-LOGIN ERROR [Config Not Found]");
            DBonline = false;
        }

        if(DBonline){
            DBonline = new SQLDB().testDB();
        }
    }

    @Override
    public void onDisable() {
        for (Player player:getServer().getOnlinePlayers()) {
            player.kickPlayer(ChatColor.RED+""+ChatColor.BOLD+"Closing for Maintenance. \nWe will open again soon.");
        }
        Iterator<Map.Entry<UUID, PlayerSQL>> it = playerSQLList.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry<UUID, PlayerSQL> psql = it.next();

            psql.getValue().saveToDB();
        }

    }

    public Scoreboard getScoreboards() {
        return scoreboards;
    }

    public Team getLobbyTeam() {
        return lobbyTeam;
    }

    public LinkedHashMap<UUID, PlayerSQL> getPlayerSQLList() {
        return playerSQLList;
    }
}
