package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.DataReader;
import edu.upenn.cit594.datamanagement.Population;
import edu.upenn.cit594.logging.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopulationDataProcessor {
    Logger logger = Logger.getInstance();

    private final DataReader<Population> populationReader;
    private final String populationFilePath;
    private List<Population> populations;  // Cache for populations
    private Map<String, Integer> populationCache;  // Cache for population by ZIP Code

    public PopulationDataProcessor(DataReader<Population> populationReader, String populationFilePath) {
        this.populationReader = populationReader;
        this.populationFilePath = populationFilePath;
        this.populations = populationReader.readData(populationFilePath);
        initializeCache();
        logger.logEvent("Population data loaded from " + populationFilePath);
    }
    private void initializeCache() {
        populationCache = new HashMap<>();
        for (Population populationData : populations) {
            populationCache.put(populationData.getZipCode(), populationData.getPopulation());
        }
    }
    private List<Population> getPopulations() {
        return populations;
    }

    public int getTotalPopulation() {
        try {
            int totalPopulation = 0;

            for (Population populationData : getPopulations()) {
                totalPopulation += populationData.getPopulation();
            }

            return totalPopulation;
        } catch (Exception e) {
            System.out.println("Error: Unable to retrieve total population. Please make sure the population file is provided.");
            return 0;  // Return 0 or handle the error as needed
        }
    }

    public int getPopulationByZipCode(String zipCode) {
        try {
            // Check if the result is already in the cache
            if (populationCache.containsKey(zipCode)) {
                return populationCache.get(zipCode);
            }

            // If not, find the population for the specified ZIP Code
            for (Population populationData : getPopulations()) {
                if (populationData.getZipCode().equals(zipCode)) {
                    // Store the result in the cache and return
                    populationCache.put(zipCode, populationData.getPopulation());
                    return populationData.getPopulation();
                }
            }

            // If ZIP Code not found, return 0 or handle as needed
            return 0;
        } catch (Exception e) {
            System.out.println("Error: Unable to retrieve population for ZIP Code " + zipCode);
            return 0;  // Return 0 or handle the error as needed
        }
    }
}
