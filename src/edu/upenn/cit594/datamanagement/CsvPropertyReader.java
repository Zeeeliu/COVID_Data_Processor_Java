package edu.upenn.cit594.datamanagement;
import edu.upenn.cit594.util.validateData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Reads property data from a CSV file and creates a list of Property objects.
 * The class implements DataReader interface for the Property data type.
 *
 */
public class CsvPropertyReader implements DataReader<Property> {
    /**
     * Reads property data from the specified CSV file.
     *
     * @param fileName The name of the file to read from.
     * @return A list of Property objects representing the data in the file.
     */
    @Override
    public List<Property> readData(String fileName) {
        List<Property> properties = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(fileName)) {

            Map<String, String> row;
            while ((row = csvReader.readRowAsDict()) != null) {
                String totalLivableAreaStr = row.get("total_livable_area").trim();
                String marketValueStr = row.get("market_value").trim();
                String zipCode = extractFirstFiveDigits(row.get("zip_code").trim());

                try {
                    double totalLivableArea = validateData.isValidNumeric(totalLivableAreaStr) ?
                            validateData.parseDoubleOrFlag(totalLivableAreaStr) : Double.NaN;
                    double marketValue = validateData.isValidNumeric(marketValueStr) ?
                            Double.parseDouble(marketValueStr) : Double.NaN;

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

    /**
     * Extracts the first five digits of a ZIP code string.
     * If the first five characters are not all numeric, it returns an empty string.
     *
     * @param zipCode The ZIP code string to process.
     * @return The first five digits of the ZIP code, or an empty string if invalid.
     */
    private String extractFirstFiveDigits(String zipCode) {
        if (validateData.isValidZipCode(zipCode)) {
            // Extract first 5 characters and ensure they are all numeric
            String firstFiveDigits = zipCode.substring(0, Math.min(5, zipCode.length()));
            if (firstFiveDigits.matches("\\d+")) {
                return firstFiveDigits;
            }
        }
        return "";
    }
}
