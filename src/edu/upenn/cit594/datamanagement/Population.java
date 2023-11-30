package edu.upenn.cit594.datamanagement;

public class Population {
    private String zipCode;
    private int population;

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
