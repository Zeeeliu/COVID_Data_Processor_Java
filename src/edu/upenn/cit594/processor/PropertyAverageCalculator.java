package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.Property;

import java.util.List;

/**
 * Interface for calculating the average value of a specific metric for a list of properties.
 */
public interface PropertyAverageCalculator {
    /**
     * Calculates the average value for a given metric from a list of Property objects.
     * The specific metric to be averaged is determined by the implementing class.
     *
     * @param properties The list of Property objects to be used for the calculation.
     * @return The average value of the chosen metric for the given properties.
     */
    double calculateAverage(List<Property> properties);
}
