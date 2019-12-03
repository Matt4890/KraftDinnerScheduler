package Constraints;

import schedule.*;
import tree.Tree;
import tree.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import coursesULabs.*;

public class SoftConstraints {
  public static int calculatePenalty(Slot s, Unit u, TreeNode node) {

    int total = 0;
    if (s instanceof CourseSlot) {
      total += SoftConstraints.checkCourseMin((CourseSlot) s, node);
      System.out.println("Calculated Course Min");
      total += SoftConstraints.preferenceEval(s, u);
      System.out.println("Calculated Preference");
      total += SoftConstraints.notPairedCourse((Course) u, (CourseSlot) s, node);
      System.out.println("Calculated Pair");
      total += SoftConstraints.checkSections((Course) u, (CourseSlot) s, node);
      System.out.println("Calculated Section");
    } else {
      total += SoftConstraints.checkLabMin((LabSlot) s, node);
      System.out.println("Calculated Lab Min");
      total += SoftConstraints.preferenceEval(s, u);
      System.out.println("Calculated Lab Preference");
      total += SoftConstraints.notPairedLab((Lab) u, (LabSlot) s, node);
      System.out.println("Calculated Lab Pair");

    }
    return total;

  }

  public static int checkCourseMin(CourseSlot s, TreeNode node) {
    // decrement if the course min is met and coursemin != 1
    int count = 0;
    TreeNode current = node;
    while (current != null) {
      System.out.println("Stuck???");
      if (current.getAssign().getSlot() == s) {
        count++;
      }
      current = node.getParent();
    }

    if (s.getCourseMin() != 1 && s.getCourseMin() == count + 1) {
      return -1;
    }
    if (s.getCourseMin() > count + 1) {
      return 1;
    }
    return 0;
  }

  public static int checkLabMin(LabSlot s, TreeNode node) {
    // int labMin = s.getLabMin();
    // int labCount = s.getLabCount();
    int count = 0;
    TreeNode current = node;
    while (current != null) {
      if (current.getAssign().getSlot() == s) {
        count++;
      }
      current = current.getParent();
    }

    if (s.getLabMin() != 1 && s.getLabMin() == count + 1) {
      return -1;
    }
    if (s.getLabMin() > count + 1) {
      return 1;
    }
    return 0;
  }

  public static int preferenceEval(Slot slot, Unit u) {
    HashMap<Slot, Integer> pref = u.getPreferences();
    int total = 0;
    if (!pref.isEmpty()) {
      for (Map.Entry<Slot, Integer> entry : pref.entrySet()) {
        if (entry.getKey() != slot) {
          total += entry.getValue();
        }
      }
    }

    return total;
    /*
     * int total = 0; for (int i = 0; i < slot.getClassAssignment().size(); i++) {
     * if (!slot.getPreference().containsKey(slot.getClassAssignment().get(i))) {
     * Unit u = slot.getClassAssignment().get(i); try { total += (Integer)
     * slot.getPreference().get(u); } catch (Exception e) {}; } } return total;
     */
  }

  /**
   * might be a problem with this one
   * 
   * @param b
   * @param s
   * @param node
   * @return
   */
  public static int notPairedCourse(Course b, CourseSlot s, TreeNode node) {
    ArrayList<Unit> pairs = b.getPairs();
    if (pairs.size() == 0) {
      return 0;
    }
    int total = pairs.size();
    TreeNode current = node;
    while (current != null) {
      Slot ancestorSlot = current.getAssign().getSlot();
      for (Slot overlap : s.getOverlaps()) {
        if (ancestorSlot == overlap) {
          if (b.getPairs().contains(current.getAssign().getUnit())) {
            total = total - 2;
          }
        }
      }
      current = current.getParent();
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
  public static int notPairedLab(Lab b, LabSlot s, TreeNode node) {
    ArrayList<Unit> pairs = b.getPairs();
    if (pairs.size() == 0) {
      return 0;
    }
    int total = pairs.size();
    TreeNode current = node;
    while (current != null) {
      Slot ancestorSlot = current.getAssign().getSlot();
      for (Slot overlap : s.getOverlaps()) {
        if (ancestorSlot == overlap) {
          if (b.getPairs().contains(current.getAssign().getUnit())) {
            total = total - 2;
          }
        }
      }
      current = current.getParent();
    }
    return total;
  }

  /**
   * 
   * @param course
   * @param s
   * @return
   */
  public static int checkSections(Course course, CourseSlot s, TreeNode node) {
    TreeNode current = node;
    int total = 0;
    while (current != null) {
      if (s == current.getAssign().getSlot()) {
        if (course.getBrothers().contains(current.getAssign().getUnit())) {
          total++;
        }
      }
      current = current.getParent();
    }
    return total;
  }

}
