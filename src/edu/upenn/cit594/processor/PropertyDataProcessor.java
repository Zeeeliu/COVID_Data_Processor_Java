package edu.upenn.cit594.processor;
import edu.upenn.cit594.datamanagement.CSVFormatException;
import edu.upenn.cit594.datamanagement.DataReader;
import edu.upenn.cit594.datamanagement.Property;
import edu.upenn.cit594.logging.Logger;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Processor for handling property-related data and calculations.
 */
public class PropertyDataProcessor {
    Logger logger = Logger.getInstance();

    private PropertyAverageCalculator calculator;
    private DataReader<Property> propertyDataReader;  // Updated to use DataReader interface
    private PopulationDataProcessor populationDataProcessor;
    private List<Property> propertyData;  // Add a member variable to store the property data
    String propertyFilePath;
    //Use a map to store calculated average property metrics for each ZIP Code
    private Map<String, Double> averageMetricCache  = new HashMap<>();


    /**
     * Constructor to initialize the data and calculator.
     *
     * @param propertyFilePath Path to the property data file.
     * @param propertyDataReader Data reader for property data.
     * @param populationDataProcessor Processor for population data.
     */
    public PropertyDataProcessor(String propertyFilePath, DataReader<Property> propertyDataReader, PopulationDataProcessor populationDataProcessor) throws CSVFormatException, IOException {
        // Use CsvPropertyReader to read property data from the CSV file
        this.populationDataProcessor = populationDataProcessor;
        this.propertyDataReader = propertyDataReader;
        this.propertyFilePath = propertyFilePath;
        this.propertyData = propertyDataReader.readData(propertyFilePath);
        logger.logEvent("Property data loaded from " + propertyFilePath);
    }


    /**
     * Shows the average property metric for a given ZIP code.
     *
     * @param zipCode The ZIP code for which to calculate the average.
     */
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


    /**
     * Calculates and displays the average property metric for a specified ZIP code.
     * The calculation is based on the current calculator set in the processor.
     *
     * @param zipCode The ZIP code for which the average metric is to be calculated.
     */
    private double getOrCalculateAveragePropertyMetric(String zipCode) {
        String calculatorName = calculator.getClass().getSimpleName(); // Get the class name of the calculator
        String cacheKey = calculatorName + ":" + zipCode; // Combine calculator name and zip code as the cache key

        if (averageMetricCache.containsKey(cacheKey)) {
            return averageMetricCache.get(cacheKey);
        } else {
            double averageMetric = calculateAveragePropertyMetric(zipCode);
            averageMetricCache.put(cacheKey, averageMetric);
            return averageMetric;
        }
    }


    /**
     * Calculates the average property metric for the specified ZIP code.
     * The specific metric is determined by the currently set calculator.
     *
     * @param zipCode The ZIP code for which the average metric is to be calculated.
     * @return The calculated average metric value.
     */
    private double calculateAveragePropertyMetric(String zipCode) {
        List<Property> propertiesInZipCode = propertyData.stream()
                .filter(p -> p.getZipCode().equals(zipCode))
                .collect(java.util.stream.Collectors.toList());

        return calculator.calculateAverage(propertiesInZipCode);
    }


    /**
     * Calculates and displays the total market value per capita for properties in a specified ZIP code.
     * The calculation considers the population of the ZIP code and the total market value of properties.
     *
     * @param zipCode The ZIP code for which the market value per capita is to be calculated.
     */
    public void getTotalMarketValuePerCapita(String zipCode) {
        try {
            // Get the population for the specified ZIP Code
            int population = populationDataProcessor.getPopulationByZipCode(zipCode);

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


    /**
     * Displays the average market value of properties across all properties in the area.
     */
    public double showAverageMarketValue() {
        // Sum the market values for all properties
        double totalMarketValue = propertyData.stream()
                .filter(property -> !Double.isNaN(property.getMarketValue()))
                .mapToDouble(Property::getMarketValue)
                .sum();

        // Count the total number of properties
        long totalNumberOfProperties = propertyData.stream()
                .filter(property -> !Double.isNaN(property.getMarketValue()))
                .count();

        // Calculate the average market value across all properties
        double averageMarketValue = totalNumberOfProperties > 0 ? totalMarketValue / totalNumberOfProperties : 0;

        // Display the result
        logger.logEvent("Average market value of properties: " + averageMarketValue);
        return averageMarketValue;
    }

}
