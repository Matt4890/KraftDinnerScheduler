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

        // Setup file params
        String filename = "./CourseSchedulerAI/inputs/shortExample.txt";
        ArrayList<String> fileLines = new ArrayList<String>();

        // Read in the file
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while (br.ready()) {
                fileLines.add(br.readLine());
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

        // Setup regular expressions
        Pattern slotRegex   = Pattern.compile("(MO|TU|FR),\\s*(\\d{1,2}:\\d{2}),\\s*(\\d+),\\s*(\\d+)");
        Pattern courseRegex = Pattern.compile("([A-Z]{4})\\s*(\\d{3})\\s*LEC\\s*(\\d{2})");
        Pattern labRegex    = Pattern.compile("([A-Z]{4})\\s*(\\d{3})\\s*(?:LAB|TUT)\\s*(\\d{2})");
        Pattern labLecRegex = Pattern.compile("([A-Z]{4})\\s*(\\d{3})\\s*LEC\\s*(\\d{2})\\s*(?:LAB|TUT)\\s*(\\d{2})");

        // Iterate through file lines

    }

}