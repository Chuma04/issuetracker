package com.chumamhango.issuetracker.DAL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Site {
    private int site_ID;
    private String site;
    private String location;

    public int getSite_ID() {
        return site_ID;
    }

    public void setSite_ID(int site_ID) {
        this.site_ID = site_ID;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    public static List<Site> getSites() {
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();
        Site site;

        List<Site> sites = new ArrayList<>();

        try{
            String query = "SELECT * FROM site";
            Statement st = conn.createStatement();
            ResultSet result = st.executeQuery(query);

            while(result.next()){
                site = new Site();

                site.setSite_ID(result.getInt("site_ID"));
                site.setSite(result.getString("description"));
                site.setLocation(result.getString("location"));

                sites.add(site);
            }
        }
        catch (SQLException e){

        }
        finally {
            if(conn != null)
                newConnect.disconnect(conn);
        }

        return sites;
        }
    }
