package tree;

import schedule.*;
import coursesULabs.*;
import Constraints.*;

public class Kontrol {
    private static int weight_min_filled;
    private static int weight_pref;
    private static int weight_pair;
    private static int weight_section_diff;

    private static int pen_course_min = 5;
    private static int pen_lab_min = 3;
    private static int pen_not_paired = 2;

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
            total += (((CourseSlot)s).getCourseMax() - (((CourseSlot)s).getCourseMax() - s.getClassAssignment().size()));
        }
        else{
            total -= (((LabSlot)s).getLabMin() - s.getClassAssignment().size());
            total += (((LabSlot)s).getLabMax() - (((LabSlot)s).getLabMax() - s.getClassAssignment().size()));
        }
        total = total / u.getConstrained();

        return total;
    }

    //TODO: Create a method that uses the bound control which will be different than the desirablility 
    // which is used in the actual branch and bound. The bound controls should be good but simplistic
}