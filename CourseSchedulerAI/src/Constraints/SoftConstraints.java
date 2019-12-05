package Constraints;

import schedule.*;
import tree.Kontrol;
import tree.*;
import tree.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import coursesULabs.*;

public class SoftConstraints {
  /*
   * public static int calculatePenalty(Slot s, Unit u, TreeNode node, int pen) {
   * 
   * int total = 0; if (s instanceof CourseSlot) { total +=
   * SoftConstraints.checkCourseMin((CourseSlot) s, node, pen);
   * System.out.println("Calculated Course Min"); total +=
   * SoftConstraints.preferenceEval(s, u, node);
   * System.out.println("Calculated Preference"); total +=
   * SoftConstraints.notPairedCourse((Course) u, (CourseSlot) s, node);
   * System.out.println("Calculated Pair"); total +=
   * SoftConstraints.checkSections((Course) u, (CourseSlot) s, node);
   * System.out.println("Calculated Section"); } else { total +=
   * SoftConstraints.checkLabMin((LabSlot) s, node);
   * System.out.println("Calculated Lab Min"); total +=
   * SoftConstraints.preferenceEval(s, u, node);
   * System.out.println("Calculated Lab Preference"); total +=
   * SoftConstraints.notPairedLab((Lab) u, (LabSlot) s, node);
   * System.out.println("Calculated Lab Pair");
   * 
   * } return total;
   * 
   * }
   */

  public static int checkCourseMin(Slot s, TreeNode node, int pen, ArrayList<Slot> allSlots) {
    int total = 0;
    TreeNode current = node;
    ArrayList<Pair> scheduled = new ArrayList<Pair>();
    while (current != null){
      scheduled.add(current.getAssign());
      current = current.getParent();
    }


    for(Slot selectedSlot : allSlots){
      if(selectedSlot instanceof CourseSlot){
        int counter = 0;
        for(Pair selectPair : scheduled){
          if(selectPair.getSlot() == selectedSlot){
            counter++;
          }
        }
        total += Math.max(0, ((CourseSlot) selectedSlot).getCourseMin() - counter) * pen;
      }
    }

    return total;
  }

  /*
  public static int checkCourseMin(Slot s, TreeNode node, int pen, ArrayList<Slot> allSlots) {
    // decrement if the course min is met and coursemin != 1
    int count = 0;
    TreeNode current = node.getParent();

    int[] counts = new int[allSlots.size()];
    while (current != null && current.getAssign().getUnit() != null) {
      for (int i = 0; i < allSlots.size(); i++) {
        if (allSlots.get(i) == current.getAssign().getSlot()) {
          counts[i]++;
        }
      }

      for (int i = 0; i < allSlots.size(); i++) {
        if (allSlots.get(i) instanceof CourseSlot) {

          int a = ((CourseSlot) allSlots.get(i)).getCourseMin();
          int b = counts[i];
          if (((CourseSlot) allSlots.get(i)).getCourseMin() > counts[i]) {
            count += (((CourseSlot) allSlots.get(i)).getCourseMin() - counts[i]);
          }
        }
      }

      // System.out.println(current.toString());
      current = current.getParent();
      // node.incrementDesire((s.getCourseMin() - count) *
      // Kontrol.getWeight_min_filled());
    }

    return pen * count;
  }
  */
  public static int checkLabMin(Slot s, TreeNode node, int pen, ArrayList<Slot> allSlots) {
    int total = 0;
    TreeNode current = node;
    ArrayList<Pair> scheduled = new ArrayList<Pair>();
    while (current != null){
      scheduled.add(current.getAssign());
      current = current.getParent();
    }


    for(Slot selectedSlot : allSlots){
      if(selectedSlot instanceof LabSlot){
        int counter = 0;
        for(Pair selectPair : scheduled){
          if(selectPair.getSlot() == selectedSlot){
            counter++;
          }
        }
        total += Math.max(0, ((LabSlot) selectedSlot).getLabMin() - counter) * pen;
      }
    }

    return total;
  }

  /*
  public static int checkLabMin(Slot s, TreeNode node, int pen, ArrayList<Slot> allSlots) {
    // decrement if the course min is met and coursemin != 1
    int count = 0;
    TreeNode current = node.getParent();

    int[] counts = new int[allSlots.size()];
    while (current != null && current.getAssign().getUnit() != null) {
      for (int i = 0; i < allSlots.size(); i++) {
        if (allSlots.get(i) == current.getAssign().getSlot()) {
          counts[i]++;
        }
      }

      for (int i = 0; i < allSlots.size(); i++) {
        if (allSlots.get(i) instanceof LabSlot) {
          if (((LabSlot) allSlots.get(i)).getLabMin() > counts[i]) {
            count += (((LabSlot) allSlots.get(i)).getLabMin() - counts[i]);
          }
        }
      }

      // System.out.println(current.toString());
      current = current.getParent();
      // node.incrementDesire((s.getCourseMin() - count) *
      // Kontrol.getWeight_min_filled());
    }

    return pen * count;

  }
  */
  public static int preferenceEval(Slot slot, Unit u, TreeNode node) {
    HashMap<Slot, Integer> pref = u.getPreferences();
    int total = 0;
    int num = 0;
    if (!pref.isEmpty()) {
      for (Map.Entry<Slot, Integer> entry : pref.entrySet()) {
        if (entry.getKey() != slot) {
          total += entry.getValue();
        } else {
          num = entry.getValue();
        }
      }
    }
    node.incrementDesire(num * Kontrol.getWeight_pref());

    return total;
    /*
     * int total = 0; for (int i = 0; i < slot.getClassAssignment().size(); i++) {
     * if (!slot.getPreference().containsKey(slot.getClassAssignment().get(i))) {
     * Unit u = slot.getClassAssignment().get(i); try { total += (Integer)
     * slot.getPreference().get(u); } catch (Exception e) {}; } } return total;
     */
  }
  
