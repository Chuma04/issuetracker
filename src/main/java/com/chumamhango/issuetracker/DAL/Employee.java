package com.chumamhango.issuetracker.DAL;

import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Employee {
    private int employee_ID;
    private String title;
    private String username;
    private String fname;
    private String mname;
    private String lname;
    private int user_ID;
    private String dob;
    private String gender;
    private String phone_number;
    private String email;
    private String role;


    public int getEmployee_ID() {
        return employee_ID;
    }

    public void setEmployee_ID(int employee_ID) {
        this.employee_ID = employee_ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public static void registerRequest(int userId, String title, String fName, String mName, String lName, String dob,
                                       String gender, String phone, String email) {

        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        try {
            StringUtils.capitalize(fName);
            StringUtils.capitalize(lName);

            String query = "INSERT INTO employee (user_ID, title, fname, mname, lname, dob, gender, phone_number, email) VALUES (?,?,?,?,?,?,?,?,?)";

            PreparedStatement insertStatement = conn.prepareStatement(query);

            insertStatement.setInt(1, userId);
            insertStatement.setString(2, title);
            insertStatement.setString(3, fName);
            insertStatement.setString(4, mName);
            insertStatement.setString(5, lName);
            insertStatement.setString(6, dob);
            insertStatement.setString(7, gender);
            insertStatement.setString(8, phone);
            insertStatement.setString(9, email);

            insertStatement.executeUpdate();

            newConnect.disconnect(conn);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                newConnect.disconnect(conn);
        }
    }

    public static List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();

        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        try {
            if (conn == null)
                throw new SQLException();

            Employee employee;

            String queryE = "SELECT u.username, u.role_ID, e.user_ID, e.employee_ID, e.title, e.fname, e.mname, e.lname, e.dob, e.gender, e.phone_number, e.email FROM employee e JOIN user u on u.user_ID = e.user_ID";
            Statement queryStatement = conn.createStatement();
            ResultSet eResult = queryStatement.executeQuery(queryE);

            while (eResult.next()) {
                String role;
                if(eResult.getInt("role_ID") == 1)
                    role = "Supervisor";
                else
                    role = "Inspector";
                employee = new Employee();

                employee.setEmployee_ID(eResult.getInt("employee_ID"));
                employee.setUser_ID(eResult.getInt("user_ID"));
                employee.setUsername(eResult.getString("username"));
                employee.setTitle(eResult.getString("title"));
                employee.setFname(eResult.getString("fname"));
                employee.setMname(eResult.getString("mname"));
                employee.setLname(eResult.getString("lname"));
                employee.setDob(eResult.getString("dob"));
                employee.setGender(eResult.getString("gender"));
                employee.setPhone_number(eResult.getString("phone_number"));
                employee.setRole(role);
                employee.setEmail(eResult.getString("email"));

                employees.add(employee);
            }
        } catch (SQLException e) {

        } finally {
            if (conn != null)
                newConnect.disconnect(conn);
        }

        return employees;
    }

    public static Employee getEmployee(int user_ID){
        Employee employee = new Employee();

        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        try {
            String queryE = "SELECT u.role_ID, u.user_ID,e.employee_ID, e.title, e.fname, e.mname, e.lname, e.dob, e.gender, e.phone_number, e.email FROM employee e JOIN user u on u.user_ID = e.user_ID WHERE u.user_ID = " + user_ID;
            Statement queryStatement = conn.createStatement();
            ResultSet eResult = queryStatement.executeQuery(queryE);

            eResult.next();

            String role;
            if(eResult.getInt("role_ID") == 1)
                role = "Supervisor";
            else
                role = "Inspector";

            employee.setEmployee_ID(eResult.getInt("employee_ID"));
            employee.setTitle(eResult.getString("title"));
            employee.setFname(eResult.getString("fname"));
            employee.setMname(eResult.getString("mname"));
            employee.setLname(eResult.getString("lname"));
            employee.setDob(eResult.getString("dob"));
            employee.setGender(eResult.getString("gender"));
            employee.setPhone_number(eResult.getString("phone_number"));
            employee.setUser_ID(eResult.getInt("user_ID"));
            employee.setRole(role);
            employee.setEmail(eResult.getString("email"));
        }
        catch (SQLException e){

        }
        finally {
            if(conn != null)
                newConnect.disconnect(conn);
        }

        return employee;
    }


    public static void updateEmployee(int user_ID, String role, String title, String fname, String mname, String lname, String dob, String gender, String phone_number, String email){
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        try{
            int roleNum;

            if(role.equals("Supervisor"))
                roleNum = 1;
            else
                roleNum = 2;

            String uUpdate = "UPDATE user SET role_ID = ? WHERE user_ID = ?";
            PreparedStatement st = conn.prepareStatement(uUpdate);

            st.setInt(1, roleNum);
            st.setInt(2, user_ID);
            st.executeUpdate();

            String eUpdate = "UPDATE employee SET title = ?, fname = ?, mname = ?, lname = ?, " +
                    "dob = ?, gender = ?, phone_number = ?, email = ? WHERE user_ID = ?";
            st = conn.prepareStatement(eUpdate);

            st.setString(1, title);
            st.setString(2, fname);
            st.setString(3, mname);
            st.setString(4, lname);
            st.setString(5, dob);
            st.setString(6, gender);
            st.setString(7, phone_number);
            st.setString(8, email);
            st.setInt(9, user_ID);
            st.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("A connection error occurred");
        }
        finally {
            if(conn != null)
                newConnect.disconnect(conn);
        }
    }

    public static void deleteEmployee(int userId){
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        try{
            String dQuery = "DELETE FROM employee WHERE user_ID = ?";
            PreparedStatement st = conn.prepareStatement(dQuery);

            st.setInt(1, userId);

            st.executeUpdate();
        }
        catch (SQLException e){

        }
        finally {
            if(conn != null)
                newConnect.disconnect(conn);
        }
    }

    public static boolean employeeExists(String email) {
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        boolean exists = false;

        try {
            Statement st = conn.createStatement();
            ResultSet result = st.executeQuery("SELECT * FROM employee WHERE email = '" + email + "'");

            if (result.next()) {
                exists = true;
            }
        } catch (SQLException e) {

        } finally {
            if (conn != null) {
                newConnect.disconnect(conn);
            }
        }
        return exists;
    }

    public static List<Employee> getEmployeesByRole(int roleId) {

        List<Employee> employees = new ArrayList<Employee>();

        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        try {
            if (conn == null)
                throw new SQLException();

            Employee employee;

            String queryE = "SELECT * FROM employee e JOIN user u ON e.user_ID = u.user_ID WHERE u.role_ID = " + roleId;
            Statement queryStatement = conn.createStatement();
            ResultSet eResult = queryStatement.executeQuery(queryE);

            while (eResult.next()) {
                String role;
                if(eResult.getInt("role_ID") == 1)
                    role = "Supervisor";
                else
                    role = "Inspector";
                employee = new Employee();

                employee.setEmployee_ID(eResult.getInt("employee_ID"));
                employee.setUser_ID(eResult.getInt("user_ID"));
                employee.setUsername(eResult.getString("username"));
                employee.setTitle(eResult.getString("title"));
                employee.setFname(eResult.getString("fname"));
                employee.setMname(eResult.getString("mname"));
                employee.setLname(eResult.getString("lname"));
                employee.setDob(eResult.getString("dob"));
                employee.setGender(eResult.getString("gender"));
                employee.setPhone_number(eResult.getString("phone_number"));
                employee.setRole(role);
                employee.setEmail(eResult.getString("email"));

                employees.add(employee);
            }
        } catch (SQLException e) {

        } finally {
            if (conn != null)
                newConnect.disconnect(conn);
        }

        return employees;
    }

    // count the number of supervisors in the database
    public static int countUsersByRole(int role) {
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        int count = 0;

        try {
            if (conn == null)
                throw new SQLException();

            String queryE = "SELECT * FROM employee e JOIN user u ON e.user_ID = u.user_ID WHERE u.role_ID = " + role;
            Statement queryStatement = conn.createStatement();
            ResultSet eResult = queryStatement.executeQuery(queryE);

            while (eResult.next()) {
                count++;
            }
        } catch (SQLException e) {

        } finally {
            if (conn != null)
                newConnect.disconnect(conn);
        }

        return count;
    }
}