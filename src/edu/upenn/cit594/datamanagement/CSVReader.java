package edu.upenn.cit594.datamanagement;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * CSV reader to read individual CSV rows
 * as arrays of strings that have been read from a given CSV file.
 */
public class CSVReader implements AutoCloseable {
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 5130409650040L;
    private final BufferedReader reader;
    private String[] headers = new String[0];
    public CSVReader(String filename) throws IOException {
        this.reader = Files.newBufferedReader(Paths.get(filename));
        try {
            this.headers = readRow();
        } catch (CSVFormatException | IOException e) {
            System.out.println(e);
        }
        if (this.headers == null) {
            throw new IOException("CSV file is empty or headers are missing");
        }
    }


    /**
     * Reads the next row from the CSV file and returns it as an array of strings.
     *
     * @return a single row of CSV represented as a string array, where each
     *         element of the array is a field of the row; or {@code null} when
     *         there are no more rows left to be read.
     * @throws IOException when the underlying reader encountered an error
     * @throws CSVFormatException when the CSV file is formatted incorrectly
     */
    // enum for the state machine
    private enum State {
        START_FIELD,        // state when starting to parse a new field
        UNQUOTED_FIELD,     // state when in the middle of an unquoted field
        QUOTED_FIELD,       // state when in the middle of a quoted field
        END_QUOTED_FIELD,      // state immediately after a closing quote in a field
        LAST_WAS_CR     // state to handle \r
    }

    public String[] readRow() throws IOException, CSVFormatException {
        // initialize a list to hold individual fields found in the current row
        List<String> values = new ArrayList<>();

        // Leverage StringBuilder to create a mutable character sequence
        StringBuilder currentField = new StringBuilder();

        // set the initial state, indicating the start of a field
        State state = State.START_FIELD;

        // variable to track if we are inside of a quoted field
        boolean inQuotedField = false;

        // initialize the variables to track where we are for better error tracking
        int currentChar;    // hold the current character read from the CSV file
        int line = 1; // line counter, initialized as 1


        // While loop to enable continuous reading until it reached the end of the file
        while ((currentChar = reader.read()) != -1) {

            // read the character and increment the column counter for each character read
            char c = (char) currentChar;

            // switch statement based on the current state of the parser
            switch(state) {
                //the start of a new field
                case START_FIELD:
                    // Quote starts a quoted field
                    if (c == '\"') {
                        state = State.QUOTED_FIELD;
                        inQuotedField = true;

                        // comma signifies end of field, save the field and reset the builder
                    } else if (c == ',') {
                        values.add(currentField.toString());
                        currentField.setLength(0);

                        // newline character indicates the end of row
                    } else if (c == '\n' || c == '\r') {
                        if (values.size() != 0 || currentField.length() != 0) {
                            // add the last field and prepare to return the record
                            values.add(currentField.toString());
                            return values.toArray(new String[0]);
                        }

                        // reset the counter to get ready for new row
                        line++;


                        if (c == '\r') {
                            state = State.LAST_WAS_CR;
                        }

                        // handling leading and trailing spaces and tabs so they are added to the field
                    } else if (c == ' ' || c == '\t') {
                        currentField.append(c);

                        // All the other cases indicate that it is a regular character, and starts an unquoted field
                    } else {
                        currentField.append(c);
                        state = State.UNQUOTED_FIELD;
                    }
                    break;

                // within a non-quoted field
                case UNQUOTED_FIELD:
                    // commas ends the current field. save it and reset the field
                    if (c == ',') {
                        values.add(currentField.toString());
                        currentField.setLength(0);
                        // prepare for the next field
                        state = State.START_FIELD;
                        // increment the field

                    } else if (c == '"') {
                        throw new CSVFormatException("Unescaped Double Quotes in Fields", line);
                    }

                    // newline character indicates the end of row
                    else if ((c == '\n') || (c== '\r')) {
                        // check if it's not just an empty line or the filed is not empty, this indicates that we do
                        // have data to save for the current row
                        if (values.size() != 0 || currentField.length() != 0) {
                            // add the last field and prepare to return the record
                            values.add(currentField.toString());
                            return values.toArray(new String[0]);
                        }
                        // now this is a new line, increment the line and row counters
                        line++;


                        // other characters are part of the current field
                    } else { currentField.append(c); }
                    break;

                // within a quoted field
                case QUOTED_FIELD:
                    // A second quote indicate the end of the quoted field, or an escaped quote (""), dependent on the
                    // character. need to check the next character to decide. Therefore, first transition to the
                    // END_QUOTED_FIELD
                    if (c == '\"') {
                        state = State.END_QUOTED_FIELD;
                        inQuotedField = false;

                        // if we encounter any characters other than a quote. Just add to the field.
                    } else {
                        currentField.append(c);
                    }
                    break;

                // checking character immediately after a closing quote
                case END_QUOTED_FIELD:
                    if (c == '\"') {
                        // "" are escaped
                        currentField.append('\"');
                        // still in quoted field
                        state = State.QUOTED_FIELD;

                        // Comma ends the field, save it, and reset the field
                    } else if (c == ',') {
                        values.add(currentField.toString());
                        currentField.setLength(0);
                        // Prepare for the next field.
                        state = State.START_FIELD;

                        // newline character indicates the end of row
                    } else if ((c == '\n') || (c== '\r')) {
                        // add the last field and prepare to return the record
                    values.add(currentField.toString());
                    return values.toArray(new String[0]);

                    // Unexpected character (not whitespace) outside of quotes is an error.//
                } else if (c != ' ' && c != '\t') {
                    throw new CSVFormatException("Unexpected character after quoted field", line);
                }break;

                case LAST_WAS_CR:
                    if (c == '\n') {
                        state = State.START_FIELD;
                    } else {
                        // The '\r' was not followed by '\n', which is an error outside of quotes
                        if (!inQuotedField) {
                            throw new CSVFormatException("Carriage return must be followed by newline outside quoted field", line);
                        }
                        state = State.UNQUOTED_FIELD; // If we're in a quoted field, we revert to the unquoted state.
                    }
                    break;
            }
        }

        // At the end of the file, check for a field or line that hasn't been closed properly.
        if (inQuotedField) {
            throw new CSVFormatException("Line ended with an unfinished quoted field", line);
        }

        if (state == State.LAST_WAS_CR) {
            throw new CSVFormatException("File ended with a carriage return character", line);
        }

        // If there's data in currentField, a field is still open, so we add it.
        if (currentField.length() > 0 || state == State.END_QUOTED_FIELD) {
            values.add(currentField.toString());
            return values.toArray(new String[0]);
        }

        // If the row was not empty, we return it, otherwise return null indicating EOF.
        if (!values.isEmpty()) {
            return values.toArray(new String[0]);
        }

        // If EOF is reached and no data is left to process, return null.
        return null;
    }

    public Map<String, String> readRowAsDict() throws IOException, CSVFormatException {
        String[] rowData = readRow();
        if (rowData == null) {
            return null; // Return null if end of file is reached
        }

        if (rowData.length != headers.length) {
            throw new CSVFormatException("Data row does not match header length");
        }

        Map<String, String> rowMap = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            rowMap.put(headers[i], rowData[i]);
        }

        return rowMap;
    }
    @Override
    public void close() throws IOException {
        this.reader.close();
    }
}
