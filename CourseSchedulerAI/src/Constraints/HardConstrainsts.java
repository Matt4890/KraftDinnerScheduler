package Constraints;

import schedule.*;
import tree.TreeNode;

import java.util.*;

import coursesULabs.*;
import enums.*;


public class HardConstrainsts {

    public static boolean checkAssignmentHardConstriantsCourse(Course course, CourseSlot slot, TreeNode node) {
        return checkCourseMax(course, slot, node) && checkEveningClasses(course, slot)
                && checkConflict500Levels(course, slot, node) && checkLabNotWithCourseAddingCourse(course, slot, node)
                && checkUnwanted(course, slot) && checkNotCompatible(course, slot, node);
    }
    /**
     * Checks whether adding the course will break the coursemax 
     * 
     * @param c course to add
     * @param s slot to add to
     * @param node access to rest of schedule 
     * @return true if the slot can hold the course, 
     */
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
            // System.out.println("Max failed");
            return false;
        }
        return true;
    }

    /**
     * Check to make sure if course is an evening course (its lecture number begins
     * with 9) check that it is only being assigned to slots at or after 18:00
     * 
     * @param c course to add
     * @param s slot to add to
     * @return true if the constraint is met
     */
    public static boolean checkEveningClasses(Course c, CourseSlot s) {
        if (Integer.toString(c.getLectureNum()).substring(0, 1).equals("9")) {
            // Check evening class
            if (s.getTime() >= 1800) {

            }
            return s.getTime() >= 1800;
        }
        return true;
    }

    /**
     * Check to see if assignment would cause two 500 levels to be in the same slot
     * 
     * @param c    course to add
     * @param s    slot to add to
     * @param node access the rest of the current schedule
     * @return true if there are no conflicting 500 level classes
     */
    public static boolean checkConflict500Levels(final Course c, final CourseSlot s, TreeNode node) {
        if (c.getCourseNum() >= 500 && c.getCourseNum() < 600) {
            TreeNode current = node.getParent();
            while (current != null) {
                if (s == current.getAssign().getSlot() && current.getAssign().getUnit().getCourseNum() >= 500
                        && current.getAssign().getUnit().getCourseNum() < 600) {
                    // System.out.println("500 failed");

                    return false;
                }
                current = current.getParent();
            }
        }
        return true;
    }

    /**
     * 
     * When Adding a Course check to see if the corresponding lab(s) are in the same
     * slot
     * 
     * @param c
     * @param s
     * @param node
     * @return true indicates the hard constraint check was passed i.e. the lab(s)
     *         are not in the same slot
     */
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
                                // System.out.println("Problem is conflicting lab");
                                // System.out.println("lab course failed");

                                return false;
                            } else if (unit.getLectureNum() == c.getLectureNum()) {
                                // System.out.println("Problem is conflicting lab");
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

    /**
     * Check if the lab assinged to the slot passes the hard constraints
     * 
     * @param lab
     * @param slot
     * @param node
     * @return true if assignment is valid for the given schedule
     */
    public static boolean checkAssignmentHardConstriantsLab(final Lab lab, final LabSlot slot, TreeNode node) {
        return checkLabMax(lab, slot, node) && checkUnwanted(lab, slot) && checkNotCompatible(lab, slot, node)
                && checkLabNotWithCourseAddingLab(lab, slot, node);
    }

    /**
     * 
     * @param c    lab to add
     * @param s    slot to add to
     * @param node node to check from
     * @return
     */
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
            // System.out.println("Max failed");

            return false;
        }
        return true;
    }

    /**
     * given a course and a slot to add it to, will return true if unwanted
     * constraint is not broken
     * 
     * @param course    course to add
     * @param slotToAdd slot to add to
     * @return true indicates the hard constraint check was passed
     */
    public static boolean checkUnwanted(Unit course, Slot slotToAdd) {
        if (course.getUnwanted().contains(slotToAdd)) {
            // System.out.println("unwanted failed");

            return false;
        } else if (course instanceof Course) {
            if (((CourseSlot) slotToAdd).getDay().equals(CourseDays.TUETHR)) {
                if (slotToAdd.getTime() == 1100) {
                    // System.out.println("Problem is 1100");
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
     * @return true indicates the hard constraint check was passed
     */
    public static boolean checkNotCompatible(Unit course, Slot slotToAddTo, TreeNode node) {
        TreeNode current = node.getParent();
        while (current != null) {
            Slot ancestorSlot = current.getAssign().getSlot();
            for (Slot overlap : slotToAddTo.getOverlaps()) {
                if (ancestorSlot == overlap) {
                    if (course.getNotCompatible().contains(current.getAssign().getUnit())) {

                        // System.out.println("not compatible failed");
                        return false;
                    }
                }
            }
            current = current.getParent();
        }
        return true;
    }

    /**
     * 
     * When Adding a Lab check to see if the corresponding course is in the same
     * slot
     * 
     * @param c
     * @param s
     * @param node
     * @return true indicates the hard constraint check was passed i.e. the course
     *         is not in the same slot
     */
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
                                // System.out.println("Problem is conflicting lab");
                                return false;
                            } else if (unit.getLectureNum() == c.getLectureNum()) {
                                // System.out.println("Problem is conflicting lab");
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
