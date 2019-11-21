package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import coursesULabs.*;
import enums.CourseDays;
import enums.LabDays;
import schedule.*;

class Parser {

    public static void main(String[] args) {

        // Setup file params
        // String filename = "./inputs/shortExample.txt";
        String filename = "./inputs/departmentExample2.txt";
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
        Pattern slotRegex   = Pattern.compile("(MO|TU|FR),(\\d{1,2}:\\d{2}),(\\d+),(\\d+)");
        Pattern courseRegex = Pattern.compile("([A-Z]{4})(\\d{3})LEC(\\d{2})");
        Pattern labRegex    = Pattern.compile("([A-Z]{4})(\\d{3})TUT(\\d{2})");
        Pattern labLecRegex = Pattern.compile("([A-Z]{4})(\\d{3})LEC(\\d{2})TUT(\\d{2})");
        Pattern csIdRegex   = Pattern.compile("CS(\\d+)");
        Pattern lsIdRegex   = Pattern.compile("LS(\\d+)");
        Pattern cIdRegex    = Pattern.compile("C(\\d+)");
        Pattern lIdRegex    = Pattern.compile("L(\\d+)");

        // Get sections from input file
        String   fileStr            = String.join("\n", fileLines).toUpperCase().replaceAll(" |\t", "").replaceAll("\\bLAB\\b", "TUT");
        String[] courseSlots_s      = getSectionLines("COURSESLOTS", fileStr);
        String[] labSlots_s         = getSectionLines("LABSLOTS", fileStr);
        String[] courses_s          = getSectionLines("COURSES", fileStr);
        String[] labs_s             = getSectionLines("LABS", fileStr);
        String   notCompat_s        = getSection("NOTCOMPATIBLE", fileStr);
        String   unwanted_s         = getSection("UNWANTED", fileStr);
        String   preferences_s      = getSection("PREFERENCES", fileStr);
        String   pairs_s            = getSection("PAIR", fileStr);
        String   partialAssign_s    = getSection("PARTIALASSIGNMENTS", fileStr);

        // Parse course slots
        ArrayList<CourseSlot> courseSlots = new ArrayList<CourseSlot>();
        count = 0;
        for (String slotStr : courseSlots_s) {
            Matcher m = slotRegex.matcher(slotStr);
            if (m.find()) {
                CourseSlot cs = new CourseSlot(
                    count,
                    Integer.parseInt(m.group(2).replace(":", "")),
                    CourseDays.fromString(m.group(1)),
                    Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4)),
                    new HashMap<Unit, Integer>()
                );
                courseSlots.add(cs);
                String replaceStr = cs.toString();
                unwanted_s = unwanted_s.replaceAll("(?<!(?:TUT|LAB).*)" + replaceStr, "CS" + count);
                preferences_s = preferences_s.replaceAll(replaceStr + "(?!.*(?:TUT|LAB))", "CS" + count);
                partialAssign_s = partialAssign_s.replaceAll("(?<!(?:TUT|LAB).*)" + replaceStr, "CS" + count);
                count++;
            } else {
                System.out.println("Failed to parse string '" + slotStr + "' as a CourseSlot!");
                System.out.println("Exiting...");
                System.exit(1);
            }
        }

        // Parse lab slots
        ArrayList<LabSlot> labSlots = new ArrayList<LabSlot>();
        count = 0;
        for (String slotStr : labSlots_s) {
            Matcher m = slotRegex.matcher(slotStr);
            if (m.find()) {
                LabSlot ls = new LabSlot(
                    count,
                    Integer.parseInt(m.group(2).replace(":", "")),
                    LabDays.fromString(m.group(1)),
                    Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4)),
                    new HashMap<Unit, Integer>()
                );
                labSlots.add(ls);
                String replaceStr = ls.toString();
                unwanted_s = unwanted_s.replaceAll(replaceStr, "LS" + count);
                preferences_s = preferences_s.replaceAll(replaceStr, "LS" + count);
                partialAssign_s = partialAssign_s.replaceAll(replaceStr, "LS" + count);
                count++;
            } else {
                System.out.println("Failed to parse string '" + slotStr + "' as a LabSlot!");
                System.out.println("Exiting...");
                System.exit(1);
            }
        }

        // Parse labs
        ArrayList<Lab> labs = new ArrayList<Lab>();
        count = 0;
        for (String labStr : labs_s) {
            Matcher m1 = labLecRegex.matcher(labStr);
            Matcher m2 = labRegex.matcher(labStr);
            if (m1.find()) {
                Lab l = new Lab(
                    count,
                    Integer.parseInt(m1.group(3)),
                    m1.group(1),
                    Integer.parseInt(m1.group(2)),
                    Integer.parseInt(m1.group(4))
                );
                labs.add(l);
                String replaceStr = l.toString();
                notCompat_s = notCompat_s.replaceAll(replaceStr, "L" + count);
                unwanted_s = unwanted_s.replaceAll(replaceStr, "L" + count);
                preferences_s = preferences_s.replaceAll(replaceStr, "L" + count);
                pairs_s = pairs_s.replaceAll(replaceStr, "L" + count);
                partialAssign_s = partialAssign_s.replaceAll(replaceStr, "L" + count);
                count++;
            } else if (m2.find()) {
                Lab l = new Lab(
                    count,
                    m2.group(1),
                    Integer.parseInt(m2.group(2)),
                    Integer.parseInt(m2.group(3))
                );
                labs.add(l);
                String replaceStr = l.toString();
                notCompat_s = notCompat_s.replaceAll(replaceStr, "L" + count);
                unwanted_s = unwanted_s.replaceAll(replaceStr, "L" + count);
                preferences_s = preferences_s.replaceAll(replaceStr, "L" + count);
                pairs_s = pairs_s.replaceAll(replaceStr, "L" + count);
                partialAssign_s = partialAssign_s.replaceAll(replaceStr, "L" + count);
                count++;
            } else {
                System.out.println("Failed to parse string '" + labStr + "' as a Lab!");
                System.out.println("Exiting...");
                System.exit(1);
            }
        }

        // Parse courses
        ArrayList<Course> courses = new ArrayList<Course>();
        count = 0;
        for (String courseStr : courses_s) {
            Matcher m = courseRegex.matcher(courseStr);
            if (m.find()) {
                Course c = new Course(
                    count,
                    Integer.parseInt(m.group(3)),
                    m.group(1),
                    Integer.parseInt(m.group(2))
                );
                courses.add(c);
                String replaceStr = c.toString();
                notCompat_s = notCompat_s.replaceAll(replaceStr, "C" + count);
                unwanted_s = unwanted_s.replaceAll(replaceStr, "C" + count);
                preferences_s = preferences_s.replaceAll(replaceStr, "C" + count);
                pairs_s = pairs_s.replaceAll(replaceStr, "C" + count);
                partialAssign_s = partialAssign_s.replaceAll(replaceStr, "C" + count);
                count++;
            } else {
                System.out.println("Failed to parse string '" + courseStr + "' as a Course!");
                System.out.println("Exiting...");
                System.exit(1);
            }
        }

        // Parse Not Compatible
        for (String line : notCompat_s.split("\n")) {
            Matcher cm = cIdRegex.matcher(line);
            Matcher lm = lIdRegex.matcher(line);
            Unit u1 = null;
            Unit u2 = null;
            while (cm.find()) {
                if (u1 == null) {
                    u1 = courses.get(Integer.parseInt(cm.group(1)));
                } else {
                    u2 = courses.get(Integer.parseInt(cm.group(1)));
                }
            }
            while (lm.find()) {
                if (u1 == null) {
                    u1 = labs.get(Integer.parseInt(lm.group(1)));
                } else {
                    u2 = labs.get(Integer.parseInt(lm.group(1)));
                }
            }
            if (u1 == null || u2 == null) {
                System.out.println("Failed to parse IDs on line '" + line + "'!");
                System.out.println("Exiting...");
                System.exit(1);
            } else {
                u1.addToNotCompatible(u2);
                u2.addToNotCompatible(u1);
            }
        }

        // Parse Unwanted
        for (String line : unwanted_s.split("\n")) {
            Matcher cm = cIdRegex.matcher(line);
            Matcher lm = lIdRegex.matcher(line);
            Matcher csm = csIdRegex.matcher(line);
            Matcher lsm = lsIdRegex.matcher(line);
            Unit u = null;
            Slot s = null;
            if (cm.find()) {
                u = courses.get(Integer.parseInt(cm.group(1)));
            } else if (lm.find()) {
                u = labs.get(Integer.parseInt(lm.group(1)));
            }
            if (csm.find()) {
                s = courseSlots.get(Integer.parseInt(csm.group(1)));
            } else if (lsm.find()) {
                s = labSlots.get(Integer.parseInt(lsm.group(1)));
            }
            if (u == null || s == null) {
                System.out.println("Failed to parse IDs on line '" + line + "'!");
                System.out.println("Exiting...");
                System.exit(1);
            } else {
                u.addToUnwanted(s);
            }
        }

        // Parse Preferences
        for (String line : preferences_s.split("\n")) {
        }

        // Parse Pairs
        for (String line : pairs_s.split("\n")) {
        }

        // Parse Partial Assignments
        HashMap<Slot, Unit> partialAssignments = new HashMap<Slot, Unit>();
        for (String line : partialAssign_s.split("\n")) {
            String[] params = line.split(",");

        }
        // TODO: REMOVE ASSIGNED UNITS FROM COURSE/LAB ARRAY LIST BEFORE OUTPUT!

        // Output!

        HashMap<String, Course> courseMap = new HashMap<String, Course>();
        HashMap<String, Lab>    labMap    = new HashMap<String, Lab>();

        for (Course c : courses) courseMap.put(c.toString(), c);
        for (Lab l : labs)       labMap.put(l.toString(), l);

        Schedule schedule = new Schedule(courseSlots, labSlots);

        System.out.println("Done!");

    }

    private static String[] getSectionLines(String label, String fileStr) {
        return getSection(label, fileStr).split("\n");
    }

    private static String getSection(String label, String fileStr) {

        Pattern regex = Pattern.compile(label + "\\s*:\\s*(.*?)(?:\n\n|$)", Pattern.DOTALL);
        Matcher matcher = regex.matcher(fileStr);
        if (matcher.find() && !matcher.group(1).equals("")) {
            return matcher.group(1);
        } else {
            return "";
        }

    }

}
