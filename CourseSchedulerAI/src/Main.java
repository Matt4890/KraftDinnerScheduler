import parser.*;
import schedule.*;
import tree.*;
import coursesULabs.*;
import enums.CourseDays;
import enums.LabDays;

import java.util.*;
import java.io.*;

public class Main {

  public static String filename;
  public static int courseMinPen;
  public static int pairsPen;
  public static int brothersPen;
  public static int labMinPen;
  public static int minAssignWeight;
  public static int pairsWeight;
  public static int brothersWeight;
  public static int prefsWeight;
  public static int total_num_of_units;

  /**
   * Entry point for the scheduler AI.
   * 
   * @param args List of integers as strings for the weights and penalties. In
   *             order: CourseMinPenalty, LabMinPenalty, PairsPenalty,
   *             SectionsPenalty, MinAssignWeight, PrefsWeight, PairsWeight, and
   *             SectionsWeight.
   * @throws FileNotFoundException
   */
  public static void main(String[] args) throws FileNotFoundException {
    // Instantiating the File class

    File file = new File("output.txt");
    // Instantiating the PrintStream class
    PrintStream stream = new PrintStream(file);
    System.out.println("From now on " + file.getAbsolutePath() + " will be your console");
    System.setOut(stream);

    // Printing values to file

    filename = args[0];
    courseMinPen = Integer.parseInt(args[1]);
    labMinPen = Integer.parseInt(args[2]);
    pairsPen = Integer.parseInt(args[3]);
    brothersPen = Integer.parseInt(args[4]);
    minAssignWeight = Integer.parseInt(args[5]);
    prefsWeight = Integer.parseInt(args[6]);
    pairsWeight = Integer.parseInt(args[7]);
    brothersWeight = Integer.parseInt(args[8]);

    Kontrol.setPen_min_filled(courseMinPen);
    Kontrol.setPen_pair(pairsPen);
    Kontrol.setPen_labmin_filled(labMinPen);
    Kontrol.setPen_section_diff(brothersPen);

    Kontrol.setWeight_min_filled(minAssignWeight);
    Kontrol.setWeight_pair(pairsWeight);
    Kontrol.setWeight_pref(prefsWeight);

    Unit.setBrotherIncrease(brothersPen);
    Unit.setPairsIncrease(pairsPen);
    Unit.setPreferencesIncrease(labMinPen);

    Kontrol.setWeight_pair(pairsWeight);
    Kontrol.setWeight_pref(prefsWeight);
    Kontrol.setWeight_section_diff(brothersWeight);

    TreeNode root = null;
    Parser parser = new Parser(filename, Kontrol.getWeight_pref(), Kontrol.getWeight_pair(),
    Kontrol.getWeight_min_filled());

    Kontrol.setAllSlots(parser.getAllSlots());

    if (parser.getPartialAssignments().size() != 0) {
      Pair assign = new Pair(parser.getPartialAssignments().get(0).getSlot(),
      parser.getPartialAssignments().get(0).getUnit());
      root = new TreeNode(assign, 0);

      root.setPenalty(Kontrol.evalAssignmentPairing(assign.getSlot(), assign.getUnit(), root));

      root.setDepth(0);

      HashMap<String, Lab> allLabs = parser.getLabMap();
      addConstraintsForSpecialClasses(allLabs, parser);
      TreeNode curr = root;
      int size = parser.getPartialAssignments().size();
      for (int i = 1; i < size; i++) {
        TreeNode n = new TreeNode(
            new Pair(parser.getPartialAssignments().get(i).getSlot(), parser.getPartialAssignments().get(i).getUnit()),
            0, curr);
        n.setPenalty(Kontrol.evalAssignmentPairing(parser.getPartialAssignments().get(i).getSlot(),
            parser.getPartialAssignments().get(i).getUnit(), n.getParent())
            + n.getParent().getPenaltyValueOfTreeNode());
        n.setDepth(i);
        n.setDepth(curr.getDepth() + 1);
        curr.addChild(n);
        curr = n;

      }
    }

    if (root == null) {
      root = new TreeNode(new Pair(null, null), 0);
    }

    HashMap<String, Course> allCourses = parser.getCourseMap();

    HashMap<String, Lab> allLabs = parser.getLabMap();

    makeBrothers(allCourses);
    makePotentialsBros(allCourses);

    // Get list of units to ignore (partial assignments)
    ArrayList<Unit> partialAssignedUnits = new ArrayList<Unit>();
    parser.getPartialAssignments().forEach(p -> partialAssignedUnits.add(p.getUnit()));

    ArrayList<Unit> unitsToProcess = orderedUnitsForAdding(allCourses, allLabs, partialAssignedUnits);
    total_num_of_units = unitsToProcess.size();
    Generator search = new Generator(root, (parser.getPartialAssignments().size() + unitsToProcess.size()));

    int numberNodesBefore = parser.getPartialAssignments().size() != 0 ? parser.getPartialAssignments().size() : 0;

    setupOverlaps(parser.getAllSlots());

    search.branchAndBoundSkeleton(root, unitsToProcess, parser.getAllSlots(), numberNodesBefore);


  }
  /*
   * Function to add CPSC813 and CPSC913 if CPSC313 and CPSC413 exist
   *
   * @param allCourses: hashmap of courses indexed by string
   *
   * @param parse: Class Parser to add CPSC813 and CPSC913 to partial assignment
   *
   */
  private static void addConstraintsForSpecialClasses(HashMap<String, Lab> allCourses, Parser parse) {
    boolean toremove813 = false;
    String id813 = "";
    boolean toremove913 = false;
    String id913 = "";
    ArrayList<Slot> slots = parse.getAllSlots();
    Slot tues1800 = null;
    for (Slot slot : slots) {
      if (slot.getTime() == 1800) {
        if (slot instanceof LabSlot) {
          if (((LabSlot) slot).getDay() == LabDays.TUETHR) {
            tues1800 = slot;
          }
        }
      }
    }
    for (Map.Entry<String, Lab> entry : allCourses.entrySet()) {
      if (entry.getValue().getCourseNum() == 813 && entry.getValue().getCourseType().equals("CPSC")) {
        Lab cpsc813 = entry.getValue();
        if (tues1800 != null) {
          parse.getPartialAssignments().add(new Pair(tues1800, cpsc813));
          toremove813 = true;
          id813 = cpsc813.toString();
        } else {
          System.out.println("No valid Solution");
          System.exit(0);
        }
        for (Unit unit : cpsc813.getNotCompatible()) {
          if (unit.getCourseNum() == 313 && unit.getCourseType().equals("CPSC")) {
            Unit cpsc313 = unit; // purely for readability can be a lab or course
            cpsc813.addToNotCompatible(cpsc313);
            cpsc313.addToNotCompatible(cpsc813);
            cpsc313.incrementNonCompatible();
            cpsc813.incrementNonCompatible();
            for (Unit notCompatibleToadd : cpsc313.getNotCompatible()) {
              cpsc813.addToNotCompatible(notCompatibleToadd);
              notCompatibleToadd.addToNotCompatible(cpsc813);
              notCompatibleToadd.incrementNonCompatible();
              cpsc813.incrementNonCompatible();
            }
          }
        }
      } else if (entry.getValue().getCourseNum() == 913 && entry.getValue().getCourseType().equals("CPSC")) {
        Lab cpsc913 = entry.getValue();
        if (tues1800 != null) {
          parse.getPartialAssignments().add(new Pair(tues1800, cpsc913));
          toremove913 = true;
          id913 = cpsc913.toString();
        } else {
          System.out.println("No valid Solution");
          System.exit(0);
        }
        for (Unit unit : cpsc913.getNotCompatible()) {
          if (unit.getCourseNum() == 413 && unit.getCourseType().equals("CPSC")) {
            Unit cpsc413 = unit; // purely for readability can be a lab or course
            cpsc913.addToNotCompatible(cpsc413);
            cpsc413.addToNotCompatible(cpsc913);
            cpsc413.incrementNonCompatible();
            cpsc913.incrementNonCompatible();
            for (Unit notCompatibleToadd : cpsc413.getNotCompatible()) {
              cpsc913.addToNotCompatible(notCompatibleToadd);
              notCompatibleToadd.addToNotCompatible(cpsc913);
              notCompatibleToadd.incrementNonCompatible();
              cpsc913.incrementNonCompatible();
            }
          }
        }
      }
    }
    if (toremove813) {
      allCourses.remove(id813);
    }
    if (toremove913) {
      allCourses.remove(id913);
    }
  }

