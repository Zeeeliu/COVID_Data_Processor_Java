package edu.upenn.cit594.datamanagement;

/**
 * The Vaccination class represents a record of vaccination data.
 * It stores information about COVID-19 vaccinations for a specific ZIP code and date.
 */
public class Vaccination {
    private String zipCode;
    private int neg;
    private int pos;
    private int deaths;
    private int hospitalized;
    private int partiallyVaccinated;
    private int fullyVaccinated;
    private int boosted;
    private String etlTimestamp;

    /**
     * Constructor for a new Vaccination instance.
     *
     * @param zipCode             The ZIP Code where the vaccinations were provided.
     * @param neg                 The number of negative test results.
     * @param pos                 The number of positive test results.
     * @param deaths              The number of deaths.
     * @param hospitalized        The number of hospitalized individuals.
     * @param partiallyVaccinated The total number of persons who have received their first dose in the ZIP Code but not their
     *                            second dose (“partially vaccinated”), as of the reporting date
     * @param fullyVaccinated     The total number of persons who have received their second dose (“fully vaccinated”) in the
     *                            ZIP Code, as of the reporting date
     * @param boosted             The number of individuals who have received booster doses.
     * @param etlTimestamp        The timestamp at which the vaccination data for that ZIP Code were reported, in “YYYY-
     *                            MM-DD hh:mm:ss” format.
     */
    public Vaccination(String zipCode, int neg, int pos, int deaths, int hospitalized,
                       int partiallyVaccinated, int fullyVaccinated, int boosted, String etlTimestamp) {
        this.zipCode = zipCode;
        this.neg = neg;
        this.pos = pos;
        this.deaths = deaths;
        this.hospitalized = hospitalized;
        this.partiallyVaccinated = partiallyVaccinated;
        this.fullyVaccinated = fullyVaccinated;
        this.boosted = boosted;
        this.etlTimestamp = etlTimestamp;
    }

    // Getters and setters (if needed) go here

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getNeg() {
        return neg;
    }

    public void setNeg(int neg) {
        this.neg = neg;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getHospitalized() {
        return hospitalized;
    }

    public void setHospitalized(int hospitalized) {
        this.hospitalized = hospitalized;
    }

    public int getPartiallyVaccinated() {
        return partiallyVaccinated;
    }

    public void setPartiallyVaccinated(int partiallyVaccinated) {
        this.partiallyVaccinated = partiallyVaccinated;
    }

    public int getFullyVaccinated() {
        return fullyVaccinated;
    }

    public void setFullyVaccinated(int fullyVaccinated) {
        this.fullyVaccinated = fullyVaccinated;
    }

    public int getBoosted() {
        return boosted;
    }

    public void setBoosted(int boosted) {
        this.boosted = boosted;
    }

    public String getEtlTimestamp() {
        return etlTimestamp;
    }

    public void setEtlTimestamp(String etlTimestamp) {
        this.etlTimestamp = etlTimestamp;
    }


    /**
     * Returns a string representation of the Vaccination object.
     *
     * @return A string detailing the vaccination data.
     */
    @Override
    public String toString() {
        return "Vaccination{" +
                "zipCode='" + zipCode + '\'' +
                ", neg=" + neg +
                ", pos=" + pos +
                ", deaths=" + deaths +
                ", hospitalized=" + hospitalized +
                ", partiallyVaccinated=" + partiallyVaccinated +
                ", fullyVaccinated=" + fullyVaccinated +
                ", boosted=" + boosted +
                ", etlTimestamp='" + etlTimestamp + '\'' +
                '}';
    }


    /**
     * Checks if the vaccination data matches a specified vaccination type.
     *
     * @param vaccinationType The type of vaccination to check (partial or full).
     * @return true if the vaccination data matches the specified type, false otherwise.
     */
    public boolean matchesVaccinationType(String vaccinationType) {
        switch (vaccinationType.toLowerCase()) {
            case "partial":
            case "full":
                return true;
            default:
                return false;
        }
    }
}


