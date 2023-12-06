package edu.upenn.cit594;

import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.*;
import edu.upenn.cit594.ui.UserInterface;
import edu.upenn.cit594.util.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws CSVFormatException, IOException {
        // Initialize Logger
        Logger logger = Logger.getInstance();

        // Parse command-line arguments
        if (args.length < 1 || args.length > 4) {
            System.out.println("Error: Invalid number of arguments. Please provide between one and four file paths.");
            logger.logEvent("Error: Invalid number of arguments. Please provide between one and four file paths.");
            System.exit(1);
        }

        // Parse arguments in the format --name=value
        Map<String, String> argMap = parseArguments(args);

        // Retrieve values from the argument map
        String populationFilePath = argMap.get("population");
        // If logger is not specified (null) it will default to System.err output
        String logFile = argMap.get("log");
        String covidDataFilePath = argMap.get("covid");
        String propertiesFile = argMap.get("properties");

        // Set the log file
        logger.setOutputFile(logFile);

        // Log command line arguments
        logger.logEvent("Command line arguments: " + String.join(" ", args));

        DataReader<Population> populationDataReader = new PopulationFileReader();
        DataReader<Vaccination> vaccinationDataReader;
        DataReader<Property> propertyDataReader = new CsvPropertyReader();
        if (covidDataFilePath.toLowerCase().endsWith(".json")) {
            vaccinationDataReader = new JsonVaccinationReader();
        } else if (covidDataFilePath.toLowerCase().endsWith(".csv")) {
            vaccinationDataReader = new CsvVaccinationReader();
        } else {
            System.out.println("Error: Unsupported file format for vaccination data. Please use JSON or CSV.");
            logger.logEvent("Error: Unsupported file format for vaccination data. Please use JSON or CSV.");
            System.exit(1);
            return; // This is to avoid further execution if there's an error
        }

        // Initialize processors and other components
        PopulationDataProcessor populationDataProcessor = new PopulationDataProcessor(populationDataReader, populationFilePath);
        VaccinationDataProcessor vaccinationDataProcessor = new VaccinationDataProcessor(vaccinationDataReader, populationDataProcessor, covidDataFilePath);
        PropertyDataProcessor propertyDataProcessor = new PropertyDataProcessor(propertiesFile, propertyDataReader, populationDataProcessor);
        getParameter getParameter = new getParameter();
        DataProcessor dataProcessor = new DataProcessor(populationDataProcessor, vaccinationDataProcessor, propertyDataProcessor, getParameter);
        UserInterface userInterface = new UserInterface(dataProcessor);
        userInterface.run();

        // Close the logger
        logger.closeLogger();
    }

//    private static Map<String, String> parseArguments(String[] args) {
//        Map<String, String> arguments = new HashMap<>();
//        for (String arg : args) {
//            String[] parts = arg.split("=", 2);
//            if (parts.length == 2) {
//                arguments.put(parts[0], parts[1]);
//            } else {
//                System.out.println("Error: Invalid argument format. Use --name=value");
//                System.exit(1);
//            }
//        }
//        return arguments;
//    }

    /**
     * Parses and validates the command-line arguments. Runtime arguments should be in the form “--name=value”.
     * There are 4 optional runtime arguments to the program:
     * • covid: The name of the COVID data file
     * • properties: The name of the property values file
     * • population: The name of the population data file
     * • log: The name of the log file (described below)
     * @param args The command-line arguments.
     * @return A map of argument names to their values.
     * @throws IllegalArgumentException under any of the following
     * conditions:
     * • Any arguments to main do not match the form “--name=value”.
     * • The name of an argument is not one of the names listed above.
     * • The name of an argument is used more than once (e.g., "--log=a.log --log=a.log").
     *
     */
    private static Map<String, String> parseArguments(String[] args) {
        //checking if runtime arguments is in the form “--name=value”.
        Pattern argPattern = Pattern.compile("^--(?<name>.+?)=(?<value>.+)$");
        Map<String, String> arguments = new HashMap<>();

        for (String arg : args) {
            var matcher = argPattern.matcher(arg);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Error: Invalid argument format. Please use --name=value");
            }
            String name = matcher.group("name");
            String value = matcher.group("value");

            //checking if the name of an argument is used more than once
            if (arguments.containsKey(name)) {
                throw new IllegalArgumentException("Error: Duplicate argument '" + name + "'");
            }

            // Check if the name of the argument is valid as one of the "covid, properties, population, log"
            if (!Set.of("covid", "properties", "population", "log").contains(name)) {
                throw new IllegalArgumentException("Error: Unknown argument name '" + name + "'");
            }

            arguments.put(name, value);
        }

// notes: not all 4 arguments are required - the assignment says "optional arguments"
//        // Check if all required argument names are present
//        if (!arguments.keySet().containsAll(Set.of("covid", "properties", "population", "log"))) {
//            throw new IllegalArgumentException("Error: Missing required arguments.");
//        }

        return arguments;
    }



}