  /*
   * Function to order units as we want to process them
   *
   * @param courses: hashmap of courses indexed by string
   *
   * @param lab: hashmap of labs indexed by string
   *
   * @return: ArrayList of units in order to process
   */
  private static ArrayList<Unit> orderedUnitsForAdding(HashMap<String, Course> courses, HashMap<String, Lab> labs,
    ArrayList<Unit> ignored) {
      ArrayList<Unit> toReturn = new ArrayList<Unit>();
      for (Map.Entry<String, Course> entry : courses.entrySet()) {
        Course c = (Course) entry.getValue();
        toReturn.add(c);
      }
      for (Map.Entry<String, Lab> entry : labs.entrySet()) {
        Lab l = (Lab) entry.getValue();
        toReturn.add(l);
      }

      for (Unit u : ignored) {
        toReturn.remove(u);
      }

      bubbleSort(toReturn);


      return toReturn;
    }

  /**
   * takes a arraylist of units and orders them from most constrained to least
   *
   * @param units
   */
  public static void bubbleSort(ArrayList<Unit> units) {
    int n = units.size();
    for (int i = 0; i < n - 1; i++) {
      for (int j = 0; j < n - i - 1; j++) {
        if (units.get(j).getConstrained() == units.get(j + 1).getConstrained()) {
          if (units.get(j).getSoftPref() < units.get(j + 1).getSoftPref()) {
            Unit temp = units.get(j);
            units.set(j, units.get(j + 1));
            units.set(j + 1, temp);
          }
        } else if (units.get(j).getConstrained() < units.get(j + 1).getConstrained()) {
          Unit temp = units.get(j);
          units.set(j, units.get(j + 1));
          units.set(j + 1, temp);
        }
      }
    }
  }
  /*
   * Function to account for potential of a lecture to have multiple sections(brothers)
   *
   * @param courses: hashmap of courses indexed by string
   *
   */
  private static void makePotentialsBros(HashMap<String, Course> courses) {
    ArrayList<Course> checked = new ArrayList<Course>();
    for (Map.Entry<String, Course> entry : courses.entrySet()) {
      Course courseToAddPotential = entry.getValue();
      checked.add(courseToAddPotential);
      for (Course course : courseToAddPotential.getBrothers()) {
        if (!checked.contains(course)) {
          courseToAddPotential.incrementPotential(((double) (brothersPen)) / 2);
          course.incrementPotential(((double) (brothersPen)) / 2);
        }
      }
    }
  }
  /*
   * Function create a list of brothers(same lecture with different section)
   *
   * @param courses: hashmap of courses indexed by string
   *
   */
  private static void makeBrothers(HashMap<String, Course> courses) {
    for (Map.Entry<String, Course> entry : courses.entrySet()) {
      for (Map.Entry<String, Course> entry2 : courses.entrySet()) {
        if (entry.getValue().isBrother(entry2.getValue())) {
          entry.getValue().addBrother(entry2.getValue());
          entry.getValue().increasePrefBrothers();
        }
      }
    }
  }

