package tree;

import schedule.*;
import coursesULabs.*;
import Constraints.*;

public class Kontrol {
    private static int weight_min_filled;
    private static int weight_pref;
    private static int weight_pair;
    private static int weight_section_diff;

    private static int pen_course_min;
    private static int pen_lab_min;
    private static int pen_not_paired;

    public static int evalAssignment(Slot s, Unit u) {
        // Take the value of the soft constraint functions of an assignment
        // and apply weights to them based on whatever we decide
        int total = 0;

        if (s instanceof CourseSlot) {
            total += SoftConstraints.checkCourseMin((CourseSlot) s, pen_course_min) * weight_min_filled;
            total += SoftConstraints.preferenceEval(s) * weight_pref;
            total += SoftConstraints.notPairedCourse((Course) u, (CourseSlot) s, pen_not_paired) * weight_pair;
            //TODO: SectionDif Constraint
        } else {
            total += SoftConstraints.checkLabMin((LabSlot) s, pen_lab_min) * weight_min_filled;
            total += SoftConstraints.preferenceEval(s) * weight_pref;
            total += SoftConstraints.notPairedLab((Lab) u, (LabSlot) s, pen_not_paired) * weight_pair;
        }
        return total;
    }

    public static int desireability(Slot s, Unit u) {
        // Take the eval plus some other factors and
        // Or we can just go strictly with the eval of that function
        // Select lowest eval with those weights

        return evalAssignment(s,u);
    }

    //Create a method that uses the bound control which will be different than the desirablility which is used in the actual branch and bound 
    //The bound controls should be good but simplistic
}