package com.sparta.amalcolm.view;

import com.sparta.amalcolm.controller.CSVReader;
import com.sparta.amalcolm.controller.EmployeeDAO;
import com.sparta.amalcolm.controller.Task_ThreadedExportToDatabase;
import com.sparta.amalcolm.model.EmployeeDTO;
import com.sparta.amalcolm.util.Printer;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class SequentialImport {

    public static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(SequentialImport.class);

    public static HashMap<String, EmployeeDTO> inputDataMap = new HashMap<>();
    public static ArrayList<EmployeeDTO> erroneousDataEntries = new ArrayList<>(); //ArrayList used because HashMap won't allow for duplicate entries.

    public static double[] timesTaken = {0, 0, 0}; // start/end of csv loading, start/end of database export, start/end of whole process.

    public static double[] PerformSequentialImport() throws SQLException {

        long startOverallTimer = System.nanoTime();

        try {
            //CSVReader accounts for duplicate entries and adds them to the "erroneousDataEntries" collection.
            inputDataMap = CSVReader.getValues(enums.SortType.Sequential);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Printer.PrintMessage("Input data: " + inputDataMap.size());
//        Printer.PrintMessage("Erroneous data: " + erroneousDataEntries.size());

        EmployeeDAO employeeDAO = new EmployeeDAO();
        if(employeeDAO == null){
            logger.info("The database has failed to connect.");
        }
        else{
            logger.info("Successfully connected to database: " + employeeDAO.connectToDatabase());
        }

        employeeDAO.setupTable();

        PreparedStatement preparedStatement = EmployeeDAO.populateTable();

        long startPopulation = System.nanoTime();
        for(Map.Entry<String, EmployeeDTO> employee : inputDataMap.entrySet()){
            EmployeeDAO.populatingTable(employee.getValue(), preparedStatement);
        }
        long endPopulation = System.nanoTime();

        timesTaken[1] = (endPopulation-startPopulation)/1000000;

        Printer.PrintMessage("Sequential - Time taken to import that data from the hashmap to database: " + timesTaken[1] + "ms");

        long endOverallTimer = System.nanoTime();

        timesTaken[2] = (endOverallTimer - startOverallTimer) / 1000000;

        return timesTaken;
    }
}
