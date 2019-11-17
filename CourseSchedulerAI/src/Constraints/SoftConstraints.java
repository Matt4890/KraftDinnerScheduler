package Constraints;

import schedule.*;

import java.util.ArrayList;

import coursesULabs.*;

public class SoftConstraints {
  public static int calculatePenalty(Slot s, Unit u){
    //TODO: Implement this pls
    return 0;

  }

  public int checkCourseMin(CourseSlot s, int pen_course_min){
    if(s.getCourseMin()> s.getCourseCount()){
      return pen_course_min;
    }
    return 0;
  }

  public int checkLabMin(LabSlot s, int pen_lab_min){
    if(s.getLabMin()>s.getLabCount()){
      return pen_lab_min;
    }
    return 0;
  }

  public int preferenceEval(Slot slot) {
      int total = 0;
      for (int i = 0; i < slot.getClassAssignment().size(); i++) {
          if (!slot.getPreference().containsKey(slot.getClassAssignment().get(i))) {
              total += (Integer)slot.getPreference().get(slot.getClassAssignment().get(i));
          }
      }
      return total;
  }

  public int notPairedCourse(Course b, CourseSlot s, int not_paired){
    ArrayList<Course> lookup = s.getAssignedCourses();
    boolean matched = false;

    for(int i = 0; i < lookup.size(); i++){
        if(lookup.get(i) == b){
            matched = true;
            break;
        }
    }
    if(matched) return 0;
    else{
      return not_paired;
    }

}


public int notPairedLab(Lab b, LabSlot s, int not_paired){
  ArrayList<Lab> lookup = s.getAssignedLabs();
  boolean matched = false;

  for(int i = 0; i < lookup.size(); i++){
      if(lookup.get(i) == b){
          matched = true;
          break;
      }
  }
  if(matched) return 0;
  else{
    return not_paired;
  }

}
