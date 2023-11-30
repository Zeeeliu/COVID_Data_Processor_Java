package edu.upenn.cit594.processor;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.util.getParameter;

import java.util.Scanner;

public class DataProcessor {
    Logger logger = Logger.getInstance();
    private final PopulationDataProcessor populationDataProcessor;
    private final VaccinationDataProcessor vaccinationDataProcessor;
    private final PropertyDataProcessor propertyDataProcessor;

    private final getParameter getParameter;
    public DataProcessor(PopulationDataProcessor populationDataProcessor, VaccinationDataProcessor vaccinationDataProcessor,
                            PropertyDataProcessor propertyDataProcessor, getParameter getParameter) {
        this.populationDataProcessor = populationDataProcessor;
        this.vaccinationDataProcessor = vaccinationDataProcessor;
        this.propertyDataProcessor = propertyDataProcessor;
        this.getParameter = getParameter;
    }

    public void showTotalPopulation() {
        try {
            int totalPopulation = populationDataProcessor.getTotalPopulation();
            System.out.println("BEGIN OUTPUT");
            System.out.println(totalPopulation);
            logger.logEvent("Total population: " + totalPopulation);
            System.out.println("END OUTPUT");
        } catch (Exception e) {
            System.out.println("Error: Unable to retrieve total population. Please make sure the population file is provided.");
        }
    }
    public void showVaccinationsPerCapita(Scanner scanner) {
        try {
            vaccinationDataProcessor.showVaccinationsPerCapita(scanner);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void showAveragePropertyMetric(PropertyAverageCalculator calculator,Scanner scanner) {
        propertyDataProcessor.setCalculator(calculator);
        String zipCode = getParameter.getZipCode(scanner);
        propertyDataProcessor.showAveragePropertyMetric(zipCode);

    }

    public void showTotalMarketValuePerCapita(Scanner scanner) {
        String zipCode = getParameter.getZipCode(scanner);
        propertyDataProcessor.getTotalMarketValuePerCapita(zipCode);
    }

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
