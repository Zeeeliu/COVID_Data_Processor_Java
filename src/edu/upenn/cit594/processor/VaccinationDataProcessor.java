package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.Vaccination;
import edu.upenn.cit594.datamanagement.DataReader;
import edu.upenn.cit594.logging.Logger;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class VaccinationDataProcessor {
    Logger logger = Logger.getInstance();

    private PopulationDataProcessor populationDataProcessor;
    private DataReader<Vaccination> vaccinationDataReader;  // Updated to use DataReader interface
    String vaccinationFilePath;
    List<Vaccination> vaccinationData;
    private Map<String, Map<String, Double>> vaccinationsPerCapitaCache = new HashMap<>();


    public VaccinationDataProcessor(DataReader<Vaccination> vaccinationDataReader, PopulationDataProcessor populationDataProcessor, String vaccinationFilePath) {
        this.vaccinationDataReader = vaccinationDataReader;
        this.populationDataProcessor = populationDataProcessor;
        this.vaccinationFilePath = vaccinationFilePath;
        this.vaccinationData = vaccinationDataReader.readData(vaccinationFilePath);
        logger.logEvent("Vaccination data file: " + vaccinationFilePath);
    }

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

    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

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

        // 将计算结果存入缓存
        vaccinationsPerCapitaCache.putAll(result);

        return result;
    }


    private double getVaccinationCount(Vaccination v, String vaccinationType) {
        if ("partial".equalsIgnoreCase(vaccinationType)) {
            return v.getPartiallyVaccinated();
        } else if ("full".equalsIgnoreCase(vaccinationType)) {
            return v.getFullyVaccinated();
        }
        return 0.0;
    }

    private double getPopulation(String zipCode) {
        // Add "" to zipcode
        zipCode = "\"" + zipCode + "\"";
        return populationDataProcessor.getPopulationByZipCode(zipCode);
    }

    private void displayVaccinationsPerCapita(Map<String, Map<String, Double>> vaccinationsPerCapita) {
        System.out.println("BEGIN OUTPUT");
        vaccinationsPerCapita.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(dateEntry -> {
                    Map<String, Double> zipToVaccinations = dateEntry.getValue();
                    zipToVaccinations.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey())
                            .forEach(zipEntry -> {
                                String zipCode = zipEntry.getKey();
                                Double vaccinationsPerCapitaValue = zipEntry.getValue();
                                System.out.printf("%s %.4f%n", zipCode, vaccinationsPerCapitaValue);
                                logger.logEvent("Vaccinations per capita for " + zipCode + " on " + dateEntry.getKey() + ": " + vaccinationsPerCapitaValue);
                            });
                });
        System.out.println("END OUTPUT");
    }

    public void showTotalFullyVaccinatedByZipCode() {

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
            System.out.println((int)totalFullyVaccinated);
            logger.logEvent("Total fully vaccinated: " + (int)totalFullyVaccinated);

    }
}
