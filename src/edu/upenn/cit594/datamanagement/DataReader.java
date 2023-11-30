package edu.upenn.cit594.datamanagement;

import java.util.List;

public interface DataReader<T> {
    List<T> readData(String fileName);
}
