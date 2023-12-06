package edu.upenn.cit594.datamanagement;

/**
 * The class {@code CSVFormatException} is a {@code Exception} to indicate a
 * syntax error occurred while processing a CSV file.
 *
 * This exception contains informational fields to help track down the invalid
 * section of the input and position in the processed output around the fault.
 *
 */
public class CSVFormatException extends Exception {
    private final int line;

    public CSVFormatException(String message, int line) {
        super((message == null ? "" : message + ": ") + "error at " + line);
        this.line = line;
    }
    public CSVFormatException(String message) {
        super((message == null ? "" : message));
        this.line = -1; // default value indicating 'line' is not set

    }
    @Override
    public boolean equals(Object o) {
        return o instanceof CSVFormatException;
    }

    public int hashCode() {
        return java.util.Objects.hash(line);
    }
}
