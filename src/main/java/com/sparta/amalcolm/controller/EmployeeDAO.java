package com.sparta.amalcolm.controller;

import com.sparta.amalcolm.model.EmployeeDTO;
import com.sparta.amalcolm.util.Printer;

import java.sql.*;

import static java.lang.Integer.parseInt;

public class EmployeeDAO {

    private final String URL = "jdbc:mysql://localhost:3306/db_DataMigration?serverTimerzone=GMT";
    private static Connection connection = null;

    private final static String setupTable="DROP TABLE IF EXISTS employees";
    private final static String createTable="CREATE TABLE employees(EmployeeID INT(6) PRIMARY KEY,Title VARCHAR(5),First_Name VARCHAR (20),Middle_Initial CHAR(1),Last_Name VARCHAR(30),Gender CHAR(1),eMail VARCHAR(50),Date_of_Birth CHAR(10),Date_of_Joining CHAR(10),Salary INT(6))";
    private final static String insertEmployee="INSERT INTO employees(EmployeeID, Title, First_Name, Middle_Initial, Last_Name, Gender, eMail, Date_of_Birth, Date_of_Joining, Salary)VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private final static String selectingEmployees = "SELECT * FROM employees";

    public Connection connectToDatabase(){
        try{
            connection = DriverManager.getConnection(URL, System.getenv("java_username"), System.getenv("java_password"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    public void setupTable(){
        try(PreparedStatement preparedStatement = connectToDatabase().prepareStatement(setupTable)){
            preparedStatement.executeUpdate();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        try(PreparedStatement preparedStatement = connectToDatabase().prepareStatement(createTable)){
            preparedStatement.executeUpdate();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    public static PreparedStatement populateTable() throws SQLException {

        PreparedStatement populatingStatement = EmployeeDAO.getConnection().prepareStatement(EmployeeDAO.getInsertEmployee());

        return populatingStatement;
    }

    public static synchronized void populatingTable(EmployeeDTO employee, PreparedStatement preparedStatement){
        try{
            preparedStatement.setInt(1, parseInt(employee.getSerialNumber()));
            preparedStatement.setString(2, employee.getNamePrefix());
            preparedStatement.setString(3, employee.getFirstName());
            preparedStatement.setString(4, employee.getMiddleInitial());
            preparedStatement.setString(5, employee.getLastName());
            preparedStatement.setString(6, employee.getGender());
            preparedStatement.setString(7, employee.getEmail());
            preparedStatement.setString(8, employee.getDateOfBirth());
            preparedStatement.setString(9, employee.getDateOfJoining());
            preparedStatement.setInt(10, parseInt(employee.getSalary()));
            int hasRun = preparedStatement.executeUpdate();

            if(hasRun>0){

            }
            else{
                Printer.PrintMessage("Update Unsuccessful.");
            }
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }

    //Added selectingAllEmployees for checking purposes while creating the program.

    public static void selectingAllEmployees(){

        int i = 1;

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectingEmployees);

            if(resultSet != null){
                while (resultSet.next()){
                    Printer.PrintMessage("Employee ID: "+resultSet.getInt(1));
                    Printer.PrintMessage("Title: "+resultSet.getString(2));
                    Printer.PrintMessage("First Name: "+resultSet.getString(3));
                    Printer.PrintMessage("Middle Initial: "+resultSet.getString(4));
                    Printer.PrintMessage("Last Name: "+resultSet.getString(5));
                    Printer.PrintMessage("Gender: "+resultSet.getString(6));
                    Printer.PrintMessage("eMail: "+resultSet.getString(7));
                    Printer.PrintMessage("Date of Birth: "+resultSet.getString(8));
                    Printer.PrintMessage("Date of Joining: "+resultSet.getString(9));
                    Printer.PrintMessage("Salary: "+resultSet.getInt(10));

                    Printer.PrintMessage("Reading: " + i++);
                }
            }
            else{
                Printer.PrintMessage("No Records Found");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static String getInsertEmployee() {
        return insertEmployee;
    }
}
