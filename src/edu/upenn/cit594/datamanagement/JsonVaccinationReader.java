package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.util.validateData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Read vaccination data from a JSON file.
 * The class implements DataReader interface for the Vaccination data type.
 */
public class JsonVaccinationReader implements DataReader<Vaccination> {

    /**
     * Reads vaccination data from a JSON file. Any incomplete record, invalid zip code, or invalid timestamp
     * will be skipped.
     *
     * @param fileName The name of the JSON file to read from.
     * @return A list of Vaccination objects parsed from the file.
     */
    @Override
    public List<Vaccination> readData(String fileName) {
        List<Vaccination> vaccinations = new ArrayList<>();

        try (FileReader reader = new FileReader(fileName)) {
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            Iterator<JSONObject> iterator = jsonArray.iterator();
            while (iterator.hasNext()) {
                JSONObject jsonObject = iterator.next();

                String zipCode = String.valueOf(jsonObject.get("zip_code"));
                if (!validateData.isValidZipCode(zipCode)) {
                    continue;  // Skip invalid ZIP Code
                }

                int neg = validateData.parseIntOrZero(jsonObject.get("NEG"));
                int pos = validateData.parseIntOrZero(jsonObject.get("POS"));
                int deaths = validateData.parseIntOrZero(jsonObject.get("deaths"));
                int hospitalized = validateData.parseIntOrZero(jsonObject.get("hospitalized"));
                int partiallyVaccinated = validateData.parseIntOrZero(jsonObject.get("partially_vaccinated"));
                int fullyVaccinated = validateData.parseIntOrZero(jsonObject.get("fully_vaccinated"));
                int boosted = validateData.parseIntOrZero(jsonObject.get("boosted"));
                String etlTimestamp = (String) jsonObject.get("etl_timestamp");
                if (!validateData.isValidTimestamp(etlTimestamp)) {
                    continue;  // Skip invalid timestamp
                }

                Vaccination vaccination = new Vaccination(zipCode, neg, pos, deaths, hospitalized,
                        partiallyVaccinated, fullyVaccinated, boosted, etlTimestamp);
                vaccinations.add(vaccination);
            }
        } catch (Exception e) {
            System.err.println("Error reading vaccination data from JSON file " + fileName + ": " + e.getMessage());
        }

        return vaccinations;
    }
}
