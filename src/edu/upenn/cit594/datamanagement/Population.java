package edu.upenn.cit594.datamanagement;

/**
 * This class represents the population data of a Philadelphia’s ZIP Codes.
 * It stores the ZIP code and the corresponding population count.
 */
public class Population {
    private String zipCode;
    private int population;

    /**
     * Constructor for a new Population instance.
     *
     * @param zipCode    The ZIP code associated with the population data.
     * @param population The population count of the ZIP code area.
     */
    public Population(String zipCode, int population) {
        this.zipCode = zipCode;
        this.population = population;
    }

    public String getZipCode() {
        return zipCode;
    }

    public int getPopulation() {
        return population;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return "Population{" +
                "zipCode='" + zipCode + '\'' +
                ", population=" + population +
                '}';
    }
}
