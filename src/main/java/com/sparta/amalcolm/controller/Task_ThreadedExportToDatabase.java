package com.sparta.amalcolm.controller;

import com.sparta.amalcolm.model.EmployeeDTO;
import com.sparta.amalcolm.view.MultiThreadedImport;
import com.sparta.amalcolm.view.enums;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.HashMap;
import java.util.Map;
import java.lang.*;

public class Task_ThreadedExportToDatabase implements Runnable{

    public static final Logger logger = LogManager.getLogger(Task_ThreadedExportToDatabase.class);

    public void run(){

        EmployeeDAO employeeDAO = new EmployeeDAO();
        if(employeeDAO == null){
            logger.error("The database has failed to connect.");
        }
        else{
            logger.info("Successfully connected to database: " + employeeDAO.connectToDatabase());
        }

        String threadName = getThreadName();

        HashMap<String, EmployeeDTO> inputDataMap;

        if(threadName == String.valueOf(enums.ThreadNames.threadOne)){
            inputDataMap = MultiThreadedImport.inputDataMap1;
        }
        else if(threadName == String.valueOf(enums.ThreadNames.threadTwo)){
            inputDataMap = MultiThreadedImport.inputDataMap2;
        }
        else if(threadName == String.valueOf(enums.ThreadNames.threadThree)){
            inputDataMap = MultiThreadedImport.inputDataMap3;
        }
        else if(threadName == String.valueOf(enums.ThreadNames.threadFour)){
            inputDataMap = MultiThreadedImport.inputDataMap4;
        }
        else{
            //This should not happen
            inputDataMap = new HashMap<>();
        }

        for(Map.Entry<String, EmployeeDTO> employee : inputDataMap.entrySet()){
            EmployeeDAO.populatingTable(employee.getValue(), MultiThreadedImport.preparedStatement);
        }
    }

    private String getThreadName(){

        Thread currentThread = Thread.currentThread();
        String threadName = currentThread.getName();

        return threadName;
    }
}
