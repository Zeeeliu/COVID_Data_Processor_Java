package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class PopulationFileReader implements DataReader<Population> {
    @Override
    public List<Population> readData(String fileName) {
        List<Population> populations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String zipCode = parts[0].trim();
                    int population = parseIntOrZero(parts[1].trim());

                    if (isValidZipCode(zipCode) && isValidPopulation(population)) {
                        Population populationData = new Population(zipCode, population);
                        populations.add(populationData);
                    }
                    else {
                        System.out.println("Invalid data - zipCode: " + zipCode + ", population: " + population);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading population data: " + e.getMessage());
        }

        return populations;
    }

    private int parseIntOrZero(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean isValid(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private boolean isValidZipCode(String zipCode) {
        if (isValid(zipCode)) {
            zipCode = zipCode.replaceAll("\"", "").trim();
            return zipCode.matches("\\d{5}");
        }
        return false;
    }

    private boolean isValidPopulation(int population) {
        return population >= 0;  // Assuming population cannot be negative
    }
}
