package edu.upenn.cit594.processor;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.getParameter;

import java.util.Scanner;

/**
 * Main processor class for handling different types of data processing based on user input.
 */
public class DataProcessor {
    Logger logger = Logger.getInstance();
    private final PopulationDataProcessor populationDataProcessor;
    private final VaccinationDataProcessor vaccinationDataProcessor;
    private final PropertyDataProcessor propertyDataProcessor;

    private final getParameter getParameter;

    /**
     * Constructor for DataProcessor.
     *
     * @param populationDataProcessor A processor for population data.
     * @param vaccinationDataProcessor A processor for vaccination data.
     * @param propertyDataProcessor A processor for property data.
     * @param getParameter A utility for getting parameters from user input.
     */
    public DataProcessor(PopulationDataProcessor populationDataProcessor, VaccinationDataProcessor vaccinationDataProcessor,
                            PropertyDataProcessor propertyDataProcessor, getParameter getParameter) {
        this.populationDataProcessor = populationDataProcessor;
        this.vaccinationDataProcessor = vaccinationDataProcessor;
        this.propertyDataProcessor = propertyDataProcessor;
        this.getParameter = getParameter;
    }

    /**
     * Displays the total population from the population data.
     */
    public void showTotalPopulation() {
        try {
            int totalPopulation = populationDataProcessor.getTotalPopulation();
            System.out.println("BEGIN OUTPUT");
            System.out.println(totalPopulation);
            logger.logEvent("Total population: " + totalPopulation);
            System.out.println("END OUTPUT");
        } catch (Exception e) {
            System.out.println("Error: Unable to retrieve total population. Please make sure the population file is provided.");
            logger.logEvent("Error displaying total population: " + e.getMessage());
        }
    }


    /**
     * Displays the total full or partial vaccinations per capita for each ZIP Code for a given date.
     * @param scanner The scanner to read user input.
     */
    public void showVaccinationsPerCapita(Scanner scanner) {
        try {
            vaccinationDataProcessor.showVaccinationsPerCapita(scanner);
        } catch (Exception e) {
            System.out.println("Error: Unable to retrieve vaccination per capita.");
            logger.logEvent("Error displaying vaccinations per capita: " + e.getMessage());
        }
    }


    /**
     * Displays the average property metric (either market value or total livable area) for a specified ZIP Code.
     * @param calculator The calculator to use for the property metric.
     * @param scanner The scanner to read user input.
     */
    public void showAveragePropertyMetric(PropertyAverageCalculator calculator,Scanner scanner) {
        try {
            propertyDataProcessor.setCalculator(calculator);
            String zipCode = getParameter.getZipCode(scanner);
            propertyDataProcessor.showAveragePropertyMetric(zipCode);
        } catch (Exception e) {
            System.out.println("Error: Unable to retrieve average property metric for ZIP Code. ");
            logger.logEvent("Error displaying average property metric: " + e.getMessage());
        }
    }


    /**
     * Displays the total market value per capita for properties in a specified ZIP Code.
     * @param scanner The scanner to read user input.
     */
    public void showTotalMarketValuePerCapita(Scanner scanner) {
        try {
            String zipCode = getParameter.getZipCode(scanner);
            propertyDataProcessor.getTotalMarketValuePerCapita(zipCode);
        } catch (Exception e) {
            System.out.println("Error: Unable to retrieve total market value per capita for ZIP Code. ");
            logger.logEvent("Error displaying total market value per capita: " + e.getMessage());
        }
    }


    /**
     * Displays a list of the currently available actions.
     */
    public void showAvailableActions() {
        System.out.println("BEGIN OUTPUT");
        AvailableActions[] availableActions = AvailableActions.values();

        // Always display 0 and 1
        System.out.println(availableActions[0].getActionNumber());
        System.out.println(availableActions[1].getActionNumber());
        logger.logEvent("Available actions: " + availableActions[0].getActionNumber()) ;
        logger.logEvent(String.valueOf(availableActions[1].getActionNumber())) ;
        // Display 2-6 only if the required parameters are provided
        if (populationDataProcessor != null && vaccinationDataProcessor != null && propertyDataProcessor != null) {
            for (int i = 2; i < Math.min(availableActions.length, 7); i++) {
                System.out.print(availableActions[i].getActionNumber() + " ");
                logger.logEvent(String.valueOf(availableActions[i].getActionNumber()));
                System.out.println();
            }
        }

        System.out.println("END OUTPUT");
    }


    /**
     * Displays the total number of fully vaccinated individuals across all ZIP codes.
     */
    public void showTotalFullVaccinations() {
        try {
            System.out.println("BEGIN OUTPUT");
            vaccinationDataProcessor.showTotalFullyVaccinatedByZipCode();
            System.out.println("END OUTPUT");
        } catch (Exception e) {
            System.out.println("Error: Unable to retrieve total full vaccinations. Please make sure the vaccination file is provided.");
        }
    }

}
