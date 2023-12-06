package edu.upenn.cit594.datamanagement;

/**
 * Represents a real estate property with details about its zip code, market value, and liveable area.
 */
public class Property {
    private double totalLivableArea;
    private double marketValue;
    private String zipCode;

    /**
     * Constructor for a new Property instance.
     *
     * @param totalLivableArea The total livable area of the property
     * @param marketValue      The market value of the property.
     * @param zipCode          The ZIP code where the property is located.
     */
    public Property(double totalLivableArea, double marketValue, String zipCode) {
        this.totalLivableArea = totalLivableArea;
        this.marketValue = marketValue;
        this.zipCode = zipCode;
    }

    // Getters and setters...
    public double getTotalLivableArea() {
        return totalLivableArea;
    }

    public void setTotalLivableArea(double totalLivableArea) {
        this.totalLivableArea = totalLivableArea;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "Property{" +
                "totalLivableArea=" + totalLivableArea +
                ", marketValue=" + marketValue +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
