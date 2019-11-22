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
        boolean toReturn = true;
        // Go through the labslots with the same time
        if (s.getDay().toString().equals(CourseDays.MONWEDFRI.toString())) {
            // Check the Lab slots at the same time on Monday Wednesday
            if(doesOverlapAddingCourse(MWLab, c, 0, s)){
                return false;
            }
            // Check at match point or -1
            int time = s.getTime();
            if ((time / 200) % 2 != 0) {
                if(doesOverlapAddingCourse(FLab, c, 0, s)){
                    return false;
                }
            } else {
                if(doesOverlapAddingCourse(FLab, c, -100, s)){
                    return false;
                }
            }
        } else if (s.getDay().toString().equals(CourseDays.TUETHR.toString())) {
            // Check lab at same time and time +1
            if(doesOverlapAddingCourse(TuThLab, c, -30, s)){
                return false;
            }
            return !doesOverlapAddingCourse(TuThLab, c, 70, s);
        }
        return toReturn;
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
        boolean toReturn = true;
        // Go through the labslots with the same time
        if (s.getDay().toString().equals(LabDays.MONWED.toString())) {
            // Check the Course Slots at the same time on Monday Wednesday
            return !doesOverlapAddingLab(MWFLec, c, 0, s);

        } else if (s.getDay().toString().equals(LabDays.TUETHR.toString())) {

            final int time = s.getTime();
            if (time != 2000) {
                if (time != 800) {

                    //checking half an hour before
                    if (time == 1000 || time == 1300 || time == 1600 || time == 1900) {
                        if(doesOverlapAddingLab(TuThLec, c, -70, s)){
                            return false;
                        }
                    //checking same time
                    } else if (time == 1100 || time == 1400 || time == 1700) {
                        return !doesOverlapAddingLab(TuThLec, c, 0, s);

                            //check hour before and half an hour after
                    } else if (time == 900 || time == 1200 || time == 1500 || time == 1800) {
                        // Check -100 and +30
                        if(doesOverlapAddingLab(TuThLec, c, 30, s)){
                            return false;
                        }
                        return !doesOverlapAddingLab(TuThLec, c, -100, s);
                    }

                }
            }
        } else { // FRIDAY
            final int time = s.getTime();
            //check time and hour ahead of it
            if (time != 2000) {
                if(!doesOverlapAddingLab(TuThLec, c, 100, s)){
                    return false;
                }
                return doesOverlapAddingLab(TuThLec, c, 0, s);
            }

        }




        return toReturn;
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
        ArrayList<Unit> unwanted = course.getNotCompatible();
        for(Unit unit : unwanted){
            if (doesOverlap(course, slotToAddTo, unit)){
                return false;
            }
        }
        return true;
    }


    /**
     * given 2 units and a slot to add the first unit in, will return true if they overlap
     * works with all combinations of lab and courses
     * @param course1
     * @param slotToCheck
     * @param course2
     * @return
     */
    private static boolean doesOverlap(Unit course1, Slot slotToCheck, Unit course2){
        Slot slot2 = course2.getSlot();
        int timeOfSlotChecked = slotToCheck.getTime();
        int timeOfSlot2 = slot2.getTime();
        if(timeOfSlotChecked >= course2.getSlot().getTime() + 200 || timeOfSlotChecked <= course2.getSlot().getTime() - 200 ){
            return false;
        }
        //unit being added is a course
        if(course1 instanceof Course){
            // Course with a course
            if (course2 instanceof Course){
                //if the same day and same time then they overlap
                if( slot2 == slotToCheck){
                    return true;
                }
            }
            //course with a lab
            else{
                //course is MWF
                if(((CourseSlot) slotToCheck).getDay().toString().equals("MO")){
                    //lab is MW
                    if(((LabSlot) slot2).getDay().toString().equals("MO") ){
                        if(timeOfSlotChecked == timeOfSlot2){
                            return true;
                        }
                    }
                    //lab is Tuesday
                    if(((LabSlot) slot2).getDay().toString().equals("TU")){
                        return false;
                    }
                    //lab is friday
                    else{
                        if(timeOfSlotChecked == timeOfSlot2 || timeOfSlotChecked == timeOfSlot2 + 100){
                            return true;
                        }
                    }
                }
                //course is Tuesday
                else{
                    if(((LabSlot) slot2).getDay().toString().equals("MO")){
                        return false;
                    }
                    if(((LabSlot) slot2).getDay().toString().equals("TU")){
                        //course is on the hour
                        if(timeOfSlotChecked % 100 == 0){
                            if(timeOfSlotChecked == timeOfSlot2+100 || timeOfSlotChecked == timeOfSlot2){
                                return true;
                            }
                        }
                        //course is on the half an hour
                        else{
                            if(timeOfSlotChecked == timeOfSlot2+30 || timeOfSlotChecked == timeOfSlot2 - 70){
                                return true;
                            }
                        }
                    }
                }
            }
        }
        //unit being added is a lab
        else{
            //lab and course 
            if(course2 instanceof Course){
                //monday lab
                if(((LabSlot)slotToCheck).getDay().toString().equals("MO")){
                    if(timeOfSlotChecked == timeOfSlot2 && ((CourseSlot)slot2).getDay().toString().equals("MO")){
                        return true;
                    }
                }
                //tuesday labs
                else if(((LabSlot)slotToCheck).getDay().toString().equals("TU")){
                    //labs in half hour slots
                    if((timeOfSlotChecked-30) % 100 == 0 && ((CourseSlot)slot2).getDay().toString().equals("TU")) {
                        if(timeOfSlotChecked == timeOfSlot2 -30 || timeOfSlotChecked == timeOfSlot2 + 70){
                            return true;
                        }
                    }
                    //labs in hour slots
                    else{
                        if((timeOfSlotChecked == timeOfSlot2 || timeOfSlotChecked == timeOfSlot2 + 100) && ((CourseSlot)slot2).getDay().toString().equals("TU")){
                            return true;
                        }
                    }
                } 
            }
            // lab and a lab
            else{
                if(slot2 == slotToCheck){
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean doesOverlapAddingLab(HashMap<Integer,Slot> courseSlots, Lab labToAdd , int timeToAdd, Slot slotToCheck ){
        CourseSlot s1 = (CourseSlot) courseSlots.get(slotToCheck.getTime() + timeToAdd);
        ArrayList<Course> correspondingCoursesInSlot = s1.getAssignedCourses();
        for (int i = 0; i < correspondingCoursesInSlot.size(); i++) {
            if (correspondingCoursesInSlot.get(i).getCourseNum() == labToAdd.getCourseNum() && correspondingCoursesInSlot.get(i).getCourseType().equals(labToAdd.getCourseType())){
                return true;
            }
        }
        return false;
    }

    private static boolean doesOverlapAddingCourse(HashMap<Integer,Slot> courseSlots, Course courseToAdd , int timeToAdd, Slot slotToCheck){
        LabSlot s1 = (LabSlot) courseSlots.get(slotToCheck.getTime() + timeToAdd);
        ArrayList<Lab> correspondingCoursesInSlot = s1.getAssignedLabs();
        for (int i = 0; i < correspondingCoursesInSlot.size(); i++) {
            if (correspondingCoursesInSlot.get(i).getCourseNum() == courseToAdd.getCourseNum() && correspondingCoursesInSlot.get(i).getCourseType().equals(courseToAdd.getCourseType())){
                return true;
            }
        }

        return false;
    }




}
