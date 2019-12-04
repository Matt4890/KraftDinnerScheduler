package Constraints;

import schedule.*;
import tree.TreeNode;

import java.util.*;

import coursesULabs.*;
import enums.*;

/*
Labcount is never updated.
Implement the special courses ie 913 813 313 and 413 probably best if we just add them to the not compatible list
*/
public class HardConstrainsts {

    public static boolean checkAssignmentHardConstriantsCourse(Course course, CourseSlot slot, TreeNode node) {
        return checkCourseMax(course, slot, node) && checkEveningClasses(course, slot)
                && checkConflict500Levels(course, slot, node)
                // && checkConflictsWithSameName(course, slot)
                && checkLabNotWithCourseAddingCourse(course, slot, node)
                // && checkDuplicateCourse(course, slot)
                && checkUnwanted(course, slot) && checkNotCompatible(course, slot, node);
    }

    public static boolean checkCourseMax(Course c, CourseSlot s, TreeNode node) {
        int count = 0;
        TreeNode current = node;
        while (current != null) {
            if (current.getAssign().getSlot() == s) {
                count++;
            }
            current = current.getParent();
        }
        if (count > s.getCourseMax()) {
            //System.out.println("Max failed");
            return false;
        }
        return true;
    }

    /**
     * Not sure what it does so I won't touch it Make sure 2 lectures of the same
     * course aren't in the same slot? Should definitly be in soft constraints
     * 
     * @param c
     * @param s
     * @return true if no hard constraint broken
     */
    public static boolean checkDuplicateCourse(Course c, CourseSlot s) {
        // Go through courses in slot to see if theres a duplicate
        // Right now its a runthrough but ideally once its implemented correctly it
        // should be a lookup??????????????
        final ArrayList<Course> lookup = s.getAssignedCourses();
        for (int i = 0; i < lookup.size(); i++) {
            if (lookup.get(i).getCourseNum() == c.getCourseNum()
                    && lookup.get(i).getCourseType().equals(c.getCourseType())) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkEveningClasses(Course c, CourseSlot s) {
        if (Integer.toString(c.getLectureNum()).substring(0, 1).equals("9")) {
            // Check evening class
            if (s.getTime() >= 1800) {

            }
            return s.getTime() >= 1800;
        }
        return true;
    }

    public static boolean checkConflict500Levels(final Course c, final CourseSlot s, TreeNode node) {
        if (c.getCourseNum() >= 500 && c.getCourseNum() < 600) {
            TreeNode current = node.getParent();
            while (current != null) {
                if (s == current.getAssign().getSlot() && current.getAssign().getUnit().getCourseNum() >= 500
                        && current.getAssign().getUnit().getCourseNum() < 600) {
                            //System.out.println("500 failed");

                    return false;
                }
                current = current.getParent();
            }
        }
        return true;
    }

    /**
     * Shouldn't this be in soft constraints
     * 
     * @param c
     * @param s
     * @return
     */
    public static boolean checkConflictsWithSameName(Unit c, Slot s) {
        for (Slot slot : s.getOverlaps()) {
            for (Unit unit : slot.getClassAssignment()) {
                if (unit.getCourseNum() == c.getCourseNum() && unit.getCourseType().equals(c.getCourseType())) {
                    return false;
                }
            }
        }
        return true;

    }

    public static boolean checkLabNotWithCourseAddingCourse(Course c, Slot s, TreeNode node) {
        TreeNode current = node.getParent();
        while (current != null) {
            Slot ancestorSlot = current.getAssign().getSlot();
            for (Slot slot : s.getOverlaps()) {
                if (ancestorSlot == slot) {
                    Unit unit = current.getAssign().getUnit();
                    if (unit.getCourseNum() == c.getCourseNum()) {
                        if (unit.getCourseType().equals(c.getCourseType())) {
                            if (unit.getLectureNum() == 0) {
                                System.out.println("Problem is conflicting lab");
            System.out.println("lab course failed");
                                
                                return false;
                            } else if (unit.getLectureNum() == c.getLectureNum()) {
                                System.out.println("Problem is conflicting lab");
                                return false;
                            }

                        }
                    }
                }
            }
            current = current.getParent();
        }
        return true;
    }

    public static boolean checkAssignmentHardConstriantsLab(final Lab lab, final LabSlot slot, TreeNode node) {
        return checkLabMax(lab, slot, node) // && checkDuplicateLab(lab, slot)
                // && checkConflictsWithSameName(lab, slot)
                && checkUnwanted(lab, slot) && checkNotCompatible(lab, slot, node)
                && checkLabNotWithCourseAddingLab(lab, slot, node);
    }

    public static boolean checkLabMax(final Lab c, final LabSlot s, TreeNode node) {
        int count = 0;
        TreeNode current = node.getParent();
        while (current != null) {
            if (current.getAssign().getSlot() == s) {
                count++;
            }
            current = current.getParent();
        }
        if (count >= s.getLabMax()) {
            //System.out.println("Max failed");

            return false;
        }
        return true;
    }

    public static boolean checkDuplicateLab(final Lab c, final LabSlot s) {
        // Go through courses in slot to see if theres a duplicate
        // Right now its a runthrough but ideally once its implemented correctly it
        // should be a lookup??????????????
        boolean result = true;
        final ArrayList<Lab> lookup = s.getAssignedLabs();
        for (int i = 0; i < lookup.size(); i++) {
            if (lookup.get(i).getCourseType().equals(c.getCourseType())) {
                result = false;
            }
        }
        return result;

    }

    /**
     * given a course and a slot to add it to, will return true if unwanted is not
     * broken
     * 
     * @param course
     * @param slotToAdd
     * @return
     */
    public static boolean checkUnwanted(Unit course, Slot slotToAdd) {
        if (course.getUnwanted().contains(slotToAdd)) {
            //System.out.println("unwanted failed");

            return false;
        } else if (course instanceof Course) {
            if (((CourseSlot) slotToAdd).getDay().equals(CourseDays.TUETHR)) {
                if (slotToAdd.getTime() == 1100) {
                    //System.out.println("Problem is 1100");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * given a course and a slot to add it to it will return true if not compatible
     * is not broken
     * 
     * @param course
     * @param slotToAddTo
     * @return
     */
    public static boolean checkNotCompatible(Unit course, Slot slotToAddTo, TreeNode node) {
        TreeNode current = node.getParent();
        while (current != null) {
            Slot ancestorSlot = current.getAssign().getSlot();
            for (Slot overlap : slotToAddTo.getOverlaps()) {
                if (ancestorSlot == overlap) {
                    if (course.getNotCompatible().contains(current.getAssign().getUnit())) {
                        
            //System.out.println("not compatible failed");
            return false;
                    }
                }
            }
            current = current.getParent();
        }
        return true;
    }

    public static boolean checkLabNotWithCourseAddingLab(Lab c, Slot s, TreeNode node) {
        TreeNode current = node.getParent();
        while (current != null) {
            Slot ancestorSlot = current.getAssign().getSlot();
            for (Slot slot : s.getOverlaps()) {
                if (ancestorSlot == slot) {
                    Unit unit = current.getAssign().getUnit();
                    if (unit.getCourseNum() == c.getCourseNum()) {
                        if (unit.getCourseType().equals(c.getCourseType())) {
                            if (c.getLectureNum() == 0) {
                                //System.out.println("Problem is conflicting lab");
                                
                                
                                return false;
                            } else if (unit.getLectureNum() == c.getLectureNum()) {
                                //System.out.println("Problem is conflicting lab");
                                return false;
                            }

                        }
                    }
                }
            }
            current = current.getParent();
        }
        return true;
    }
}
