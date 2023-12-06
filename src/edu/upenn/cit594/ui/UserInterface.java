package edu.upenn.cit594.ui;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.AverageMarketValueCalculator;
import edu.upenn.cit594.processor.AverageTotalLivableAreaCalculator;
import edu.upenn.cit594.processor.DataProcessor;

import java.util.Scanner;

/**
 * User interface for the application.
 */
public class UserInterface {
    Logger logger = Logger.getInstance();
    private DataProcessor dataProcessor;

    /**
     * Constructs a UserInterface with a specified DataProcessor.
     *
     * @param dataProcessor The data processor to handle user actions.
     */
    public UserInterface(DataProcessor dataProcessor) {
        this.dataProcessor = dataProcessor;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();
            System.out.print("> ");
            System.out.flush();
            int choice = readIntInput(scanner);
            System.out.println();
            logger.logEvent("User input: " + choice);
            switch (choice) {
                case 0:
                    System.out.println("BEGIN OUTPUT");
                    System.out.println("END OUTPUT");
                    return;
                case 1:
                    dataProcessor.showAvailableActions();
                    break;
                case 2:
                    dataProcessor.showTotalPopulation();
                    break;
                case 3:
                    dataProcessor.showVaccinationsPerCapita(scanner);
                    break;
                case 4:
                    dataProcessor.showAveragePropertyMetric(new AverageMarketValueCalculator(),scanner);
                    break;
                case 5:
                    dataProcessor.showAveragePropertyMetric(new AverageTotalLivableAreaCalculator(),scanner);
                    break;
                case 6:
                    dataProcessor.showTotalMarketValuePerCapita(scanner);
                    break;
                case 7:
                    dataProcessor.showTotalFullVaccinations();
                    break;
                default:
                    System.out.println("Error: Invalid choice. Please enter a number between 0 and 7.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("0. Exit the program.");
        System.out.println("1. Show the available actions.");
        System.out.println("2. Show the total population for all ZIP Codes.");
        System.out.println("3. Show the total vaccinations per capita for each ZIP Code for the specified date.");
        System.out.println("4. Show the average market value for properties in a specified ZIP Code.");
        System.out.println("5. Show the average total livable area for properties in a specified ZIP Code.");
        System.out.println("6. Show the total market value of properties, per capita, for a specified ZIP Code.");
        System.out.println("7. Show the total fully vaccinated across all ZIP codes");
    }


    /**
     * Reads an integer input from the user, and validate the input.
     *
     * @param scanner The scanner to read user input.
     * @return The valid integer input from the user.
     */
    private int readIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println(scanner.next());
            logger.logEvent("Error: Invalid input. Please enter a number.");
            System.out.println("Error: Invalid input. Please enter a number.");
            System.out.print("> ");
            System.out.flush();

            // Consume the entire line
            scanner.nextLine();
        }
        return  scanner.nextInt();
    }

}