  public static int notPairedCourse(Course b, CourseSlot s, TreeNode node, int pen){

    int total =0;
    TreeNode current = node.getParent();
    ArrayList <Pair> hasBeenScheduled = new ArrayList<Pair>();
    while (current != null) {
      hasBeenScheduled.add(current.getAssign()); 
      current = current.getParent();
    }

    for(Pair selectedPair : hasBeenScheduled){
      for(Unit bsPairUnit : b.getPairs()){
        if(selectedPair.getUnit() == bsPairUnit){
          if(selectedPair.getSlot() != s){
            total += pen;
          }
        }
      }
    }

    return total;
  }

  /**
   * might be a problem with this one
   *
   * @param b
   * @param s
   * @param node
   * @return
   */
  /*
  public static int notPairedCourse(Course b, CourseSlot s, TreeNode node, int pen) {
    ArrayList<Unit> pairs = b.getPairs();
    int numOfCourses = 0;
    if (pairs.size() == 0) {
      return 0;
    }
    int pairsNotAssigned = 0;
    ArrayList<Unit> pairsAssignedInSameSlot = new ArrayList<Unit>();
    ArrayList<Unit> pairsAssignedNotInSameSlot = new ArrayList<Unit>();
    int total = pairs.size();
    TreeNode current = node.getParent();
    while (current != null) {
      Slot ancestorSlot = current.getAssign().getSlot();
      if (s == ancestorSlot) {
        numOfCourses++;
      }
      for (Slot overlap : s.getOverlaps()) {
        if (ancestorSlot == overlap) {
          if (b.getPairs().contains(current.getAssign().getUnit())) {
            total = total - 2;
            pairsAssignedInSameSlot.add(current.getAssign().getUnit());
          }
        } else {
          if (b.getPairs().contains(current.getAssign().getUnit())) {
            pairsAssignedNotInSameSlot.add(current.getAssign().getUnit());
          }
        }
      }
      current = current.getParent();
    }

    for (Unit pair : pairs) {
      if (!pairsAssignedInSameSlot.contains(pair) && !pairsAssignedNotInSameSlot.contains(pair)) {
        pairsNotAssigned++;
      }
    }

    if (pairsNotAssigned <= s.getCourseMax() - numOfCourses) {
      node.incrementDesire(pairsNotAssigned * Kontrol.getWeight_pair());
    } else {
      int num = pairsNotAssigned - (s.getCourseMax() - numOfCourses);
      node.incrementDesire(-num * Kontrol.getWeight_pair());
    }

    node.incrementDesire(-pairsAssignedNotInSameSlot.size() * Kontrol.getWeight_pref());
    node.incrementDesire(pairsAssignedInSameSlot.size() * Kontrol.getWeight_pref());

    return total * pen;

  }
  */

  /**
   * might be a problem with this one
   *
   * @param b
   * @param s
   * @param node
   * @return
   */
  public static int notPairedLab(Lab b, LabSlot s, TreeNode node, int pen) {
    ArrayList<Unit> pairs = b.getPairs();
    int numOfCourses = 0;
    if (pairs.size() == 0) {
      return 0;
    }

    int pairsNotAssigned = 0;
    ArrayList<Unit> pairsAssignedInSameSlot = new ArrayList<Unit>();
    ArrayList<Unit> pairsAssignedNotInSameSlot = new ArrayList<Unit>();
    int total = pairs.size();
    TreeNode current = node.getParent();
    while (current != null) {
      Slot ancestorSlot = current.getAssign().getSlot();
      if (s == ancestorSlot) {
        numOfCourses++;
      }
      for (Slot overlap : s.getOverlaps()) {
        if (ancestorSlot == overlap) {
          if (b.getPairs().contains(current.getAssign().getUnit())) {
            total = total - 2;
            pairsAssignedInSameSlot.add(current.getAssign().getUnit());
          }
        } else {
          if (b.getPairs().contains(current.getAssign().getUnit())) {
            pairsAssignedNotInSameSlot.add(current.getAssign().getUnit());
          }
        }
      }
      current = current.getParent();
    }

    if (pairsNotAssigned <= s.getLabMax() - numOfCourses) {
      node.incrementDesire(pairsNotAssigned * Kontrol.getWeight_pair());
    } else {
      int num = pairsNotAssigned - (s.getLabMax() - numOfCourses);
      node.incrementDesire(-num * Kontrol.getWeight_pair());
    }

    node.incrementDesire(-pairsAssignedNotInSameSlot.size() * Kontrol.getWeight_pref());
    node.incrementDesire(pairsAssignedInSameSlot.size() * Kontrol.getWeight_pref());

    return total * pen;
  }

  /**
   *
   * @param course
   * @param s
   * @return
   */
  public static int checkSections(Course course, CourseSlot s, TreeNode node, int pen) {
    TreeNode current = node.getParent();
    int total = 0;
    if (!course.getBrothers().isEmpty()) {
      while (current != null) {
        if (s == current.getAssign().getSlot()) {
          if (course.getBrothers().contains(current.getAssign().getUnit())) {
            total++;
            node.incrementDesire(-Kontrol.getWeight_section_diff());
          }
        }
        current = current.getParent();
      }
    }
    return total * pen;
  }

}
