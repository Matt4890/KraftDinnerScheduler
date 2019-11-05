package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Parser {

    public static void main(String[] args) {

        String filename = "./CourseSchedulerAI/inputs/shortExample.txt";
        ArrayList<String> lines = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while (br.ready()) {
                lines.add(br.readLine());
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found: " + filename);
            System.out.println("Exiting...");
            System.exit(1);
        } catch (IOException ioe) {
            System.out.println("IO exception while reading file:" + filename);
            System.out.println("Exiting...");
            System.exit(1);
        }

    }

}