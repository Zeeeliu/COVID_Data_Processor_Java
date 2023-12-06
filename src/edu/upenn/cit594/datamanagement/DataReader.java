package edu.upenn.cit594.datamanagement;

import java.io.IOException;
import java.util.List;

/**
 * Interface for data reader classes.
 *
 * @param <T> The generic type of data that is read and processed. This generic type allows the implementation
 *           of this interface to be flexible for different types of data (JSON and CSV).
 */
public interface DataReader<T> {

    /**
     * Reads data from a specified file (JSON or CSV) and returns a list of data objects of type T.
     *
     * @param fileName The name of the file to read from.
     *
     * @return A List of objects of type T, representing the data read from the file.
     */
    List<T> readData(String fileName) throws CSVFormatException, IOException;
}
