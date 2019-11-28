package tree;

import schedule.*;
import coursesULabs.*;
import Constraints.*;

public class Kontrol {
    private static int weight_min_filled = 1;
    private static int weight_pref = 1 ;
    private static int weight_pair;
    private static int weight_section_diff;

    private static int pen_course_min = 0;
    private static int pen_lab_min = 0;
    private static int pen_not_paired = 0;
    private static int pen_section_diff = 0;

    public static int evalAssignment(Slot s, Unit u) {
        // Take the value of the soft constraint functions of an assignment
        // and apply weights to them based on whatever we decide
        int total = 0;

        if (s instanceof CourseSlot) {
            total += SoftConstraints.checkCourseMin((CourseSlot) s, pen_course_min) * weight_min_filled;
            total += SoftConstraints.preferenceEval(s) * weight_pref;
            total += SoftConstraints.notPairedCourse((Course) u, (CourseSlot) s, pen_not_paired) * weight_pair;
            total += SoftConstraints.checkSections((Course) u, (CourseSlot) s, pen_section_diff);
            
        } else {
            total += SoftConstraints.checkLabMin((LabSlot) s, pen_lab_min) * weight_min_filled;
            total += SoftConstraints.preferenceEval(s) * weight_pref;
            total += SoftConstraints.notPairedLab((Lab) u, (LabSlot) s, pen_not_paired) * weight_pair;
        }
        return total;
    }

    /**
     * the desireability is based on the following
     * eval assignment of it
     * the closer to unit min the better
     * the further from unit max the better
     * total will then be divided by constrained 
     * the closer the number is to 0 the higher the desireability
     * should be noted that partassign assigns a huge constrained value so most likely desireability will be
     * close or equal to 0 
     * @param s
     * @param u
     * @return desireability of assigning unit u into slot s
     */
    public static int desireability(Slot s, Unit u) {
        int total = 0;
        total += evalAssignment(s,u);
        if(u instanceof Course){
            total -= (((CourseSlot)s).getCourseMin() - s.getClassAssignment().size());
            total += (((CourseSlot)s).getCourseMax() - (((CourseSlot)s).getCourseMax() - s.getClassAssignment().size())) * 5;
        }
        else{
            total -= (((LabSlot)s).getLabMin() - s.getClassAssignment().size());
            total += (((LabSlot)s).getLabMax() - (((LabSlot)s).getLabMax() - s.getClassAssignment().size())) * 5;
        }
        total = total / u.getConstrained();

        return total;
    }

    public static int getWeight_min_filled() {
        return weight_min_filled;
    }

    public static void setWeight_min_filled(int weight_min_filled) {
        Kontrol.weight_min_filled = weight_min_filled;
    }

    public static int getWeight_pref() {
        return weight_pref;
    }

    public static void setWeight_pref(int weight_pref) {
        Kontrol.weight_pref = weight_pref;
    }

    public static int getWeight_pair() {
        return weight_pair;
    }

    public static void setWeight_pair(int weight_pair) {
        Kontrol.weight_pair = weight_pair;
    }

    public static int getWeight_section_diff() {
        return weight_section_diff;
    }

    public static void setWeight_section_diff(int weight_section_diff) {
        Kontrol.weight_section_diff = weight_section_diff;
    }

    public static int getPen_course_min() {
        return pen_course_min;
    }

    public static void setPen_course_min(int pen_course_min) {
        Kontrol.pen_course_min = pen_course_min;
    }

    public static int getPen_lab_min() {
        return pen_lab_min;
    }

    public static void setPen_lab_min(int pen_lab_min) {
        Kontrol.pen_lab_min = pen_lab_min;
    }

    public static int getPen_not_paired() {
        return pen_not_paired;
    }

    public static void setPen_not_paired(int pen_not_paired) {
        Kontrol.pen_not_paired = pen_not_paired;
    }

    public static int getPen_section_diff() {
        return pen_section_diff;
    }

    public static void setPen_section_diff(int pen_section_diff) {
        Kontrol.pen_section_diff = pen_section_diff;
    }

  

    //TODO: Create a method that uses the bound control which will be different than the desirablility 
    // which is used in the actual branch and bound. The bound controls should be good but simplistic
}