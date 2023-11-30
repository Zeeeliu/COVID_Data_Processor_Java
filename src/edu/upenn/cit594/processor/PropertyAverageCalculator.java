package edu.upenn.cit594.processor;

import edu.upenn.cit594.datamanagement.Property;

import java.util.List;

public interface PropertyAverageCalculator {
    double calculateAverage(List<Property> properties);
}
