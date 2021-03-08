package net.tentoria.pass.oldpackages;

import net.tentoria.pass.PassPlugin;

import java.sql.*;

public class SQLDB {


    private Connection con = null;

    public boolean testDB(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException err){
            err.printStackTrace();
            return false;
        }

        String username = PassPlugin.getMainPlugin().strings.get("sql-user");
        String password = PassPlugin.getMainPlugin().strings.get("sql-pass");
        String ip = PassPlugin.getMainPlugin().strings.get("sql-server");
        String dbname = PassPlugin.getMainPlugin().strings.get("sql-db");


        try {
            con = DriverManager.getConnection("jdbc:mysql://"+ip+"/"+dbname, username, password);
            PassPlugin.getMainPlugin().getLogger().info("Connected!");
        } catch (SQLException err) {
            PassPlugin.getMainPlugin().getLogger().warning("Failure to connect to DB [TestDB]");
            err.printStackTrace();
            return false;
        }

        return true;

    }

    public boolean connectToDB(String dbname){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException err){
            err.printStackTrace();
            return false;
        }

        String username = PassPlugin.getMainPlugin().strings.get("sql-user");
        String password = PassPlugin.getMainPlugin().strings.get("sql-pass");
        String ip = PassPlugin.getMainPlugin().strings.get("sql-server");


        try {
            con = DriverManager.getConnection("jdbc:mysql://"+ip+"/"+dbname, username, password);
            PassPlugin.getMainPlugin().getLogger().info("Connected!");
        } catch (SQLException err) {
            PassPlugin.getMainPlugin().getLogger().warning("Failure to connect to DB");
            return false;
        }

        return true;

    }

    public Connection getCon() {
        return con;
    }
}
