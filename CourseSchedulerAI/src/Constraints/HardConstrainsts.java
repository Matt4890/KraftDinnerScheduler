package Constraints;

import schedule.*;

import java.util.*;

import coursesULabs.*;
import enums.*;

public class HardConstrainsts {

    public static boolean checkAssignmentHardConstriantsCourse(final Course course, final CourseSlot slot,
            final HashMap<Integer, Slot> MWFLec, final HashMap<Integer, Slot> TuThLec,
            final HashMap<Integer, Slot> MWLab, final HashMap<Integer, Slot> TuThLab,
            final HashMap<Integer, Slot> FLab) {
        return checkCourseMax(course, slot) && checkDuplicateCourse(course, slot) && checkEveningClasses(course, slot)
                && checkConflict500Levels(course, slot)
                && checkLabConflicts(course, slot, MWFLec, TuThLec, MWLab, TuThLab, FLab);
    }

    public static boolean checkCourseMax(final Course c, final CourseSlot s) {
        return (s.getCourseCount() + 1 < s.getCourseMax());

    }

    /**
     *
     * @param c
     * @param s
     * @return true if no hard constraint broken
     */
    public static boolean checkDuplicateCourse(final Course c, final CourseSlot s) {
        // Go through courses in slot to see if theres a duplicate
        // Right now its a runthrough but ideally once its implemented correctly it
        // should be a lookup??????????????
        boolean result = true;
        final ArrayList<Course> lookup = s.getAssignedCourses();
        for (int i = 0; i < lookup.size(); i++) {
            if (lookup.get(i).getCourseNum() == c.getCourseNum()
                    && lookup.get(i).getCourseType().equals(c.getCourseType())) {
                result = false;
            }
        }

        return result;

    }

    public static boolean checkEveningClasses(final Course c, final CourseSlot s) {
        boolean toReturn = true;
        if (Integer.toString(c.getLectureNum()).substring(0, 1).equals("9")) {
            // Check evening class
            toReturn = s.getTime() >= 1800;
        }
        return toReturn;

    }

    public static boolean checkConflict500Levels(final Course c, final CourseSlot s) {
        boolean result = true;
        if (c.getCourseNum() >= 500) {
            final ArrayList<Course> lookup = s.getAssignedCourses();
            for (int i = 0; i < lookup.size(); i++) {
                if (lookup.get(i).getCourseNum() >= 500) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public static boolean checkLabConflicts(final Course c, final CourseSlot s, final HashMap<Integer, Slot> MWFLec,
            final HashMap<Integer, Slot> TuThLec, final HashMap<Integer, Slot> MWLab,
            final HashMap<Integer, Slot> TuThLab, final HashMap<Integer, Slot> FLab) {

        for(Slot slot : s.getOverlaps()){
            for(Unit unit : slot.getClassAssignment()){
                if(unit.getCourseNum() == c.getCourseNum() && unit.getCourseType().equals(c.getCourseType())){
                    return false;
                }
            }
        }
        return true;

    }

    public static boolean checkAssignmentHardConstriantsLab(final Lab lab, final LabSlot slot,
            final HashMap<Integer, Slot> MWFLec, final HashMap<Integer, Slot> TuThLec,
            final HashMap<Integer, Slot> MWLab, final HashMap<Integer, Slot> TuThLab,
            final HashMap<Integer, Slot> FLab) {
        return checkLabMax(lab, slot) && checkDuplicateLab(lab, slot)
                && checkCourseConflicts(lab, slot, MWFLec, TuThLec, MWLab, TuThLab, FLab);
    }

    public static boolean checkLabMax(final Lab c, final LabSlot s) {
        return (s.getLabCount() + 1 < s.getLabMax());
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

    public static boolean checkCourseConflicts(final Lab c, final LabSlot s, final HashMap<Integer, Slot> MWFLec,
            final HashMap<Integer, Slot> TuThLec, final HashMap<Integer, Slot> MWLab,
            final HashMap<Integer, Slot> TuThLab, final HashMap<Integer, Slot> FLab) {

        for(Slot slot : s.getOverlaps()){
            for(Unit unit : slot.getClassAssignment()){
                if(unit.getCourseNum() == c.getCourseNum() && unit.getCourseType().equals(c.getCourseType())){
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * given a course and a slot to add it to, will return true if unwanted is not broken
     * @param course
     * @param slotToAdd
     * @return
     */
    public static boolean checkUnwanted(Unit course, Slot slotToAdd){
        ArrayList<Slot> unwanted = course.getUnwanted();
        if(unwanted.contains(slotToAdd)){
            return false;
        }
        return true;
    }

    /**
     * given a course and a slot to add it to it will return true if not compatible is not broken
     * @param course 
     * @param slotToAddTo
     * @return
     */
    public static boolean checkNotCompatible(Unit course, Slot slotToAddTo){
        ArrayList<Unit> notCompatible = course.getNotCompatible();
        for(Unit unit : notCompatible){
            for(Slot overlaps : slotToAddTo.getOverlaps()){
                if(overlaps.getClassAssignment().contains(unit)){
                    return false;
                }
            }
        }
        return true;
    }
}
