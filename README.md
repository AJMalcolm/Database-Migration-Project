# Database Migration Project

Project completed while training at Sparta Global.

## About

Java 11 project to import data from a csv file, check the data for duplicate entries and then export the non-duplicated data to an SQL database. Maven used for dependency handling and JUnit4 for testing of functionality. Some basic logging performed as required using Log4j. My SQL Workbench also used to observe and test functionality.

## Tools Used

- JDK 11
- IntelliJ
- JUnit5
- Log4J
- My SQL Workbench CE 8.0
- Git

## Topics used in Project Functionality

- OOP Principles
- SOLID Principles
- Logging (Log4j)
- Unit Testing
- Defined Packages
- Iterations
- Enums
- Predefined Exceptions

## Approach

Project was completed in several steps. Firstly the FileReader/BufferedReader was tested and implamented, importing the csv file data to an ArrayList. Once this was functional I decided to switch the collection method to a HashMap, allowing for more streamlined additional functionality in the future (faster searching etc.) if it were needed.
Next the sequential approach to exporting the data to the local SQL server was developed as this was the simpler version of the software and I expected fewer issues during development. When this was complete I added the multi-threaded approach, splitting the simultaneous workload into 4 sections and passing each thread 1/4 of the data. Finally some testing and observation was performed when running the program multiple times.


Example Output:

Time taken to load the CSV file into a sequential HashMap = 493.0ms
Sequential - Time taken to import that data from the hashmap to database: 188145.0ms
Time taken to load the CSV file into a threaded HashMap = 371.0ms
MultiThreaded - Time taken to import that data from the hashmap to database: 144420.0ms
The total time taken for the sequential operation was 191006.0ms.
The total time taken for the multi-threaded (4) operation was 145063.0ms.
The approach using 4 threads was 1.3167106705362497x faster than the sequential approach.


## Future Thoughts for Development

In order to provide a definitive overview of the efficiency of multiple cores working on a task, the process would need to be run many times and average results taken. Also a wider range of core values (2, 4, 8, 16, 32, 64 cores) would be ideal to see if there is a performance-peak regarding their efficiency. And finally a possibly bottleneck in this process was the speed of the PC being used to test the functionality, making a comparison between multiple PCs (with different numbers of CPU cores) would shed yet more light on the efficiency of multi-threading.

On a personal-development note I would like to experiment with different testing methods and look to improve my approach to both unit and performance testing.

## Author

Alasdair Malcolm
