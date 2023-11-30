package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.Property;

import java.util.List;

public class AverageTotalLivableAreaCalculator implements PropertyAverageCalculator {
    @Override
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