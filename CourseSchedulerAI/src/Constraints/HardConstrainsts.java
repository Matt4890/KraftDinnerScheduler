package Constraints;

import schedule.*;

import java.util.*;

import coursesULabs.*;
import enums.*;

/*
Labcount is never updated.
Implement the special courses ie 913 813 313 and 413 probably best if we just add them to the not compatible list
*/
public class HardConstrainsts {

    public static boolean checkAssignmentHardConstriantsCourse(Course course, CourseSlot slot) {
        return checkCourseMax(course, slot)  && checkEveningClasses(course, slot)
                && checkConflict500Levels(course, slot)
                //&& checkConflictsWithSameName(course, slot)
                && checkLabNotWithCourseAddingCourse(course, slot)
                //&& checkDuplicateCourse(course, slot)
                && checkUnwanted(course, slot)
                && checkNotCompatible(course, slot);
    }

    public static boolean checkCourseMax(Course c, CourseSlot s) {
        return (s.getCourseCount() + 1 <= s.getCourseMax());
    }

    /**
     *Not sure what it does so I won't touch it
     *Make sure 2 lectures of the same course aren't in the same slot?
     *Should definitly be in soft constraints
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
            if(s.getTime() >= 1800){
                
            }
            return s.getTime() >= 1800;
        }
        return true;
    }

    public static boolean checkConflict500Levels(final Course c, final CourseSlot s) {
        if (c.getCourseNum() >= 500) {
            final ArrayList<Course> lookup = s.getAssignedCourses();
            for (int i = 0; i < lookup.size(); i++) {
                if (lookup.get(i).getCourseNum() >= 500) {
                    System.out.println("Problem is 500 together ");
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Shouldn't this be in soft constraints
     * @param c
     * @param s
     * @return
     */
    public static boolean checkConflictsWithSameName(Unit c, Slot s) {
        for(Slot slot : s.getOverlaps()){
            for(Unit unit : slot.getClassAssignment()){
                if(unit.getCourseNum() == c.getCourseNum() && unit.getCourseType().equals(c.getCourseType())){
                    return false;
                }
            }
        }
        return true;

    }

    
    public static boolean checkLabNotWithCourseAddingCourse(Course c, Slot s){
        for(Slot slot : s.getOverlaps()){
            for(Unit unit : slot.getClassAssignment()){
                if(unit.getCourseNum() == c.getCourseNum()){
                    if(unit.getCourseType().equals(c.getCourseType())){
                        if(unit.getLectureNum() == 0){
                            System.out.println("Problem is conflicting lab");
                            return false;
                        }
                        else if(unit.getLectureNum() == c.getLectureNum()){
                            System.out.println("Problem is conflicting lab");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static boolean checkAssignmentHardConstriantsLab(final Lab lab, final LabSlot slot,
            final HashMap<Integer, Slot> MWFLec, final HashMap<Integer, Slot> TuThLec,
            final HashMap<Integer, Slot> MWLab, final HashMap<Integer, Slot> TuThLab,
            final HashMap<Integer, Slot> FLab) {
        return checkLabMax(lab, slot) //&& checkDuplicateLab(lab, slot)
                //&& checkConflictsWithSameName(lab, slot)
                && checkUnwanted(lab, slot)
                && checkNotCompatible(lab, slot)
                && checkLabNotWithCourseAddingLab(lab, slot);
    }

    public static boolean checkLabMax(final Lab c, final LabSlot s) {
        return (s.getLabCount() + 1 <= s.getLabMax());
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
     * given a course and a slot to add it to, will return true if unwanted is not broken
     * @param course
     * @param slotToAdd
     * @return
     */
    public static boolean checkUnwanted(Unit course, Slot slotToAdd){
        ArrayList<Slot> unwanted = course.getUnwanted();
        for(Slot s : unwanted){
            if(slotToAdd.isSameSlot(s)){
                System.out.println("Problem is unwanted");
                return false;
            }
        }
        if(course instanceof Course){
            if(((CourseSlot)slotToAdd).getDay().equals(CourseDays.TUETHR)){
                if(slotToAdd.getTime() == 1100){
                    System.out.println("Problem is 1100");
                    return false;
                }
            }
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
                /*
                for(Unit unitToCheck : overlaps.getClassAssignment()){
                    if(unitToCheck.getCourseNum() == unit.getCourseNum()){
                        if(unitToCheck.getCourseType().equals(unit.getCourseType())){
                            if(unitToCheck.getLectureNum() == unit.getLectureNum()){
                                if(unit instanceof Course && unitToCheck instanceof Course){
                                    return false;
                                }
                                else if(unit instanceof Lab && unitToCheck instanceof Lab){
                                    if(((Lab)unit).getTutNum() == ((Lab)unitToCheck).getTutNum()){
                                        return false;
                                    }
                                }
                            }
                        }
                    }
                }
                */


                //once we stop deepcopying the units this should work
                if(overlaps.getClassAssignment().contains(unit)){
                    System.out.println("Problem is Notcompatible");
                    return false;
                }
                
            }
        }
        return true;
    }

    public static boolean checkLabNotWithCourseAddingLab(Lab c, Slot s){
        for(Slot slot : s.getOverlaps()){
            for(Unit unit : slot.getClassAssignment()){
                if(unit.getCourseNum() == c.getCourseNum()){
                    if(unit.getCourseType().equals(c.getCourseType())){
                        if(c.getLectureNum() == 0){
                            System.out.println("Problem is conflicting lab");
                            return false;
                        }
                        else if(unit.getLectureNum() == c.getLectureNum()){
                            System.out.println("Problem is conflicting lab");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
