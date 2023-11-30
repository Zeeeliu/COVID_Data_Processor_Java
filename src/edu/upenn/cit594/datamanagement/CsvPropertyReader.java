package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvPropertyReader implements DataReader<Property> {
    Logger logger = Logger.getInstance();
    @Override
    public List<Property> readData(String fileName) {
        List<Property> properties = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            br.readLine(); // Skip header line
            String line;
            while ((line = br.readLine()) != null) {
                String[] nextRecord = line.split(",");
                if (nextRecord.length < 3) {
                    continue;  // Skip incomplete records
                }

                String totalLivableAreaStr = nextRecord[0].trim();
                String marketValueStr = nextRecord[1].trim();
                String zipCode = extractFirstFiveDigits(nextRecord[2].trim());

                try {
                    double totalLivableArea = isValidNumeric(totalLivableAreaStr) ? parseDoubleOrFlag(totalLivableAreaStr) : Double.NaN;
                    double marketValue = isValidNumeric(marketValueStr) ? Double.parseDouble(marketValueStr) : Double.NaN;

                    Property property = new Property(totalLivableArea, marketValue, zipCode);
                    properties.add(property);
                } catch (NumberFormatException e) {
                    // Handle number format exception, e.g., if parsing totalLivableArea or marketValue fails
                    System.err.println("Error parsing numeric value in property data: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading property data: " + e.getMessage());
        }

        return properties;
    }

    private String extractFirstFiveDigits(String zipCode) {
        if (isValidZipCode(zipCode)) {
            // Extract first 5 characters and ensure they are all numeric
            String firstFiveDigits = zipCode.substring(0, Math.min(5, zipCode.length()));
            if (firstFiveDigits.matches("\\d+")) {
                return firstFiveDigits;
            }
        }
        return "";
    }
    private boolean isValid(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private boolean isValidZipCode(String zipCode) {
        return zipCode != null && zipCode.length() >= 5 && zipCode.matches("\\d+");
    }

    private boolean isValidNumeric(String value) {
        return isValid(value) && value.matches("-?\\d+(\\.\\d+)?");
    }

    private double parseDoubleOrZero(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private double parseDoubleOrFlag(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return Double.NaN;  // Set to a flag value for non-numeric
        }
    }
}
