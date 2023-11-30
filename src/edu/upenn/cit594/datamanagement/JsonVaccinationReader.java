package edu.upenn.cit594.datamanagement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonVaccinationReader implements DataReader<Vaccination> {

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
                if (!isValidZipCode(zipCode)) {
                    continue;  // Skip invalid ZIP Code
                }

                int neg = parseIntOrZero(jsonObject.get("NEG"));
                int pos = parseIntOrZero(jsonObject.get("POS"));
                int deaths = parseIntOrZero(jsonObject.get("deaths"));
                int hospitalized = parseIntOrZero(jsonObject.get("hospitalized"));
                int partiallyVaccinated = parseIntOrZero(jsonObject.get("partially_vaccinated"));
                int fullyVaccinated = parseIntOrZero(jsonObject.get("fully_vaccinated"));
                int boosted = parseIntOrZero(jsonObject.get("boosted"));
                String etlTimestamp = (String) jsonObject.get("etl_timestamp");
                if (!isValidTimestamp(etlTimestamp)) {
                    continue;  // Skip invalid timestamp
                }

                Vaccination vaccination = new Vaccination(zipCode, neg, pos, deaths, hospitalized,
                        partiallyVaccinated, fullyVaccinated, boosted, etlTimestamp);
                vaccinations.add(vaccination);
            }
        } catch (Exception e) {
            System.err.println("Error reading vaccination data: " + e.getMessage());
        }

        return vaccinations;
    }

    private int parseIntOrZero(Object value) {
        try {
            if (value != null) {
                return Integer.parseInt(value.toString());
            } else {
                return 0;
            }
        } catch (NumberFormatException e) {
            return 0;
        }
    }


    private boolean isValidZipCode(String zipCode) {
        return zipCode.matches("\\d{5}");
    }

    private boolean isValidTimestamp(String timestamp) {
        try {
            timestamp = timestamp.replace("\"", "");
            // 使用SimpleDateFormat验证时间戳格式
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
