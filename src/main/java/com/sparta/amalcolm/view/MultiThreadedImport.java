package com.sparta.amalcolm.view;

import com.sparta.amalcolm.controller.CSVReader;
import com.sparta.amalcolm.controller.EmployeeDAO;
import com.sparta.amalcolm.controller.Task_ThreadedExportToDatabase;
import com.sparta.amalcolm.model.EmployeeDTO;
import com.sparta.amalcolm.util.Printer;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultiThreadedImport {

    public static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(MultiThreadedImport.class);

    public static HashMap<String, EmployeeDTO> inputDataMap = new HashMap<>();
    public static HashMap<String, EmployeeDTO> inputDataMap1 = new HashMap<>();
    public static HashMap<String, EmployeeDTO> inputDataMap2 = new HashMap<>();
    public static HashMap<String, EmployeeDTO> inputDataMap3 = new HashMap<>();
    public static HashMap<String, EmployeeDTO> inputDataMap4 = new HashMap<>();
    public static ArrayList<EmployeeDTO> erroneousDataEntries = new ArrayList<>(); //ArrayList used because HashMap won't allow for duplicate entries.

    public static PreparedStatement preparedStatement;

    public static double[] timesTaken = {0, 0, 0}; // start/end of csv loading, start/end of database export, start/end of whole process.

    public static double[] PerformMultiThreadedImport() throws SQLException {

        long startOverallTimer = System.nanoTime();

        try {
            //CSVReader accounts for duplicate entries and adds them to the "erroneousDataEntries" collection.
            inputDataMap = CSVReader.getValues(enums.SortType.Multithreaded);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //In this next section the HashMap is being split into 4 quarter-sized hashmaps which can each be assigned a thread.

        int dataCollectionSize = inputDataMap.size();
        int currentPosition = 0;

        for(Map.Entry<String, EmployeeDTO> employee : inputDataMap.entrySet()){
            if(currentPosition < (dataCollectionSize/4)){
                inputDataMap1.put(employee.getKey(),employee.getValue());
            }
            else if(currentPosition >= (dataCollectionSize/4) && currentPosition < (dataCollectionSize/2)){
                inputDataMap2.put(employee.getKey(),employee.getValue());
            }
            else if(currentPosition >= (dataCollectionSize/2) && currentPosition < (dataCollectionSize*3/4)){
                inputDataMap3.put(employee.getKey(),employee.getValue());
            }
            else{
                inputDataMap4.put(employee.getKey(),employee.getValue());
            }
            currentPosition++;
        }

        EmployeeDAO employeeDAO = new EmployeeDAO();
        if(employeeDAO == null){
            logger.info("The database has failed to connect.");
        }
        else{
            logger.info("Successfully connected to database: " + employeeDAO.connectToDatabase());
        }

        employeeDAO.setupTable();

        preparedStatement = EmployeeDAO.populateTable();

        Task_ThreadedExportToDatabase exportToDatabase = new Task_ThreadedExportToDatabase();

        Thread[] threadCollection = new Thread[4];

        threadCollection[0] = new Thread(exportToDatabase);
        threadCollection[1] = new Thread(exportToDatabase);
        threadCollection[2] = new Thread(exportToDatabase);
        threadCollection[3] = new Thread(exportToDatabase);

        threadCollection[0].setName(String.valueOf(enums.ThreadNames.threadOne));
        threadCollection[1].setName(String.valueOf(enums.ThreadNames.threadTwo));
        threadCollection[2].setName(String.valueOf(enums.ThreadNames.threadThree));
        threadCollection[3].setName(String.valueOf(enums.ThreadNames.threadFour));

        long startPopulation = System.nanoTime();
        for (Thread thread : threadCollection){
            thread.start();
        }

        try {
            threadCollection[3].join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endPopulation = System.nanoTime();

        timesTaken[1] = (endPopulation-startPopulation)/1000000;

        Printer.PrintMessage("MultiThreaded - Time taken to import that data from the hashmap to database: " + timesTaken[1] + "ms");

        long endOverallTimer = System.nanoTime();

        timesTaken[2] = (endOverallTimer - startOverallTimer) / 1000000;

        return timesTaken;
    }
}
