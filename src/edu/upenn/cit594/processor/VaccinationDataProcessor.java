package edu.upenn.cit594.processor;
import edu.upenn.cit594.datamanagement.CSVFormatException;
import edu.upenn.cit594.datamanagement.Vaccination;
import edu.upenn.cit594.datamanagement.DataReader;
import edu.upenn.cit594.logging.Logger;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Processes vaccination data and provides methods to show various vaccination statistics.
 */
public class VaccinationDataProcessor {
    Logger logger = Logger.getInstance();

    private PopulationDataProcessor populationDataProcessor;
    private DataReader<Vaccination> vaccinationDataReader;  // Updated to use DataReader interface
    String vaccinationFilePath;
    List<Vaccination> vaccinationData;
    private Map<String, Map<String, Double>> vaccinationsPerCapitaCache = new HashMap<>();


    /**
     * Constructor for VaccinationDataProcessor.
     *
     * @param vaccinationDataReader The data reader for vaccination data.
     * @param populationDataProcessor The processor for population data.
     * @param vaccinationFilePath The file path for vaccination data.
     */
    public VaccinationDataProcessor(DataReader<Vaccination> vaccinationDataReader, PopulationDataProcessor populationDataProcessor, String vaccinationFilePath) throws CSVFormatException, IOException {
        this.vaccinationDataReader = vaccinationDataReader;
        this.populationDataProcessor = populationDataProcessor;
        this.vaccinationFilePath = vaccinationFilePath;
        this.vaccinationData = vaccinationDataReader.readData(vaccinationFilePath);
        logger.logEvent("Vaccination data file: " + vaccinationFilePath);
    }


    /**
     * Displays vaccinations per capita for a specified date and vaccination type.
     *
     * @param scanner The scanner to read user input.
     */
    public void showVaccinationsPerCapita(Scanner scanner) {
            String vaccinationType;
            while (true) {
                vaccinationType = scanner.nextLine().trim(); // Trim to remove leading/trailing whitespaces
                logger.logEvent("Vaccination type: " + vaccinationType);
                if ("partial".equalsIgnoreCase(vaccinationType) || "full".equalsIgnoreCase(vaccinationType)) {
                    break; // Break the loop if the input is valid
                } else {
                    System.out.println("Enter the vaccination type (partial or full):");
                }
            }

            String dateStr;
            while (true) {
                System.out.println("Enter the date in the format YYYY-MM-DD:");
                dateStr = scanner.next().trim(); // Trim to remove leading/trailing whitespaces
                System.out.println(dateStr);
                logger.logEvent("Date: " + dateStr);

                Date specifiedDate = parseDate(dateStr);
                if (specifiedDate != null) {
                    Map<String, Map<String, Double>> vaccinationsPerCapita = calculateVaccinationsPerCapita(specifiedDate, vaccinationType);
                    displayVaccinationsPerCapita(vaccinationsPerCapita);
                    break; // Break the loop if the input is valid
                } else {
                    System.out.println("Invalid date format. Please enter a valid date in the format YYYY-MM-DD.");
                }
            }
    }


