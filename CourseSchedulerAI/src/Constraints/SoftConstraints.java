package Constraints;

import schedule.*;

import java.util.ArrayList;

import coursesULabs.*;

public class SoftConstrainsts {

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
      for (int i = 0; i < slot.classAssignment.size(); i++) {
          if (!slot.getPreference().containsKey(slot.classAssignment.get(i))) {
              total += (Integer)slot.getPreference().get(slot.classAssignment.get(i));
          }
      }
      return total;
  }

  //public int notPaired(ArrayList<Unit> list, CourseSlot s, int not_paired){
    //ArrayList<Lab> lookup = s.getAssignedLabs();
    //for(int i=0; i<list.size(); i++){
      //for(int j=0; j<list.size();j++){
      //  if(list.get(i))
      //}
    //}
  //}

}
