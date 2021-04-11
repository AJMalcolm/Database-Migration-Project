package com.sparta.amalcolm.view;

import com.sparta.amalcolm.util.*;

import java.sql.SQLException;


public class App
{

    public static void main( String[] args ) throws SQLException {

        double[] sequentialTimesTaken = SequentialImport.PerformSequentialImport();
        double[] multiThreadedTimesTaken = MultiThreadedImport.PerformMultiThreadedImport();

        Printer.PrintMessage("The total time taken for the sequential operation was " + sequentialTimesTaken[2] + "ms.");
        Printer.PrintMessage("The total time taken for the multi-threaded (4) operation was " + multiThreadedTimesTaken[2] + "ms.");

        Printer.PrintMessage("The approach using 4 threads was " + (sequentialTimesTaken[2]/multiThreadedTimesTaken[2]) + "x faster than the sequential approach.");

    }
}