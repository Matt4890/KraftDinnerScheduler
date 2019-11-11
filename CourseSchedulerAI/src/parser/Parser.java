package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import coursesULabs.*;
import schedule.*;

class Parser {

    public static void main(String[] args) {

        // Setup file params
        String filename = "./inputs/shortExample.txt";
        ArrayList<String> fileLines = new ArrayList<String>();
        int count;

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
        String[] courseSlots_s      = getSection("COURSE SLOTS", fileStr);
        String[] labSlots_s         = getSection("LAB SLOTS", fileStr);
        String[] courses_s          = getSection("COURSES", fileStr);
        String[] labs_s             = getSection("LABS", fileStr);
        String[] notCompat_s        = getSection("NOT COMPATIBLE", fileStr);
        String[] unwanted_s         = getSection("UNWANTED", fileStr);
        String[] preferences_s      = getSection("PREFERENCES", fileStr);
        String[] pairs_s            = getSection("PAIR", fileStr);
        String[] partialAssign_s    = getSection("PARTIAL ASSIGNMENTS", fileStr);

        // Parse course slots
        ArrayList<CourseSlot> courseSlots = new ArrayList<CourseSlot>();
        count = 0;
        for (String slotStr : courseSlots_s) {
            Matcher m = slotRegex.matcher(slotStr);
            if (m.find()) {
                courseSlots.add(
                    new CourseSlot(
                        count++,
                        Integer.parseInt(m.group(2).replace(":", "")),
                        m.group(1),
                        Integer.parseInt(m.group(3)),
                        Integer.parseInt(m.group(4))
                    )
                );
            }
        }

        // Parse lab slots
        ArrayList<LabSlot> labSlots = new ArrayList<LabSlot>();
        count = 0;
        for (String slotStr : labSlots_s) {
            Matcher m = slotRegex.matcher(slotStr);
            if (m.find()) {
                labSlots.add(
                    new LabSlot(
                        count++,
                        Integer.parseInt(m.group(2).replace(":", "")),
                        m.group(1),
                        Integer.parseInt(m.group(3)),
                        Integer.parseInt(m.group(4))
                    )
                );
            }
        }

        // Parse courses
        ArrayList<Course> courses = new ArrayList<Course>();
        count = 0;
        for (String courseStr : courses_s) {
            Matcher m = courseRegex.matcher(courseStr);
            if (m.find()) {
                courses.add(
                    new Course(
                        count++,
                        Integer.parseInt(m.group(3)),
                        m.group(1),
                        Integer.parseInt(m.group(2))
                    )
                );
            }
        }

        // Parse courses
        ArrayList<Lab> labs = new ArrayList<Lab>();
        count = 0;
        for (String labStr : labs_s) {
            Matcher m1 = labLecRegex.matcher(labStr);
            Matcher m2 = labRegex.matcher(labStr);
            if (m1.find()) {
                labs.add(
                    new Lab(
                        count++,
                        Integer.parseInt(m1.group(3)),
                        m1.group(1),
                        Integer.parseInt(m1.group(2)),
                        Integer.parseInt(m1.group(4))
                    )
                );
            } else if (m2.find()) {
                labs.add(
                    new Lab(
                        count++,
                        m2.group(1),
                        Integer.parseInt(m2.group(2)),
                        Integer.parseInt(m2.group(3))
                    )
                );
            }
        }

        System.out.println("Done!");

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