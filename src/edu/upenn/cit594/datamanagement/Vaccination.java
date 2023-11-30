package edu.upenn.cit594.datamanagement;

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


