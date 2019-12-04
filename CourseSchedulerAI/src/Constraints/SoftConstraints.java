package Constraints;

import schedule.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import coursesULabs.*;

public class SoftConstraints {
  public static int calculatePenalty(Slot s, Unit u, int pen_course_min, int pen_not_paired, int pen_sections) {

    int total = 0;
    if (s instanceof CourseSlot) {
<<<<<<< Updated upstream
      total += SoftConstraints.checkCourseMin((CourseSlot) s, pen_course_min);
      total += SoftConstraints.preferenceEval(s,u);
      total += SoftConstraints.notPairedCourse((Course) u, (CourseSlot) s, pen_not_paired);
      total += SoftConstraints.checkSections((Course) u, (CourseSlot) s, pen_sections);
    } else {
      total += SoftConstraints.checkLabMin((LabSlot) s, pen_course_min);
      total += SoftConstraints.preferenceEval(s,u);
      total += SoftConstraints.notPairedLab((Lab) u, (LabSlot) s, pen_not_paired);
=======
      total += SoftConstraints.checkCourseMin((CourseSlot) s, node);
      // System.out.println("Calculated Course Min");
      total += SoftConstraints.preferenceEval(s, u, node);
      // System.out.println("Calculated Preference");
      total += SoftConstraints.notPairedCourse((Course) u, (CourseSlot) s, node);
      // System.out.println("Calculated Pair");
      total += SoftConstraints.checkSections((Course) u, (CourseSlot) s, node);
      // System.out.println("Calculated Section");
    } else {
      total += SoftConstraints.checkLabMin((LabSlot) s, node);
      // System.out.println("Calculated Lab Min");
      total += SoftConstraints.preferenceEval(s, u, node);
      // System.out.println("Calculated Lab Preference");
      total += SoftConstraints.notPairedLab((Lab) u, (LabSlot) s, node);
      // System.out.println("Calculated Lab Pair");
>>>>>>> Stashed changes

    }
    return total;

  }

<<<<<<< Updated upstream
  public static int checkCourseMin(CourseSlot s, int pen_course_min) {
    //decrement if the course min is met 
    if (s.getCourseMin() > s.getCourseCount()+1) {
      return -pen_course_min;
=======
  public static int checkCourseMin(CourseSlot s, TreeNode node) {
    // decrement if the course min is met and coursemin != 1
    int count = 0;
    

    TreeNode current = node.getParent();

    while (current != null) {
      //// System.out.println("Stuck???");
      
      if (current.getAssign().getSlot().isSameSlot(s)) {
        count++;
      }
      // // System.out.println(current.toString());
      current = current.getParent();
      node.incrementDesire((s.getCourseMin() - count) * Kontrol.getWeight_min_filled());

    }
    if(s.getCourseMin() >= count){
      return -1;
>>>>>>> Stashed changes
    }
    return 0;
  }

  public static int checkLabMin(LabSlot s, int pen_lab_min) {
    //int labMin = s.getLabMin();
    //int labCount = s.getLabCount();

    if (s.getLabMin() > s.getLabCount()+1) {
      return -pen_lab_min;
    }
    return 0;
  }

  public static int preferenceEval(Slot slot, Unit u) {
    HashMap<Slot, Integer> pref = u.getPreferences();
    //slot.isSameSlot(slot);

    int total = 0;
    for(Map.Entry<Slot, Integer> entry : pref.entrySet()){
      if(entry.getKey().isSameSlot(slot)){
        total = total - entry.getValue();
        break;
      }
    }
    return total;
    /*
    int total = 0;
    for (int i = 0; i < slot.getClassAssignment().size(); i++) {
      if (!slot.getPreference().containsKey(slot.getClassAssignment().get(i))) {
        Unit u = slot.getClassAssignment().get(i);
        try {
          total += (Integer) slot.getPreference().get(u);
        } catch (Exception e) {};
      }
    }
    return total;
    */
  }

  public static int notPairedCourse(Course b, CourseSlot s, int not_paired) {
    ArrayList<Unit> pairs = b.getPairs();

    if(pairs.size() == 0){
      return 0;
    }

    ArrayList<Course> lookup = s.getAssignedCourses();
    boolean matched = false;

    for(Unit pair : pairs){
      for (int i = 0; i < lookup.size(); i++) {
        Unit lookupunit = lookup.get(i);
        if (lookupunit.toString().equals(pair.toString())){
          matched = true;
          break;
        }
      }
    }   
    if (matched) {
      return -not_paired;
    } else {
      return 0;
    }
    
  }

  public static int notPairedLab(Lab b, LabSlot s, int not_paired) {
    ArrayList<Unit> pairs = b.getPairs();

    if(pairs.size() == 0){
      return 0;
    }

    ArrayList<Lab> lookup = s.getAssignedLabs();
    boolean matched = false;

    for(Unit pair : pairs){
      for (int i = 0; i < lookup.size(); i++) {
        Unit lookupunit = lookup.get(i); 
        if (lookupunit.toString().equals(pair.toString())){
          matched = true;
          break;
        }
      }
    }   
    if (matched) {
      return -not_paired;
    } else {
      return 0;
    }
    
  }
  
  public static int checkSections(Course course, CourseSlot s, int pen){
    int curr_pen = 0;
    ArrayList<Course> lookup = s.getAssignedCourses();

    for(int i = 0; i < lookup.size(); i++){
      int lookupNum = lookup.get(i).getCourseNum();
      int courseNum = course.getCourseNum();
      String lookupType = lookup.get(i).getCourseType();
      String courseType = course.getCourseType();
      if(lookupNum == courseNum && lookupType.equals(courseType)){
        curr_pen = curr_pen + pen;
      }
    }
    return curr_pen;
  }
}
