package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CsvVaccinationReader implements DataReader<Vaccination> {
    @Override
    public List<Vaccination> readData(String fileName) {
        List<Vaccination> vaccinations = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] nextRecord = line.split(",");
                if (nextRecord.length < 9) {
                    continue;  // Skip incomplete records
                }

                String zipCode = nextRecord[0].trim();
                if (!isValidZipCode(zipCode)) {
                    continue;  // Skip invalid ZIP Code
                }

                int neg = parseIntOrZero(nextRecord[1]);
                int pos = parseIntOrZero(nextRecord[2]);
                int deaths = parseIntOrZero(nextRecord[3]);
                int hospitalized = parseIntOrZero(nextRecord[4]);
                int partiallyVaccinated = parseIntOrZero(nextRecord[5]);
                int fullyVaccinated = parseIntOrZero(nextRecord[6]);
                int boosted = parseIntOrZero(nextRecord[7]);

                String etlTimestamp = nextRecord[8].trim();
                etlTimestamp = etlTimestamp.replace("\"", "");
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

    private int parseIntOrZero(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private boolean isValidZipCode(String zipCode) {
        return zipCode.matches("\\d{5}");
    }

    private boolean isValidTimestamp(String timestamp) {
        try {
            // Remove double quotes from the timestamp
            timestamp = timestamp.replace("\"", "");
            // Use SimpleDateFormat to parse the timestamp
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
