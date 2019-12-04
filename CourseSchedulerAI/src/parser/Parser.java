package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Constraints.SoftConstraints;
import coursesULabs.*;
import enums.CourseDays;
import enums.LabDays;
import schedule.*;
import tree.*;

public class Parser {
    private HashMap<String, Course> courseMap = new HashMap<String, Course>();
    private HashMap<String, Lab>    labMap    = new HashMap<String, Lab>();
    private Schedule schedule; 
    private int initialPenalty; 
    private int minWeightCount;
    private int pairWeightCount;
    private int sum_of_pref;
    private int pairWeight;
    private int minWeight;
    private int prefWeight2;
    private ArrayList<Pair> partialAssignments = new ArrayList<Pair>();
    private ArrayList<Slot> allSlots = new ArrayList<Slot>();
    

    public int getminWeightCount(){
        return minWeightCount;
    }
    public int getpairWeightCount(){
        return pairWeightCount;
    }
    public int getprefWeight(){
        return sum_of_pref;
    }
    public ArrayList<Slot> getAllSlots(){
        return this.allSlots;
    }

    
    public  Parser (String filename, int prefWeight, int pairWeight, int minWeight) {

        this.prefWeight2 = prefWeight;
        this.pairWeight = pairWeight;
        this.minWeight = minWeight;
        // Setup file params
        // String filename = "./inputs/shortExample.txt";
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
        Pattern csIdRegex   = Pattern.compile("=CS(\\d+)=");
        Pattern lsIdRegex   = Pattern.compile("=LS(\\d+)=");
        Pattern cIdRegex    = Pattern.compile("=C(\\d+)=");
        Pattern lIdRegex    = Pattern.compile("=L(\\d+)=");

        // Get sections from input file
        String   fileStr            = String.join("\n", fileLines).toUpperCase().replaceAll("\\bLAB\\b", "TUT").replaceAll(" |\t", "");
        String[] courseSlots_s      = getSectionLines("COURSESLOTS", fileStr);
        String[] labSlots_s         = getSectionLines("TUTSLOTS", fileStr);
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
            if (slotStr.equals("")) break;
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
                this.allSlots.add(cs);
                String replaceStr = cs.toString();
                unwanted_s = unwanted_s.replaceAll("(?<!(?:TUT|LAB).*)" + replaceStr, "=CS" + count + "=");
                preferences_s = preferences_s.replaceAll(replaceStr + "(?!.*(?:TUT|LAB))", "=CS" + count + "=");
                partialAssign_s = partialAssign_s.replaceAll("(?<!(?:TUT|LAB).*)" + replaceStr, "=CS" + count + "=");
                count++;
                if(cs.getCourseMin() > 0){
                    minWeightCount = minWeightCount+ cs.getCourseMin();
                    System.out.println("CourseSlotsMinWeight:" + minWeightCount);
                    cs.incrementPotential(-minWeight);
                    System.out.println("added " + minWeight + " for slotmin for " + cs.toString()  );
                }
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
            if (slotStr.equals("")) break;
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
                this.allSlots.add(ls);
                String replaceStr = ls.toString();
                unwanted_s = unwanted_s.replaceAll(replaceStr, "=LS" + count + "=");
                preferences_s = preferences_s.replaceAll(replaceStr, "=LS" + count + "=");
                partialAssign_s = partialAssign_s.replaceAll(replaceStr, "=LS" + count + "=");
                count++;
                if(ls.getLabMin() > 0){
                    minWeightCount = minWeightCount+ ls.getLabMin();
                    System.out.println("LabSlotMinWeight:" + minWeightCount);
                    ls.incrementPotential(-minWeight);
                    System.out.println("added " + minWeight + " for slotmin for " + ls.toString()  );
                }
            } else {
                System.out.println("Failed to parse string '" + slotStr + "' as a LabSlot!");
                System.out.println("Exiting...");
                System.exit(1);
            }

        }
        System.out.println("TotalMinWeight:" + minWeightCount);
        // Parse labs
        ArrayList<Lab> labs = new ArrayList<Lab>();
        count = 0;
        for (String labStr : labs_s) {
            if (labStr.equals("")) break;
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
                notCompat_s = notCompat_s.replaceAll(replaceStr, "=L" + count + "=");
                unwanted_s = unwanted_s.replaceAll(replaceStr, "=L" + count + "=");
                preferences_s = preferences_s.replaceAll(replaceStr, "=L" + count + "=");
                pairs_s = pairs_s.replaceAll(replaceStr, "=L" + count + "=");
                partialAssign_s = partialAssign_s.replaceAll(replaceStr, "=L" + count + "=");
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
                notCompat_s = notCompat_s.replaceAll(replaceStr, "=L" + count + "=");
                unwanted_s = unwanted_s.replaceAll(replaceStr, "=L" + count + "=");
                preferences_s = preferences_s.replaceAll(replaceStr, "=L" + count + "=");
                pairs_s = pairs_s.replaceAll(replaceStr, "=L" + count + "=");
                partialAssign_s = partialAssign_s.replaceAll(replaceStr, "=L" + count + "=");
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
            if (courseStr.equals("")) break;
            Matcher m = courseRegex.matcher(courseStr);
            if (m.find()) {
                Course c = new Course(
                    count,
                    Integer.parseInt(m.group(3)),
                    m.group(1),
                    Integer.parseInt(m.group(2))
                );
                courses.add(c);
                String replaceStr = c.toString() + "(?!TUT)";
                notCompat_s = notCompat_s.replaceAll(replaceStr, "=C" + count + "=");
                unwanted_s = unwanted_s.replaceAll(replaceStr, "=C" + count + "=");
                preferences_s = preferences_s.replaceAll(replaceStr, "=C" + count + "=");
                pairs_s = pairs_s.replaceAll(replaceStr, "=C" + count + "=");
                partialAssign_s = partialAssign_s.replaceAll(replaceStr, "=C" + count + "=");
                count++;
                if(Integer.toString(c.getLectureNum()).substring(0, 1).equals("9")){
                    c.incrementEvening();
                }
                else if(c.getCourseNum() >=500 && c.getCourseNum() < 600){
                    c.increment500();
                }
                else if(c.getCourseNum() == 313){
                    Lab cpsc813 = new Lab(Integer.MAX_VALUE, 1, "CPSC", 813, 0);
                    labs.add(cpsc813);
                }
                else if(c.getCourseNum() == 413){
                    Lab cpsc913 = new Lab(Integer.MAX_VALUE, 1, "CPSC", 913, 0);
                    labs.add(cpsc913);
                }
            } else {
                System.out.println("Failed to parse string '" + courseStr + "' as a Course!");
                System.out.println("Exiting...");
                System.exit(1);
            }
        }

        // Parse Not Compatible
        for (String line : notCompat_s.split("\n")) {
            if (line.equals("")) break;
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
                // System.out.println("Exiting...");
                // System.exit(1);
            } else {
                u1.addToNotCompatible(u2);
                u2.addToNotCompatible(u1);
                u1.incrementNonCompatible();
                u2.incrementNonCompatible();
            }
        }

        // Parse Unwanted
        for (String line : unwanted_s.split("\n")) {
            if (line.equals("")) break;
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
                // System.out.println("Exiting...");
                // System.exit(1);
            } else {
                u.addToUnwanted(s);
                u.incrementUnwanted();
            }
        }

        // Parse Preferences
        sum_of_pref = 0;
        for (String line : preferences_s.split("\n")) {
            if (line.equals("")) break;
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
                // System.out.println("Exiting...");
                // System.exit(1);
            } else {
                u.addToPreferences(s, Integer.parseInt(line.split(",")[2]));
                sum_of_pref += Integer.parseInt(line.split(",")[2]); 
                System.out.println("Sum_of_pref:" + sum_of_pref);
                u.incrementPotential(- prefWeight2  * Integer.parseInt(line.split(",")[2]) );
                u.increasePrefPreferences();
                System.out.println("added pen for pref of " + prefWeight2 + " for " + u.toString() );
            }
        }
        System.out.println("Totalsum_of_pref:" + sum_of_pref);

        // Parse Pairs
        for (String line : pairs_s.split("\n")) {
            if (line.equals("")) break;
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
                // System.out.println("Exiting...");
                // System.exit(1);
            } else {
                u1.addToPairs(u2);
                u2.addToPairs(u1);
                u1.incrementPotential(- (((double)pairWeight) / 2));
                u2.incrementPotential(- (((double)pairWeight) / 2));
                u1.increasePrefPairs();
                u2.increasePrefPairs();
                System.out.println("added pen for pair of " + pairWeight + " for " + u1.toString() );
                pairWeightCount ++;
                System.out.println("PairsWeightCount:" + pairWeightCount);
            }
        }
        System.out.println("TotalWeightCount:" + pairWeightCount);

        // Parse Partial Assignments
        for (String line : partialAssign_s.split("\n")) {
            if (line.equals("")) break;
            Matcher cm = cIdRegex.matcher(line);
            Matcher lm = lIdRegex.matcher(line);
            Matcher csm = csIdRegex.matcher(line);
            Matcher lsm = lsIdRegex.matcher(line);
            Unit u = null;
            Slot s = null;
            int i = -1;
            boolean isCourse = false;
            if (cm.find()) {
                isCourse = true;
                i = Integer.parseInt(cm.group(1));
                u = courses.get(i);
            } else if (lm.find()) {
                i = Integer.parseInt(lm.group(1));
                u = labs.get(i);
            }
            if (csm.find()) {
                s = courseSlots.get(Integer.parseInt(csm.group(1)));
            } else if (lsm.find()) {
                s = labSlots.get(Integer.parseInt(lsm.group(1)));
            }
            if (u == null || s == null) {
                System.out.println("Failed to parse IDs on line '" + line + "'!");
                // System.out.println("Exiting...");
                // System.exit(1);
            } else {
                this.partialAssignments.add(new Pair(s,u));
                // this.initialPenalty += Kontrol.evalAssignment(s, u);
                
                // s.addOccupant(u);
                // if (isCourse) {
                //     courses.remove(i);
                // } else {
                //     labs.remove(i);
                // }
            }
        }

        // Output!

        //HashMap<String, Course> courseMap = new HashMap<String, Course>();
        //HashMap<String, Lab>    labMap    = new HashMap<String, Lab>();
        Unit.setUnwantedIncrease((courses.size()+ labs.size()) / (courseSlots.size()+labSlots.size()));
        Unit.setNonCompatibleIncrease((courses.size()+ labs.size()) / (courseSlots.size()+labSlots.size()));
        int evening =0;
        int slotsavailable  = 0;
        for(CourseSlot slot : courseSlots ){
            if (slot.getTime() >= 1800){
                slotsavailable += slot.getCourseMax();
            }
        }
        int eveCourses = 0;
        for(Course c : courses){
            if(Integer.toString(c.getLectureNum()).substring(0, 1).equals("9")){
                eveCourses ++;
            }
        }
        if(slotsavailable != 0){
            evening = (eveCourses / slotsavailable) / 2;
        }
        if(eveCourses > slotsavailable){
            System.out.println("Evening courses but no evening slots or not enough");
            System.exit(0);
        }
        Unit.setEveningIncrease(evening);
        Unit.setIncrease500((courses.size()+ labs.size()) / (courseSlots.size()+labSlots.size()));
    


        for (Course c : courses) this.courseMap.put(c.toString(), c);
        for (Lab l : labs)       this.labMap.put(l.toString(), l);

        this.schedule = new Schedule(courseSlots, labSlots);

        System.out.println("Done!");

    }
    public int getInitialPenalty(){
        return this.initialPenalty;
    }
    public Schedule getSchedule(){
        return this.schedule;
    }
    public HashMap<String, Course> getCourseMap(){
        return this.courseMap;
    }
    public HashMap<String, Lab> getLabMap(){
        return this.labMap;
    }
    
    private static String[] getSectionLines(String label, String fileStr) {
        return getSection(label, fileStr).split("\n");
    }
    public ArrayList<Pair> getPartialAssignments(){
        return this.partialAssignments;

    }

    private static String getSection(String label, String fileStr) {

        Pattern regex = Pattern.compile(label + "\\s*:(.*?)(?:\n\n|$)", Pattern.DOTALL);
        Matcher matcher = regex.matcher(fileStr);
        if (matcher.find() && !matcher.group(1).equals("")) {
            return matcher.group(1).charAt(0) == '\n' ?
                matcher.group(1).substring(1) :
                matcher.group(1);
        } else {
            return "";
        }

    }

}
