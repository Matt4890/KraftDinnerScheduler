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

  /**
   * This method should only be called on the bottom nodes of the tree
   * 
   * What this does is it calculates the accumulated course min penalty of all the 
   * courses that have been assigned a slot 
   * 
   * @param s
   * @param node
   * @param pen
   * @param allSlots
   * @return accumulated course min penalty 
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

  /**
   * This method should only be called on the bottom nodes of the tree
   * 
   * What this does is it calculates the accumulated lab min penalty of all the 
   * labs that have been assigned a slot 
   * 
   * @param s
   * @param node
   * @param pen
   * @param allSlots
   * @return accumulated lab min penalty 
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

  /**
   * Gets the nodes preference times, and adds a penalty if the slot we want to assign it to 
   * is not this nodes preference time. If this slot is a preference slot for this node we check
   * if it has any other preferences and we return the sum of those preference penalties 
   * 
   * @param slot
   * @param u
   * @param node
   * @return preference Eval penalty  
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
  }

  /**
   * Check if this course has any pairs. If it does check that this course is being assigned with its pair.
   * If it isn't we apply a penalty value to it.
   * 
   * @param b
   * @param s
   * @param node
   * @param pen
   * @return paired penalty 
   */
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
   * Check if this lab has any pairs. If it does check that this lab is being assigned with its pair.
   * If it isn't we apply a penalty value to it. 
   * 
   * @param b
   * @param s
   * @param node
   * @param pen
   * @return paired penalty 
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
   * Check if the slot we are assigning this course in already contains the same course but
   * different lec
   * 
   * @param course
   * @param s
   * @param node
   * @param pen
   * @return section penalty
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
