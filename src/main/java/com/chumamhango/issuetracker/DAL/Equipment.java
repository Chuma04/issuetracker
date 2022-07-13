package com.chumamhango.issuetracker.DAL;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Equipment {

    private int equipment_ID;
    private int inspector_ID;
    private String inspector;
    private String name;
    private String site;

    private String supervisor;

    private String statusType;
    private int site_ID;
    private String description;
    private int statusId;
    private String status;
    private String date_inspected;
    private String comment;


    public String getSite() {
        return site;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public int getEquipment_ID() {
        return equipment_ID;
    }

    public void setEquipment_ID(int equipment_ID) {
        this.equipment_ID = equipment_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public int getSite_ID() {
        return site_ID;
    }
    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public void setSite_ID(int site_ID) {
        this.site_ID = site_ID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInspector_ID() {
        return inspector_ID;
    }

    public void setInspector_ID(int inspector_ID) {
        this.inspector_ID = inspector_ID;
    }

    public String getDate_inspected() {
        return date_inspected;
    }
    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public void setDate_inspected(String date_inspected) {
        this.date_inspected = date_inspected;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public static List<Equipment> getEquipmentsById(int id) {
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();
        Equipment equipment;
        List<Equipment> equipments = new ArrayList<Equipment>();

        try{
            String query = "SELECT * FROM equipment e JOIN site s on e.site_ID = s.site_ID" +
                    " JOIN status s2 on e.status_ID = s2.status_ID WHERE E.inspector_ID = " + id;
            Statement st = conn.createStatement();
            ResultSet result = st.executeQuery(query);

            while(result.next()){
                equipment = new Equipment();

                String supId = result.getString("supervisor_ID");

                if(supId != null){
                    String supQuery2 = "SELECT fname, lname from employee where employee_ID = " + Integer.parseInt(supId);
                    Statement supSt2 = conn.createStatement();
                    ResultSet supResult2 = supSt2.executeQuery(supQuery2);
                    supResult2.next();
                    equipment.setSupervisor(supResult2.getString("fname") + " " + supResult2.getString("lname"));
                }
                else{
                    equipment.setSupervisor("");
                }

                equipment.setEquipment_ID(result.getInt("Equipment_ID"));
                equipment.setInspector_ID(result.getInt("inspector_ID"));
                equipment.setName(result.getString("name"));
                equipment.setSite(result.getString("description"));
                equipment.setStatus(result.getString("state"));
                equipment.setStatusType(result.getString("status_type"));
                equipment.setDate_inspected(result.getString("date_inspected"));
                equipment.setComment(result.getString("comment"));

                equipments.add(equipment);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            if(conn != null)
                newConnect.disconnect(conn);
        }

        return equipments;
    }

    public static List<Equipment> getEquipments() {
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();
        Equipment equipment;

        List<Equipment> equipments = new ArrayList<>();

        try{
            String query = "SELECT * FROM equipment eq JOIN site s ON eq.site_ID = s.site_ID " +
                    "JOIN employee em ON eq.inspector_ID = em.employee_ID JOIN status st on eq.status_ID = st.status_ID ORDER BY eq.equipment_ID ASC;";
            Statement st = conn.createStatement();
            ResultSet result = st.executeQuery(query);

            while(result.next()){
                equipment = new Equipment();

                equipment.setEquipment_ID(result.getInt("Equipment_ID"));
                equipment.setInspector_ID(result.getInt("inspector_ID"));
                equipment.setName(result.getString("name"));
                equipment.setSite(result.getString("description"));
                equipment.setDescription(result.getString("equipment_desc"));
                equipment.setStatus(result.getString("state"));
                equipment.setInspector(result.getString("fname") + " " + result.getString("lname"));
                equipment.setDate_inspected(result.getString("date_inspected"));
                equipment.setStatusType(result.getString("status_type"));
                equipment.setStatusId(result.getInt("status_ID"));
                equipment.setComment(result.getString("comment"));

                equipments.add(equipment);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            if(conn != null)
                newConnect.disconnect(conn);
        }

        return equipments;
    }

    public static void addNewEquipment(String name, String description, int siteId, int inspectorId) {
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        try{
            String insStatement = "INSERT INTO equipment (name, equipment_desc, site_ID, inspector_ID, status_ID) VALUES (?,?,?,?,?);";
            PreparedStatement st = conn.prepareStatement(insStatement);

            st.setString(1, name);
            st.setString(2, description);
            st.setInt(3, siteId);
            st.setInt(4, inspectorId);
            st.setInt(5, 1);

            st.executeUpdate();
        }
        catch (SQLException e){

        }
        finally{
            if(conn != null)
                newConnect.disconnect(conn);
        }
    }

    public static Equipment getEquipment(int equipmentId) {
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        Equipment equipment = new Equipment();

        try {
            String query = "SELECT * FROM equipment e Join site s ON e.site_ID = s.site_ID JOIN status st " +
                    "ON e.status_ID = st.status_ID JOIN employee em on em.employee_ID = e.inspector_ID" +
                    " WHERE equipment_ID = " + equipmentId;
            Statement st = conn.createStatement();
            ResultSet result = st.executeQuery(query);

            result.next();

            equipment.setEquipment_ID(result.getInt("equipment_ID"));
            equipment.setInspector_ID(result.getInt("inspector_ID"));
            equipment.setName(result.getString("name"));
            equipment.setSite(result.getString("description"));
            equipment.setStatus(result.getString("state"));
            equipment.setDescription(result.getString("equipment_desc"));
            equipment.setStatusType(result.getString("status_type"));
            equipment.setComment(result.getString("comment"));
            equipment.setSite_ID(result.getInt("site_ID"));
            equipment.setInspector(result.getString("fname") + " " + result.getString("lname"));
        }
        catch (SQLException e){

        }
        finally{
            if(conn != null)
                newConnect.disconnect(conn);
        }

        return equipment;
    }

    public static void updateStatus(int equipmentId, String status, String statusType) {
        // TODO: Update status of equipment
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        try{
            // check if equipment has a status id of 1
            String query = "SELECT status_ID FROM equipment WHERE equipment_ID = " + equipmentId;
            Statement st = conn.createStatement();
            ResultSet result = st.executeQuery(query);
            result.next();
            int statusId = result.getInt("status_ID");

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
            String date = today.format(formatter);

            if(statusId == 1){
                // insert new status
                String sQuery = "INSERT INTO status (state, status_type) VALUES (?,?)";
                PreparedStatement prepSt = conn.prepareStatement(sQuery);
                prepSt.setString(1, status);
                prepSt.setString(2, statusType);
                prepSt.executeUpdate();

                Statement idSt = conn.createStatement();
                ResultSet idRs = idSt.executeQuery("SELECT LAST_INSERT_ID()");
                idRs.next();

                int newStatusId = idRs.getInt(1);

                String sQuery2 = "UPDATE equipment SET status_ID = ?, date_inspected = ? WHERE equipment_ID = ?";
                PreparedStatement st2 = conn.prepareStatement(sQuery2);

                st2.setInt(1, newStatusId);
                st2.setString(2, date);
                st2.setInt(3, equipmentId);
                st2.executeUpdate();
            }
            else{
                // get status id of equipment with equipment id = equipmentId
                String sQuery = "SELECT status_ID FROM equipment WHERE equipment_ID = " + equipmentId;
                Statement st2 = conn.createStatement();
                ResultSet result2 = st2.executeQuery(sQuery);
                result2.next();
                int statusId2 = result2.getInt("status_ID");

                //update state of status with status id = statusId2
                String sQuery2 = "UPDATE status SET state = ?, status_type = ? WHERE status_ID = ?";
                PreparedStatement st3 = conn.prepareStatement(sQuery2);
                st3.setString(1, status);
                st3.setString(2, statusType);
                st3.setInt(3, statusId2);
                st3.executeUpdate();

                // update date inspected of equipment with equipment id = equipmentId
                String sQuery3 = "UPDATE equipment SET date_inspected = ? WHERE equipment_ID = ?";
                PreparedStatement st4 = conn.prepareStatement(sQuery3);
                st4.setString(1, date);
                st4.setInt(2, equipmentId);
                st4.executeUpdate();
            }

            // update comment of equipment with equipment id = equipmentId with null
            String sQuery4 = "UPDATE equipment SET comment = ? WHERE equipment_ID = ?";
            PreparedStatement st5 = conn.prepareStatement(sQuery4);
            st5.setString(1, null);
            st5.setInt(2, equipmentId);
            st5.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally{
            if(conn != null)
                newConnect.disconnect(conn);
        }
    }

    // add comment to equipment
    public static void addComment(int equipmentId, String comment, String supervisorId) {
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        try{
            String query = "UPDATE equipment SET comment = ?, supervisor_ID = ? WHERE equipment_ID = ?";
            PreparedStatement st = conn.prepareStatement(query);

            st.setString(1, comment);
            st.setString(2, supervisorId);
            st.setInt(3, equipmentId);

            st.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally{
            if(conn != null)
                newConnect.disconnect(conn);
        }
    }

    // update edited equipment
    public static void updateEquipment(int equipmentId, String name, String description, int siteId, int inspectorId) {
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        try{
            String query = "UPDATE equipment SET name = ?, equipment_desc = ?, site_ID = ?, inspector_ID = ? WHERE equipment_ID = ?";
            PreparedStatement st = conn.prepareStatement(query);

            st.setString(1, name);
            st.setString(2, description);
            st.setInt(3, siteId);
            st.setInt(4, inspectorId);
            st.setInt(5, equipmentId);

            st.executeUpdate();
        }
        catch (SQLException e){

        }
        finally{
            if(conn != null)
                newConnect.disconnect(conn);
        }
    }

    public static void deleteEquipment(int equipmentId) throws Exception {
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        try{
            String dQuery = "DELETE FROM equipment WHERE equipment_ID = ?";
            PreparedStatement st = conn.prepareStatement(dQuery);

            st.setInt(1, equipmentId);

            st.executeUpdate();
        }
        catch (SQLException e){
            throw new Exception("SQL error");
        }
        finally {
            if(conn != null)
                newConnect.disconnect(conn);
        }
    }
}
