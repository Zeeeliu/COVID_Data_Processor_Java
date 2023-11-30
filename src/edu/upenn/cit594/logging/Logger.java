package edu.upenn.cit594.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static Logger instance;
    private PrintWriter writer;

    private Logger() {
        // Private constructor to enforce Singleton pattern
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

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
                // If no fileName is provided, use console output
                writer = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logEvent(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logEntry = timestamp + " " + message;

        // Log the event to the console and the file
        if (writer != null) {
            writer.println(logEntry);
            writer.flush();
        }
    }

    public void closeLogger() {
        // Close the writer when the program ends
        if (writer != null) {
            writer.close();
        }
    }
}
