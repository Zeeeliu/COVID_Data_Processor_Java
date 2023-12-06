package edu.upenn.cit594.datamanagement;
import edu.upenn.cit594.util.validateData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Reads population data from a CSV file and creates a list of Population objects.
 */
public class PopulationFileReader implements DataReader<Population> {

    /**
     * Reads population data from the specified CSV file.
     * Records will be skipped if the ZIP Code is not exactly 5 digits or the population figure is not an integer.
     *
     * @param fileName The name of the file to read from.
     * @return A list of Population objects representing the data in the file.
     */
    @Override
    public List<Population> readData(String fileName) {
        List<Population> populations = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(fileName)) {
            Map<String, String> row;
            while ((row = csvReader.readRowAsDict()) != null) {
                try {
                    int population = validateData.parseIntOrZero(row.get("population").trim());
                    String zipCode = row.get("zip_code").trim();

                    if (validateData.isValidZipCode(zipCode) && validateData.isPopulationValid(population)) {
                        Population populationData = new Population(zipCode, population);
                        populations.add(populationData);
                    } else {
                        System.out.println("Invalid data - zipCode: " + zipCode + ", population: " + population);
                    }
                } catch (Exception e) {
                    System.err.println("Error reading population data: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error opening CSV file: " + e.getMessage());
        }
        return populations;
    }
}