  public static int getNumOfUnits() {
    return total_num_of_units;
  }
  /*
   * Function to account for overlapping slots
   *
   * @param slots: ArrayList of all slots
   * 
   */
  public static void setupOverlaps(ArrayList<Slot> slots) {
    for (Slot slot : slots) {
      for (Slot slot2 : slots) {
        if (slot instanceof CourseSlot) {
          if (slot2 instanceof CourseSlot) {
            if (slot.getTime() == slot2.getTime()) {
              if (((CourseSlot) slot).getDay() == ((CourseSlot) slot2).getDay()) {
                if (slot != slot2) {
                  slot.addOverlaps(slot2);
                }
              }
            }
          }

          // slot 2 is a lab
          else {
            if (((CourseSlot) slot).getDay() == CourseDays.MONWEDFRI) {
              if (((LabSlot) slot2).getDay() == LabDays.MONWED) {
                if (slot.getTime() == slot2.getTime()) {
                  if (!slot.getOverlaps().contains(slot2))
                    slot.addOverlaps(slot2);
                }
              } else if (((LabSlot) slot2).getDay() == LabDays.FRI) {
                if (slot.getTime() == slot2.getTime() || slot.getTime() == slot2.getTime() + 100) {
                  if (!slot.getOverlaps().contains(slot2))
                    slot.addOverlaps(slot2);
                }
              }
            }
          }
        }

        else {
          if (slot2 instanceof CourseSlot) {
            if (((LabSlot) slot).getDay() == LabDays.FRI) {
              if (((CourseSlot) slot2).getDay() == CourseDays.MONWEDFRI) {
                if (slot.getTime() == slot2.getTime() || slot.getTime() == slot2.getTime() - 100) {
                  if (!slot.getOverlaps().contains(slot2))
                    slot.addOverlaps(slot2);
                }
              }
            } else if (((LabSlot) slot).getDay() == LabDays.MONWED) {
              if (((CourseSlot) slot2).getDay() == CourseDays.MONWEDFRI) {
                if (slot.getTime() == slot2.getTime()) {
                  if (!slot.getOverlaps().contains(slot2))
                    slot.addOverlaps(slot2);
                }
              }
            } else {
              if (((CourseSlot) slot2).getDay() == CourseDays.TUETHR) {
                if (slot.getTime() % 100 == 0) {
                  if (slot2.getTime() == slot.getTime()) {
                    if (!slot.getOverlaps().contains(slot2))
                      slot.addOverlaps(slot2);
                  } else if (slot.getTime() == slot2.getTime() - 100) {
                    if (!slot.getOverlaps().contains(slot2))
                      slot.addOverlaps(slot2);
                  }
                } else {
                  if (slot.getTime() == slot2.getTime() + 30) {
                    if (!slot.getOverlaps().contains(slot2))
                      slot.addOverlaps(slot2);
                  } else if (slot.getTime() == slot2.getTime() - 70) {
                    if (!slot.getOverlaps().contains(slot2))
                      slot.addOverlaps(slot2);
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