    /**
     * Parses a date string into a Date object.
     *
     * @param dateStr The date string to be parsed.
     * @return The parsed Date object or null if parsing fails.
     */
    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Calculates the vaccinations per capita for a specified date and vaccination type (full or partial).
     *
     * @param specifiedDate The date for which to calculate the statistics.
     * @param vaccinationType The type of vaccination (partial or full).
     * @return A map of ZIP codes to their vaccinations per capita.
     */
    private Map<String, Map<String, Double>> calculateVaccinationsPerCapita(Date specifiedDate, String vaccinationType) {
        String specifiedDateStr = new SimpleDateFormat("yyyy-MM-dd").format(specifiedDate);
        //if the result is already cached, return the cached result
        if (vaccinationsPerCapitaCache.containsKey(specifiedDateStr)) {
            if (vaccinationsPerCapitaCache.get(specifiedDateStr).containsKey(vaccinationType)) {
              return vaccinationsPerCapitaCache;

            }
        }
        Map<String, Map<String, Double>> result = vaccinationData.stream()
                .filter(v -> v.getEtlTimestamp().startsWith(specifiedDateStr) && v.matchesVaccinationType(vaccinationType))
                .collect(Collectors.groupingBy(
                        v -> v.getEtlTimestamp().substring(0, 10),
                        Collectors.toMap(
                                Vaccination::getZipCode,
                                v -> {
                                    double vaccinationCount = getVaccinationCount(v, vaccinationType);
                                    double population = getPopulation(v.getZipCode());
                                    double resultValue = 0.0;
                                    if (population > 0) {
                                        resultValue = vaccinationCount / population;
                                    }
                                    //Four decimal places
                                    logger.logEvent(String.format("Vaccinations per capita for %s on %s: %.4f", v.getZipCode(), v.getEtlTimestamp(), resultValue));
                                    return resultValue;
                                }
                        )
                ));

        // save result to cache
        vaccinationsPerCapitaCache.putAll(result);

        return result;
    }



    /**
     * Gets the vaccination count based on the vaccination type.
     *
     * @param v The Vaccination object.
     * @param vaccinationType The type of vaccination (partial or full).
     * @return The count of vaccinations.
     */
    private double getVaccinationCount(Vaccination v, String vaccinationType) {
        if ("partial".equalsIgnoreCase(vaccinationType)) {
            return v.getPartiallyVaccinated();
        } else if ("full".equalsIgnoreCase(vaccinationType)) {
            return v.getFullyVaccinated();
        }
        return 0.0;
    }


    /**
     * Gets the population for a given ZIP code.
     *
     * @param zipCode The ZIP code.
     * @return The population of the ZIP code.
     */
    private double getPopulation(String zipCode) {
        return populationDataProcessor.getPopulationByZipCode(zipCode);
    }


    /**
     * Displays the vaccinations per capita data.
     *
     * @param vaccinationsPerCapita The map of ZIP codes to their vaccinations per capita.
     */
    private void displayVaccinationsPerCapita(Map<String, Map<String, Double>> vaccinationsPerCapita) {
        System.out.println("BEGIN OUTPUT");

        // Check if there are any valid records for the provided date
        boolean dataFound = vaccinationsPerCapita.values().stream()
                .flatMap(map -> map.values().stream())
                .anyMatch(value -> value > 0);

        if (!dataFound) {
            System.out.println("0");
        } else {
            vaccinationsPerCapita.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(dateEntry -> {
                        Map<String, Double> zipToVaccinations = dateEntry.getValue();
                        zipToVaccinations.entrySet().stream()
                                .sorted(Map.Entry.comparingByKey())
                                .filter(zipEntry -> zipEntry.getValue() > 0)
                                .forEach(zipEntry -> {
                                    String zipCode = zipEntry.getKey();
                                    Double vaccinationsPerCapitaValue = zipEntry.getValue();
                                    System.out.printf("%s %.4f%n", zipCode, vaccinationsPerCapitaValue);
                                });
                    });
        }

        System.out.println("END OUTPUT");
    }


    /**
     * Displays the total number of fully vaccinated individuals by ZIP code.
     */
    public int showTotalFullyVaccinatedByZipCode() {

            // Group vaccinations by ZIP code and find the maximum "fully_vaccinated" count for each ZIP code
            Map<String, DoubleSummaryStatistics> summaryByZipCode = vaccinationData.stream()
                    .collect(Collectors.groupingBy(
                            Vaccination::getZipCode,
                            Collectors.summarizingDouble(Vaccination::getFullyVaccinated)
                    ));

            // Calculate the sum of maximum "fully_vaccinated" counts for all ZIP codes
            double totalFullyVaccinated = summaryByZipCode.values().stream()
                    .mapToDouble(DoubleSummaryStatistics::getMax)
                    .sum();

            // Display the result
//            System.out.println((int)totalFullyVaccinated);
            logger.logEvent("Total fully vaccinated: " + (int)totalFullyVaccinated);
        return (int)totalFullyVaccinated;
    }

}
