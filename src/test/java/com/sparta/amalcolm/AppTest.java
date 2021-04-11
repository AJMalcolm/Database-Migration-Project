package com.sparta.amalcolm;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class AppTest 
{
    @Test
    @DisplayName("Can access/load from file.")
    public void accessLoadFileTest() throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/employeesRecordSmall.csv"));

        boolean ready = bufferedReader.ready();

        assertTrue(ready);
    }

    @Test
    @DisplayName("Can connect to SQL database")
    public void connectSQLDatabaseTest() {
        final String URL = "jdbc:mysql://localhost:3306/db_DataMigration?serverTimerzone=GMT";
        Connection connection = null;

        try{
            connection = DriverManager.getConnection(URL, System.getenv("java_username"), System.getenv("java_password"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        assertNotNull( connection );
    }
}
