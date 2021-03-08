package net.tentoria.pass.PlayerHandlers;

import net.tentoria.pass.LibFunctions;
import net.tentoria.pass.PassPlugin;
import net.tentoria.pass.oldpackages.SQLDB;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerSQL {

    private String u;
    private String name;
    private int r;
    private int t;
    private int c;

    private Scoreboard scoreb;

    public PlayerSQL(String uuid, int rank, int tickets, int coins){

        u = uuid;
        r = rank;
        t = tickets;
        c = coins;

        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        name = player.getName();

        scoreb = Bukkit.getScoreboardManager().getNewScoreboard();

    }

    public boolean overrideFromDB(){
        SQLDB sql = new SQLDB();

        boolean connected = sql.connectToDB(PassPlugin.getMainPlugin().strings.get("sql-db"));
        Connection con = sql.getCon();

        ResultSet results;

        try {

            if(!connected){
                PassPlugin.getMainPlugin().getLogger().info("1");
                con.close();
                return false;
            }

            PassPlugin.getMainPlugin().getLogger().info("2");

            Statement statement = con.createStatement();
            String sqlstr = "SELECT * FROM userstats WHERE uuid='"+u+"';";
            results = statement.executeQuery(sqlstr);

            PassPlugin.getMainPlugin().getLogger().info("3");

            if(results == null) {
                PassPlugin.getMainPlugin().getLogger().info("4");
                saveToDB();
                updateScoreboard();
                PassPlugin.getMainPlugin().getLogger().info("5");
                return true;
            }

            int resulttest = 0;

            if(results.next()){
                resulttest++;
            }
            if(resulttest < 1){
                PassPlugin.getMainPlugin().getLogger().info("6");
                saveToDB();
                updateScoreboard();
                PassPlugin.getMainPlugin().getLogger().info("7");
                return true;
            }

            PassPlugin.getMainPlugin().getLogger().info("8");
            t = results.getInt("tickets");
            r = results.getInt("rank");
            c = results.getInt("coins");
            PassPlugin.getMainPlugin().getLogger().info("9");

            con.close();

        } catch (Exception err){
            PassPlugin.getMainPlugin().getLogger().info("10");
            err.printStackTrace();
            return false;
        }

        PassPlugin.getMainPlugin().getLogger().info("11");
        return true;
    }

    public boolean checkFromDB(){
        SQLDB sql = new SQLDB();

        boolean connected = sql.connectToDB(PassPlugin.getMainPlugin().strings.get("sql-db"));
        Connection con = sql.getCon();

        ResultSet results;

        try {
            if(!connected){
                con.close();
                return false;
            }

            Statement statement = con.createStatement();
            String sqlstr = "SELECT * FROM userstats WHERE uuid='"+u+"';";
            results = statement.executeQuery(sqlstr);

            if(results == null) {

                return false;
            }

            int resulttest = 0;

            if(results.next()){
                resulttest++;
            }
            if(resulttest < 1){
                updateScoreboard();
                return false;
            }


            con.close();

        } catch (Exception err){
            err.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean saveToDB(){

        SQLDB sql = new SQLDB();

        boolean connected = sql.connectToDB(PassPlugin.getMainPlugin().strings.get("sql-db"));
        Connection con = sql.getCon();

        if(checkFromDB()){

            try {
                if(!connected){
                    PassPlugin.getMainPlugin().getLogger().warning("saveToDB isn't connecting.");
                    con.close();
                    return false;
                }
                PassPlugin.getMainPlugin().getLogger().info("Updating DB");
                Statement statement = con.createStatement();

                String sqltickets = "UPDATE userstats SET tickets='"+t+"' WHERE uuid='"+u+"';";
                statement.execute(sqltickets);
                String sqlranks = "UPDATE userstats SET rank='"+r+"' WHERE uuid='"+u+"';";
                statement.execute(sqlranks);
                String sqlcoins = "UPDATE userstats SET coins='"+c+"' WHERE uuid='"+u+"';";
                statement.execute(sqlcoins);
                con.close();
                PassPlugin.getMainPlugin().getLogger().info("Updated DB");



            } catch (Exception err){
                err.printStackTrace();
                return false;
            }
        } else {
            try {
                if(!connected){
                    PassPlugin.getMainPlugin().getLogger().warning("saveToDB isn't connecting.");
                    con.close();
                    return false;
                }
                //Process Data
                PassPlugin.getMainPlugin().getLogger().info("Inserting into DB");
                Statement statement = con.createStatement();

                String sqltickets = "INSERT INTO userstats (uuid, tickets, rank, coins) VALUES ('"+u+"', "+t+", "+r+", "+c+");";
                statement.execute(sqltickets);
                con.close();
                PassPlugin.getMainPlugin().getLogger().info("Inserted into DB");


            } catch (Exception err){
                err.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public void updateScoreboard(){


        Player player = PassPlugin.getMainPlugin().getServer().getPlayer(UUID.fromString(u));
        List<UUID> list = new ArrayList<UUID>(PassPlugin.getMainPlugin().playerSQLList.keySet());
            player.setScoreboard(scoreb);


            Objective objective = scoreb.registerNewObjective(scoreb.getObjectives().size()+"#1", "dummy");

            objective.setDisplayName(ChatColor.WHITE + "" + ChatColor.BOLD + "= " + ChatColor.RED + "" + ChatColor.BOLD + "TENTORIA" + ChatColor.WHITE + "" + ChatColor.BOLD + " =");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);

            Score blank = objective.getScore(ChatColor.RESET.toString());
            blank.setScore(15);
            Score tickets = objective.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Tickets: " + ChatColor.YELLOW + String.valueOf(t));
            tickets.setScore(14);
            Score rank = objective.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Rank: " + ChatColor.YELLOW + LibFunctions.getRankDisplay(r));
            rank.setScore(13);
            blank = objective.getScore(ChatColor.RESET.toString()+ChatColor.RESET.toString());
            blank.setScore(12);
            blank = objective.getScore(ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString());
            blank.setScore(11);
            blank = objective.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Server: " + ChatColor.YELLOW + player.getWorld().getName());
            blank.setScore(10);
            blank = objective.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "Version: " + ChatColor.YELLOW + "Alpha 1.0");
            blank.setScore(9);
            blank = objective.getScore(ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString());
            blank.setScore(8);
            blank = objective.getScore(ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString());
            blank.setScore(7);
            blank = objective.getScore(ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString());
            blank.setScore(6);
            blank = objective.getScore(ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString());
            blank.setScore(5);
            blank = objective.getScore(ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString());
            blank.setScore(4);
            blank = objective.getScore(ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString()+ChatColor.RESET.toString());
            blank.setScore(3);
            blank = objective.getScore(ChatColor.GOLD + "" + ChatColor.BOLD + "You are playing on:");
            blank.setScore(2);
            blank = objective.getScore(ChatColor.YELLOW + "" + ChatColor.MAGIC +  "play.tentoria.net");
            blank.setScore(1);

            Objective ranktag = scoreb.registerNewObjective(scoreb.getObjectives().size()+"#2", "dummy");
            ranktag.setDisplayName(ChatColor.GOLD + "Tickets");
            ranktag.setDisplaySlot(DisplaySlot.BELOW_NAME);

            player.setPlayerListName(LibFunctions.getRankDisplay(r) + "> "+ChatColor.WHITE + player.getName());

    }

    public int getRank() {
        return r;
    }

    public void setRank(int rank) {
        this.r = rank;
        updateScoreboard();
    }

    public int getTickets() {
        return t;
    }

    public void setTickets(int tickets) {
        this.t = tickets;
        updateScoreboard();
    }

    public void addTickets(int tickets) {
        this.t = this.t + tickets;
        updateScoreboard();
    }

    public void removeTickets(int tickets) {
        this.t = this.t - tickets;
        updateScoreboard();
    }

    public int getCoins() {
        return c;
    }

    public void setCoins(int coins) {
        this.c = coins;
        updateScoreboard();
    }

    public void addCoins(int coins) {
        this.c = coins + this.c;
        updateScoreboard();
    }

    public void removeCoins(int coins) {
        this.c = this.c - coins;
        updateScoreboard();
    }

    public String getUUID() {
        return u;
    }

    public Scoreboard getScoreb() {
        return scoreb;
    }
}
