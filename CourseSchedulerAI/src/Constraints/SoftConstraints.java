package Constraints;

import schedule.*;

import java.util.ArrayList;

import coursesULabs.*;

public class SoftConstraints {
  public static int calculatePenalty(Slot s, Unit u, int pen_course_min, int pen_not_paired, int pen_sections) {

    int total = 0;
    if (s instanceof CourseSlot) {
      total += SoftConstraints.checkCourseMin((CourseSlot) s, pen_course_min);
      total += SoftConstraints.preferenceEval(s);
      total += SoftConstraints.notPairedCourse((Course) u, (CourseSlot) s, pen_not_paired);
      total += SoftConstraints.checkSections((Course) u, (CourseSlot) s, pen_sections);
    } else {
      total += SoftConstraints.checkLabMin((LabSlot) s, pen_course_min);
      total += SoftConstraints.preferenceEval(s);
      total += SoftConstraints.notPairedLab((Lab) u, (LabSlot) s, pen_not_paired);

    }
    return total;

  }

  public static int checkCourseMin(CourseSlot s, int pen_course_min) {
    if (s.getCourseMin() > s.getCourseCount()) {
      return pen_course_min;
    }
    return 0;
  }

  public static int checkLabMin(LabSlot s, int pen_lab_min) {
    if (s.getLabMin() > s.getLabCount()) {
      return pen_lab_min;
    }
    return 0;
  }

  public static int preferenceEval(Slot slot) {
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
  }

  public static int notPairedCourse(Course b, CourseSlot s, int not_paired) {
    ArrayList<Course> lookup = s.getAssignedCourses();
    boolean matched = false;

    for (int i = 0; i < lookup.size(); i++) {
      if (lookup.get(i) == b) {
        matched = true;
        break;
      }
    }
    if (matched)
      return 0;
    else {
      return not_paired;
    }

  }

  public static int notPairedLab(Lab b, LabSlot s, int not_paired) {
    ArrayList<Lab> lookup = s.getAssignedLabs();
    boolean matched = false;

    for (int i = 0; i < lookup.size(); i++) {
      if (lookup.get(i) == b) {
        matched = true;
        break;
      }
    }
    if (matched) {
      return 0;
    } else {
      return not_paired;
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
