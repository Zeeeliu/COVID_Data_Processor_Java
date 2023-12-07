package edu.upenn.cit594.util;
import java.util.Scanner;

public class getParameter {

    //get the zip code from user
    public String getZipCode(Scanner scanner) {
        while (true){
            String zipCode = scanner.nextLine();
            if (zipCode.length() == 5) {
                return zipCode;
            } else {
                System.out.println( "Please enter a 5-digit zip code.");
            }
        }
        }
    }



