package edu.upenn.cit594;

import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.*;
import edu.upenn.cit594.ui.UserInterface;
import edu.upenn.cit594.util.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Initialize Logger
        Logger logger = Logger.getInstance();

        // Parse command-line arguments
        // Parse command-line arguments
        if (args.length < 1 || args.length > 4) {
            System.out.println("Error: Invalid number of arguments. Please provide between one and four file paths.");
            logger.logEvent("Error: Invalid number of arguments. Please provide between one and four file paths.");
            System.exit(1);
        }

        // Parse arguments in the format --name=value
        Map<String, String> argMap = parseArguments(args);

        // Retrieve values from the argument map
        String populationFilePath = argMap.get("--population");
        String logFile = argMap.get("--log");
        String covidDataFilePath = argMap.get("--covid");
        String propertiesFile = argMap.get("--properties");

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

    private static Map<String, String> parseArguments(String[] args) {
        Map<String, String> arguments = new HashMap<>();
        for (String arg : args) {
            String[] parts = arg.split("=", 2);
            if (parts.length == 2) {
                arguments.put(parts[0], parts[1]);
            } else {
                System.out.println("Error: Invalid argument format. Use --name=value");
                System.exit(1);
            }
        }
        return arguments;
    }
}
