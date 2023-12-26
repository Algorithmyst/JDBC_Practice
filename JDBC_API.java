package com.JDBC.prog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class JDBC_API {
    String userName = "root";
    String passwrd = "ElectioN!1234";
    String URL = "jdbc:mysql://localhost:3306/studentdb";
    Logger log_obj = LogManager.getLogger();

    public void queryExe(String query) {

        try (Connection con = DriverManager.getConnection(URL, userName, passwrd)) {
            Statement st = con.createStatement();
            if (!st.execute(query)) {
                log_obj.info("Table Updated Successfully");
            } else {
                ResultSet rs = st.executeQuery(query);
                ResultSetMetaData rsmd = rs.getMetaData();
                // "%s\t%-10s\t%-10s\t%-10s\t%-10s\t%s%n"
                log_obj.info("{}\t{}\t{}\t{}\t{}\t{}\n", rsmd.getColumnLabel(1), rsmd.getColumnLabel(2),
                        rsmd.getColumnLabel(3), rsmd.getColumnLabel(4), rsmd.getColumnLabel(5), rsmd.getColumnLabel(6));
                while (rs.next()) {
                    // "%2d\t%-10s\t%-10s\t%-10s\t%-10s\t%5d%n"
                    log_obj.info("{}\t{}\t{}\t{}\t{}\t{}\n", rs.getInt("ID"), rs.getString("FName"),
                            rs.getString("LName"), rs.getString("Stream"), rs.getString("City"), rs.getInt("Marks"));
                }
                rs.close();
            }
            st.close();
        } catch (SQLException e) {
            log_obj.error(e);
            userName = "root";
            passwrd = "ElectioN!123";
            URL = "jdbc:mysql://localhost:3306/studentdb";
            queryExe(query);
        }
    }

    public boolean checkForExceptions(String query, int id_to_check) {
        try (Connection con = DriverManager.getConnection(URL, userName, passwrd);
             Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                if (rs.getInt("ID") == id_to_check) {
                    return true;
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            userName = "root";
            passwrd = "ElectioN!123";
            URL = "jdbc:mysql://localhost:3306/studentdb";
            checkForExceptions(query, id_to_check);
        }
        return false;
    }

}
