package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.DataReader;
import edu.upenn.cit594.datamanagement.Property;
import edu.upenn.cit594.logging.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PropertyDataProcessor {
    Logger logger = Logger.getInstance();

    private PropertyAverageCalculator calculator;
    private DataReader<Property> propertyDataReader;  // Updated to use DataReader interface
    private PopulationDataProcessor populationDataProcessor;
    private List<Property> propertyData;  // Add a member variable to store the property data
    String propertyFilePath;
    //Use a map to store calculated average property metrics for each ZIP Code
    private Map<String, Double> averageMetricCache  = new HashMap<>();

    // Constructor to initialize the data and calculator
    public PropertyDataProcessor(String propertyFilePath, DataReader<Property> propertyDataReader, PopulationDataProcessor populationDataProcessor) {
        // Use CsvPropertyReader to read property data from the CSV file
        this.populationDataProcessor = populationDataProcessor;
        this.propertyDataReader = propertyDataReader;
        this.propertyFilePath = propertyFilePath;
        this.propertyData = propertyDataReader.readData(propertyFilePath);
        logger.logEvent("Property data loaded from " + propertyFilePath);
    }

    public void showAveragePropertyMetric(String zipCode) {
        try {
            double averageMetric = getOrCalculateAveragePropertyMetric(zipCode);
            System.out.println("BEGIN OUTPUT");
            logger.logEvent("Average property metric for ZIP Code " + zipCode + ": " + (int)averageMetric);
            System.out.println((int) averageMetric); // Truncate to integer
            System.out.println("END OUTPUT");
        } catch (Exception e) {
            System.out.println("Error: Unable to calculate average property metric. Please make sure the property data is provided.");
        }
    }
    private double getOrCalculateAveragePropertyMetric(String zipCode) {
        if (averageMetricCache.containsKey(zipCode)) {
            return averageMetricCache.get(zipCode);
        } else {
            double averageMetric = calculateAveragePropertyMetric(zipCode);
            averageMetricCache.put(zipCode, averageMetric);
            return averageMetric;
        }
    }
    private double calculateAveragePropertyMetric(String zipCode) {
        List<Property> propertiesInZipCode = propertyData.stream()
                .filter(p -> p.getZipCode().equals(zipCode))
                .collect(java.util.stream.Collectors.toList());

        return calculator.calculateAverage(propertiesInZipCode);
    }


    public void getTotalMarketValuePerCapita(String zipCode) {
        String zipCode_2 = "\""+zipCode+"\"";
        try {
            // Get the population for the specified ZIP Code
            int population = populationDataProcessor.getPopulationByZipCode(zipCode_2);

            if (population == 0) {
                System.out.println(0);  // ZIP Code not found or population is 0
                return;
            }

            // Filter properties by the specified ZIP Code
            List<Property> propertiesInZipCode = propertyData.stream()
                    .filter(property -> property.getZipCode().equals(zipCode))
                    .collect(Collectors.toList());

            if (propertiesInZipCode.isEmpty()) {
                System.out.println(0);  // No properties found for the ZIP Code
                return;
            }

            // Calculate total market value for the ZIP Code
            double totalMarketValue = propertiesInZipCode.stream()
                    .filter(property -> !Double.isNaN(property.getMarketValue()))
                    .mapToDouble(Property::getMarketValue)
                    .sum();

            // Calculate total market value per capita
            double marketValuePerCapita = totalMarketValue / population;

            // Return the truncated integer value
            System.out.println("BEGIN OUTPUT");
            logger.logEvent("Total Market Value Per Capita for ZIP Code " + zipCode + ": " + (int)marketValuePerCapita);
            System.out.println((int) marketValuePerCapita); // Truncate to integer
            System.out.println("END OUTPUT");
//            logger.logEvent("Total Market Value Per Capita for ZIP Code " + zipCode + ": " + marketValuePerCapita);
        } catch (Exception e) {
            System.out.println("Error: Unable to calculate total market value per capita.");
        }
    }

    public void setCalculator(PropertyAverageCalculator calculator) {
        this.calculator = calculator;
    }

}
