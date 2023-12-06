package edu.upenn.cit594.datamanagement;
import edu.upenn.cit594.util.validateData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Reads vaccination data from a CSV file.
 * The class implements DataReader interface for the Vaccination data type.
 */
public class CsvVaccinationReader implements DataReader<Vaccination> {

    /**
     * Reads vaccination data from a CSV file. Any incomplete record, invalid zip code, or invalid timestamp
     * will be skipped.
     *
     * @param fileName The name of the CSV file to read from.
     * @return A list of Vaccination objects parsed from the file.
     */
    @Override
    public List<Vaccination> readData(String fileName) throws CSVFormatException, IOException {
        List<Vaccination> vaccinations = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(fileName)) {

            Map<String, String> row;
            while ((row = csvReader.readRowAsDict()) != null) {
                try {
                    int neg = validateData.parseIntOrZero(row.get("NEG").trim());
                    int pos = validateData.parseIntOrZero(row.get("POS").trim());
                    int deaths = validateData.parseIntOrZero(row.get("deaths").trim());
                    int hospitalized = validateData.parseIntOrZero(row.get("hospitalized").trim());
                    int partiallyVaccinated = validateData.parseIntOrZero(row.get("partially_vaccinated").trim());
                    int fullyVaccinated = validateData.parseIntOrZero(row.get("fully_vaccinated").trim());
                    int boosted = validateData.parseIntOrZero(row.get("boosted").trim());

                    String zipCode = row.get("zip_code").trim();
                    if (! validateData.isValidZipCode(zipCode)) {
                        continue; // skip invalid zip code
                    }

                    String etlTimestamp = row.get("etl_timestamp").trim();
                    etlTimestamp = etlTimestamp.replace("\"", "");
                    if (!validateData.isValidTimestamp(etlTimestamp)) {
                        continue;  // Skip invalid timestamp
                    }

                    Vaccination vaccination = new Vaccination(zipCode, neg, pos, deaths, hospitalized,
                            partiallyVaccinated, fullyVaccinated, boosted, etlTimestamp);
                    vaccinations.add(vaccination);
                } catch (Exception e) {
                    System.err.println("Error reading vaccination data from CSV file " + fileName + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading vaccination data: " + e.getMessage());
        }
        return vaccinations;
    }
}
