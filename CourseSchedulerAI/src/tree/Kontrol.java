package tree;

import schedule.*;
import coursesULabs.*;

import java.util.ArrayList;

import Constraints.*;

public class Kontrol {
    private static int weight_min_filled;
    private static int weight_pref;
    private static int weight_pair;
    private static int weight_section_diff;
    private static int pen_min_filled;
    private static int pen_labmin_filled;
    private static int pen_pair;
    private static int pen_section_diff;
    private static ArrayList<Slot> allSlots;

    public static int evalAssignmentPairing(Slot s, Unit u, TreeNode node) {

        int total = 0;
        if (s instanceof CourseSlot) {
            // total += SoftConstraints.checkCourseMin((CourseSlot) s, node, pen_min_filled)
            // * Kontrol.weight_min_filled;
            total += SoftConstraints.preferenceEval(s, u, node) * Kontrol.weight_pref;
            total += SoftConstraints.notPairedCourse((Course) u, (CourseSlot) s, node, pen_pair) * Kontrol.weight_pair;
            total += SoftConstraints.checkSections((Course) u, (CourseSlot) s, node, pen_section_diff)
                    * Kontrol.weight_section_diff;
        } else {
            // total += SoftConstraints.checkLabMin((LabSlot) s, node, pen_labmin_filled) *
            // Kontrol.weight_min_filled;

            total += SoftConstraints.preferenceEval(s, u, node) * Kontrol.weight_pref;

            total += SoftConstraints.notPairedLab((Lab) u, (LabSlot) s, node, pen_pair) * Kontrol.weight_pair;

        }
        return total;

    }

    public static int calculateMin(TreeNode node, Slot s, Unit u) {
        int total = SoftConstraints.checkCourseMin(s, node, pen_min_filled, allSlots) * Kontrol.weight_min_filled;
        total += SoftConstraints.checkLabMin(s, node, pen_labmin_filled, allSlots)* Kontrol.weight_min_filled;
        return total;
    }

    /**
     * the desireability is based on the following eval assignment of it the closer
     * to unit min the better the further from unit max the better total will then
     * be divided by constrained the closer the number is to 0 the higher the
     * desireability should be noted that partassign assigns a huge constrained
     * value so most likely desireability will be close or equal to 0
     * 
     * @param s
     * @param u
     * @return desireability of assigning unit u into slot s
     */
    public static int desireability(Slot s, Unit u, TreeNode n) {
        int total = 0;
        total += evalAssignmentPairing(s, u, n);
        if (u instanceof Course) {
            total -= (((CourseSlot) s).getCourseMin() - s.getClassAssignment().size());
            total += (((CourseSlot) s).getCourseMax()
                    - (((CourseSlot) s).getCourseMax() - s.getClassAssignment().size())) * 5;
        } else {
            total -= (((LabSlot) s).getLabMin() - s.getClassAssignment().size());
            total += (((LabSlot) s).getLabMax() - (((LabSlot) s).getLabMax() - s.getClassAssignment().size())) * 5;
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

    public static int getPen_min_filled() {
        return pen_min_filled;
    }

    public static void setPen_min_filled(int pen_min_filled) {
        Kontrol.pen_min_filled = pen_min_filled;
    }

    public static int getPen_pair() {
        return pen_pair;
    }

    public static void setPen_pair(int pen_pair) {
        Kontrol.pen_pair = pen_pair;
    }

    public static int getPen_section_diff() {
        return pen_section_diff;
    }

    public static void setPen_section_diff(int pen_section_diff) {
        Kontrol.pen_section_diff = pen_section_diff;
    }

    public static int getPen_labmin_filled() {
        return pen_labmin_filled;
    }

    public static void setPen_labmin_filled(int pen_labmin_filled) {
        Kontrol.pen_labmin_filled = pen_labmin_filled;
    }

    public static ArrayList<Slot> getAllSlots() {
        return allSlots;
    }

    public static void setAllSlots(ArrayList<Slot> allSlots) {
        Kontrol.allSlots = allSlots;
    }

    // TODO: Create a method that uses the bound control which will be different
    // than the desirablility
    // which is used in the actual branch and bound. The bound controls should be
    // good but simplistic
}