package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.Property;

import java.util.List;

public class AverageMarketValueCalculator implements PropertyAverageCalculator {
    @Override
    public double calculateAverage(List<Property> properties) {
        long count = properties.stream()
                .filter(property -> !Double.isNaN(property.getMarketValue()))
                .count();

        if (count > 0) {
            double totalMarketValue = properties.stream()
                    .filter(property -> !Double.isNaN(property.getMarketValue()))
                    .mapToDouble(Property::getMarketValue)
                    .sum();
            return totalMarketValue / count;
        } else {
            return 0.0;
        }
    }
}
