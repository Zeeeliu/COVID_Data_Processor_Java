package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.Property;

import java.util.List;

/**
 * Calculator for computing the average total livable area of properties.
 */
public class AverageTotalLivableAreaCalculator implements PropertyAverageCalculator {
    @Override

    /**
     * Calculates the average total livable area of a list of properties.
     * Only considers properties with a valid (non-NaN) total livable area.
     *
     * @param properties The list of Property objects to calculate the average for.
     * @return The average total livable area of the provided properties, or 0.0 if there are no valid properties.
     */
    public double calculateAverage(List<Property> properties) {
        long count = properties.stream()
                .filter(property -> !Double.isNaN(property.getTotalLivableArea()))
                .count();

        if (count > 0) {
            double totalLivableArea = properties.stream()
                    .filter(property -> !Double.isNaN(property.getTotalLivableArea()))
                    .mapToDouble(Property::getTotalLivableArea)
                    .sum();
            return totalLivableArea / count;
        } else {
            return 0.0;
        }
    }
}