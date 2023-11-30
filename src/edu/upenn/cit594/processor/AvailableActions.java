package edu.upenn.cit594.processor;

public enum AvailableActions {
    EXIT(0),
    SHOW_AVAILABLE_ACTIONS(1),
    SHOW_TOTAL_POPULATION(2,"population"),
    SHOW_VACCINATIONS_PER_CAPITA(3, "population", "covid"),
    SHOW_AVERAGE_MARKET_VALUE(4, "properties"),
    SHOW_AVERAGE_TOTAL_LIVABLE_AREA(5, "properties"),
    SHOW_TOTAL_MARKET_VALUE_PER_CAPITA(6, "properties", "population"),
    SHOW_CUSTOM_FEATURE(7, "covid");

    private final int actionNumber;
    private final String[] requiredParameters;

    AvailableActions(int actionNumber, String... requiredParameters) {
        this.actionNumber = actionNumber;
        this.requiredParameters = requiredParameters;
    }

    public int getActionNumber() {
        return actionNumber;
    }

    public String[] getRequiredParameters() {
        return requiredParameters;
    }
}
