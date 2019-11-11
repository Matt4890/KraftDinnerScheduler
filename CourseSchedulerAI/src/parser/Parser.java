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
        String filename = "./inputs/shortExample.txt";
        ArrayList<String> fileLines = new ArrayList<String>();
        int slotCount = 0;

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

        // Get sections from input file
        String fileStr = String.join("\n", fileLines).toUpperCase();
        String[] courseSlots    = getSection("COURSE SLOTS", fileStr);
        String[] labSlots       = getSection("LAB SLOTS", fileStr);
        String[] courses        = getSection("COURSES", fileStr);
        String[] labs           = getSection("LABS", fileStr);
        String[] notCompat      = getSection("NOT COMPATIBLE", fileStr);
        String[] unwanted       = getSection("UNWANTED", fileStr);
        String[] preferences    = getSection("PREFERENCES", fileStr);
        String[] pairs          = getSection("PAIR", fileStr);
        String[] partialAssign  = getSection("PARTIAL ASSIGNMENTS", fileStr);

    }

    private static String[] getSection(String label, String fileStr) {

        Pattern regex = Pattern.compile(label + "\\s*:\\s*((?:.|\n)*?)(?:\n\n|$)");
        Matcher matcher = regex.matcher(fileStr);
        if (matcher.find()) {
            return matcher.group(1).split("\n");
        } else {
            return new String[0];
        }

    }

}