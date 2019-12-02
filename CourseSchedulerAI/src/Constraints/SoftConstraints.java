package Constraints;

import schedule.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import coursesULabs.*;

public class SoftConstraints {
  public static int calculatePenalty(Slot s, Unit u) {

    int total = 0;
    if (s instanceof CourseSlot) {
      total += SoftConstraints.checkCourseMin((CourseSlot) s);
      total += SoftConstraints.preferenceEval(s,u);
      total += SoftConstraints.notPairedCourse((Course) u, (CourseSlot) s);
      total += SoftConstraints.checkSections((Course) u, (CourseSlot) s);
    } else {
      total += SoftConstraints.checkLabMin((LabSlot) s);
      total += SoftConstraints.preferenceEval(s,u);
      total += SoftConstraints.notPairedLab((Lab) u, (LabSlot) s);

    }
    return total;

  }

  public static int checkCourseMin(CourseSlot s) {
    //decrement if the course min is met 
    if (s.getCourseMin() == s.getCourseCount()+1) {
      return -1;
    }
    if (s.getCourseMin() > s.getCourseCount()){
      return 1;
    }
    return 0;
  }

  public static int checkLabMin(LabSlot s) {
    //int labMin = s.getLabMin();
    //int labCount = s.getLabCount();

    if (s.getLabMin() == s.getLabCount()+1) {
      return -1;
    }
    if (s.getLabMin() > s.getLabCount()){
      return 1;
    }
    return 0;
  }

  public static int preferenceEval(Slot slot, Unit u) {
    HashMap<Slot, Integer> pref = u.getPreferences();
    //slot.isSameSlot(slot);

    int total = 0;
    for(Map.Entry<Slot, Integer> entry : pref.entrySet()){
      System.out.println("The slot: " + slot.toString() + " compared with " + entry.getKey().toString() + " checked to be the same is:" + entry.getKey().isSameSlot(slot));
      if(entry.getKey().isSameSlot(slot)){

        total = total - entry.getValue();
        break;
      }
    }
    //WHERE DO WE EVER ADD THE PREFERENCE IF ITS NOT THERE???

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

  public static int notPairedCourse(Course b, CourseSlot s) {
    ArrayList<Unit> pairs = b.getPairs();

    if(pairs.size() == 0){
      return 0;
    }

    ArrayList<Course> lookup = s.getAssignedCourses();
    boolean matched = false;

    for(Unit pair : pairs){
      for (int i = 0; i < lookup.size(); i++) {
        Unit lookupunit = lookup.get(i);
        if (lookupunit == pair){
          matched = true;
          break;
        }
      }
    }   
    if (matched) {
      return -1;
    } else {
      return 1; // Shouldn't this be adding the penalty??
    }
    
  }

  public static int notPairedLab(Lab b, LabSlot s) {
    ArrayList<Unit> pairs = b.getPairs();

    if(pairs.size() == 0){
      return 0;
    }

    ArrayList<Lab> lookup = s.getAssignedLabs();
    boolean matched = false;

    for(Unit pair : pairs){
      for (int i = 0; i < lookup.size(); i++) {
        Unit lookupunit = lookup.get(i); 
        if (lookupunit==pair){
          matched = true;
          break;
        }
      }
    }   
    if (matched) {
      return -1;
    } else {
      return 1;
    }
    
  }
  
  public static int checkSections(Course course, CourseSlot s){
    int curr_pen = 0;
    ArrayList<Course> lookup = s.getAssignedCourses();

    for(int i = 0; i < lookup.size(); i++){
      int lookupNum = lookup.get(i).getCourseNum();
      int courseNum = course.getCourseNum();
      String lookupType = lookup.get(i).getCourseType();
      String courseType = course.getCourseType();
      if(lookupNum == courseNum && lookupType.equals(courseType)){
        curr_pen = curr_pen + 1;
      }
    }
    return curr_pen;
  }
}
