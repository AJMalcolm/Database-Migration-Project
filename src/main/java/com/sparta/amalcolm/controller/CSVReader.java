package com.sparta.amalcolm.controller;

import com.sparta.amalcolm.model.EmployeeDTO;
import com.sparta.amalcolm.view.*;
import com.sparta.amalcolm.util.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVReader {
    public static HashMap<String, EmployeeDTO> getValues(enums.SortType sortType) throws IOException {

        ArrayList<EmployeeDTO> erroneousDataEntries;

        if(sortType == enums.SortType.Sequential){
            erroneousDataEntries = SequentialImport.erroneousDataEntries;
        }
        else if(sortType == enums.SortType.Multithreaded){
            erroneousDataEntries = MultiThreadedImport.erroneousDataEntries;
        }
        else {
            //This should not happen.
            erroneousDataEntries = new ArrayList<>();
        }

        long start = System.nanoTime();
        HashMap<String, EmployeeDTO> Employees = new HashMap<>();
        ArrayList<String> addedIDs = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/employeesRecordSmall.csv"));

        String currentLine;
        bufferedReader.readLine(); // Using this to skip the first line within the CSV file (headers of columns).

        while((currentLine = bufferedReader.readLine()) !=null ) {
            String[] records = currentLine.split(",");
            EmployeeDTO employee = new EmployeeDTO(records[0], records[1], records[2], records[3], records[4], records[5], records[6], records[7], records[8], records[9]);
            if(!addedIDs.contains(records[0])){
                Employees.put(records[0], employee);
                addedIDs.add(records[0]);
            }
            else{
                erroneousDataEntries.add(employee);
            }
        }

        long end = System.nanoTime();

        double timeTaken = (end - start)/1000000;

        if(sortType == enums.SortType.Sequential){

            Printer.PrintMessage("Time taken to load the CSV file into a sequential HashMap = " + timeTaken + "ms");
            SequentialImport.timesTaken[0] = timeTaken;
        }
        else if(sortType == enums.SortType.Multithreaded){
            Printer.PrintMessage("Time taken to load the CSV file into a threaded HashMap = " + timeTaken + "ms");
            MultiThreadedImport.timesTaken[0] = timeTaken;
        }
        else{
            //This should not happen
        }

        return Employees;
    }
}
