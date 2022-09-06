package com.chumamhango.issuetracker.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    final private String username = "root"; // username for the server
    final private String connPwd = ""; // your SQL server password
    final private String url = ""; // your database url. e.g. 'jdbc:mysql://localhost:3306/issuetrackerdb' for íssutrackerdb' database on SQL local server running on port 3306
    final private String driver = "com.mysql.cj.jdbc.Driver"; // your database driver. Make sure to add the MYSQL connector JAR file or the MYSQL jdbc dependency to your project

    protected Connection connect() {

        try{
            Class.forName(driver);
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        try {
            return DriverManager.getConnection(url, username, connPwd);
        }
        catch (SQLException e){
            return null;
        }
    }

    protected void disconnect(Connection conn){
        try {
            conn.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
