package com.chumamhango.issuetracker.DAL;

import java.sql.*;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

public class User {
    private int user_ID;

    private String username;

    private int role_ID;

    public int getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(int user_ID) {
        this.user_ID = user_ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRole_ID() {
        return role_ID;
    }

    public void setRole_ID(int role_ID) {
        this.role_ID = role_ID;
    }

    public String loginRequest(String username, String pwd) throws SQLException {

        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        try {
            String usernameQuery = "SELECT username FROM user WHERE username = '" + username + "'";
            Statement usernameQueryStatement = conn.createStatement();
            ResultSet uResult = usernameQueryStatement.executeQuery(usernameQuery);

            //checking if user exist
            if(uResult.next()){
                Argon2PasswordEncoder hasher = new Argon2PasswordEncoder(
                        32,64,1,15*1024,2);

                String pwdQuery = "SELECT password FROM user WHERE username = '" + username + "'";
                Statement pwdQueryStatement = conn.createStatement();
                ResultSet pResult = pwdQueryStatement.executeQuery(pwdQuery);

                pResult.next();

                String storedPwd = pResult.getString("password");

                // checking if raw password matches the stored encrypted password
                if(hasher.matches(pwd, storedPwd)){
                    String query = "SELECT user_ID FROM user WHERE username = '" + username + "'";
                    Statement queryStatement = conn.createStatement();
                    ResultSet result = queryStatement.executeQuery(query);
                    result.next();

                    this.setUser_ID(result.getInt("user_ID"));

                    return "allowed"; // Supervisor or Inspector dashboard
                }
                else{
                    // wrong password error
                    return "pwd Error"; //back to login page
                }
            }
            else{
                //user does not exist error
                return "user error"; // back to login page
            }
        }
        catch (SQLException e){

            e.printStackTrace();
            return "SQL error";
        }
        finally {
            if (conn != null)
                newConnect.disconnect(conn);
        }
    }

    public boolean registerRequest(String fname, String lname, String pwd, String role, String email){
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        boolean valid = false;

        try {
            if(Employee.employeeExists(email)){
                return valid;
            }

            int roleNum;
            if(role.equals("Supervisor"))
                roleNum = 1;
            else
                roleNum = 2;

            // creating username
            String username = fname.toLowerCase() + lname.toLowerCase();

            String uQuery = "SELECT username FROM user WHERE username = '" + username + "'";
            Statement uQueryStatement = conn.createStatement();
            ResultSet uResult = uQueryStatement.executeQuery(uQuery);

            // finding unique username with concatenation of first name and last name
            String tempUsername = username;
            int increment = 0;
            while(uResult.next()){
                username = tempUsername + String.format("%02d", ++increment);
                uQuery = "SELECT username FROM user WHERE username = '" + username + "'";
                uResult = uQueryStatement.executeQuery(uQuery);
            }

            //hashing password
            Argon2PasswordEncoder hasher = new Argon2PasswordEncoder(32,64,1,15*1024,2);
            String hashedPwd = hasher.encode(pwd);

            String query = "INSERT INTO user (username, password, role_ID) VALUES (?,?,?)";
            PreparedStatement insertStatement = conn.prepareStatement(query);

            insertStatement.setString(1, username);
            insertStatement.setString(2, hashedPwd);
            insertStatement.setInt(3, roleNum);

            insertStatement.executeUpdate();

            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery("SELECT user_ID FROM user WHERE username = '" + username + "'");
            res.next();

            this.setUser_ID(res.getInt("user_ID"));

            valid = true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (conn != null)
                newConnect.disconnect(conn);
            return valid;
        }
    }

    public static void deleteUser(int userId){
        Connect newConnect = new Connect();
        Connection conn = newConnect.connect();

        //  deleting user from user table
        try{
            String dQuery = "DELETE FROM user WHERE user_ID = ?";
            PreparedStatement st = conn.prepareStatement(dQuery);

            st.setInt(1, userId);

            st.executeUpdate();
        }
        catch (SQLException e){
            System.out.println("A connection error occurred!");
        }
        finally {
            if(conn != null)
                newConnect.disconnect(conn);
        }
    }
}
