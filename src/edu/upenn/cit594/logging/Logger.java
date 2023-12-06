package edu.upenn.cit594.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger class following the Singleton pattern to handle logging throughout the application.
 * This logger can write to a file or standard error, and it appends a timestamp to each log entry.
 */
public class Logger {
    private static Logger instance;
    private PrintWriter writer;
    private boolean isSystemErr; // Flag to indicate if the writer is set to System.err when log file name was not specified

    /**
     * Private constructor to prevent direct instantiation and enforce Singleton pattern.
     */
    private Logger() {
    }

    /**
     * Gets the single instance of the Logger.
     * If the instance does not exist, it is created.
     *
     * @return The singleton Logger instance.
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    /**
     * Sets or changes the output destination for the logger.
     * If the log file name was not specified in the runtime arguments then the logger should write to standard error.
     * If changing from a file to another destination, the method closes the current file writer.
     *
     * @param fileName The name of the file to write logs to, or null to log to System.err.
     */
    public void setOutputFile(String fileName) {
        try {
            // Close the existing writer, if any
            if (writer != null) {
                writer.close();
            }

            // Create a new FileWriter with append mode
            if (fileName != null && !fileName.isEmpty()) {
                writer = new PrintWriter(new FileWriter(fileName, true));
            } else {
                writer = new PrintWriter(new OutputStreamWriter(System.err));
                isSystemErr = true;
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot set the output file for logger", e);
        }
    }

    /**
     * Logs an event with the current timestamp.
     *
     * @param message The message to be logged.
     */
    public void logEvent(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logEntry = timestamp + " " + message;

        // Log the event to the console and the file
        if (writer != null) {
            writer.println(logEntry);
            writer.flush();
        }
    }

    /**
     * Closes the logger's writer to release resources.
     * If the current writer is System.err, it is not closed.
     */
    public void closeLogger() {
        // Close the writer when the program ends
        if (writer != null && !isSystemErr) {
            writer.close();
        }
    }
}
