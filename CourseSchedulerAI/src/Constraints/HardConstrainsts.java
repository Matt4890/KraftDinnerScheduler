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
            final LabSlot s1 = (LabSlot) MWLab.get(s.getTime());
            final ArrayList<Lab> correspondingLabsInSlot = s1.getAssignedLabs();
            for (int i = 0; i < correspondingLabsInSlot.size(); i++) {
                if (correspondingLabsInSlot.get(i).getCourseNum() == c.getCourseNum() && correspondingLabsInSlot.get(i).getCourseType().equals(c.getCourseType())) {
                    return false;
                }
            }
            // Check at match point or -1
            int time = s.getTime();
            if ((time / 200) % 2 != 0) {
                final LabSlot s2 = (LabSlot) FLab.get(s.getTime());
                final ArrayList<Lab> correspondingLabsInSlot1 = s2.getAssignedLabs();
                for (int i = 0; i < correspondingLabsInSlot1.size(); i++) {
                    if (correspondingLabsInSlot1.get(i).getCourseNum() == c.getCourseNum() && correspondingLabsInSlot1.get(i).getCourseType().equals(c.getCourseType())) {
                        return false;
                    }
                }
            } else {

                final LabSlot s3 = (LabSlot) FLab.get(s.getTime() - 100);
                final ArrayList<Lab> correspondingLabsInSlot2 = s3.getAssignedLabs();
                for (int i = 0; i < correspondingLabsInSlot2.size(); i++) {
                    if (correspondingLabsInSlot2.get(i).getCourseNum() == c.getCourseNum() && correspondingLabsInSlot2.get(i).getCourseType().equals(c.getCourseType())) {
                        return false;
                    }
                }
            }
        } else if (s.getDay().toString().equals(CourseDays.TUETHR.toString())) {
            // Check lab at same time and time +1
            final LabSlot s1 = (LabSlot) TuThLab.get(s.getTime() - 30); // Same time
            final ArrayList<Lab> correspondingLabsInSlot = s1.getAssignedLabs();
            for (int i = 0; i < correspondingLabsInSlot.size(); i++) {
                if (correspondingLabsInSlot.get(i).getCourseNum() == c.getCourseNum() && correspondingLabsInSlot.get(i).getCourseType().equals(c.getCourseType())) {
                    return false;
                }
            }
            final LabSlot s2 = (LabSlot) MWLab.get(s.getTime() + 70); // Time +1
            final ArrayList<Lab> correspondingLabsInSlot1 = s2.getAssignedLabs();
            for (int i = 0; i < correspondingLabsInSlot1.size(); i++) {
                if (correspondingLabsInSlot1.get(i).getCourseNum() == c.getCourseNum() && correspondingLabsInSlot1.get(i).getCourseType().equals(c.getCourseType())) {
                    return false;
                }
            }

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
            final CourseSlot s1 = (CourseSlot) MWFLec.get(s.getTime());
            final ArrayList<Course> correspondingCoursesInSlot = s1.getAssignedCourses();
            for (int i = 0; i < correspondingCoursesInSlot.size(); i++) {
                if (correspondingCoursesInSlot.get(i).getCourseNum() == c.getCourseNum() && correspondingCoursesInSlot.get(i).getCourseType().equals(c.getCourseType())) {
                    return false;
                }
            }

        } else if (s.getDay().toString().equals(LabDays.TUETHR.toString())) {

            final int time = s.getTime();
            if (time != 2000) {
                if (time != 800) {

                    //checking half an hour before
                    if (time == 1000 || time == 1300 || time == 1600 || time == 1900) {
                        final CourseSlot s1 = (CourseSlot) TuThLec.get(s.getTime() - 70);
                        final ArrayList<Course> correspondingCoursesInSlot = s1.getAssignedCourses();
                        for (int i = 0; i < correspondingCoursesInSlot.size(); i++) {
                            if (correspondingCoursesInSlot.get(i).getCourseNum() == c.getCourseNum() && correspondingCoursesInSlot.get(i).getCourseType().equals(c.getCourseType())) {
                                return false;
                            }
                        }

                        //checking same time 
                    } else if (time == 1100 || time == 1400 || time == 1700) {
                        final CourseSlot s1 = (CourseSlot) TuThLec.get(s.getTime());
                        final ArrayList<Course> correspondingCoursesInSlot = s1.getAssignedCourses();
                        for (int i = 0; i < correspondingCoursesInSlot.size(); i++) {
                            if (correspondingCoursesInSlot.get(i).getCourseNum() == c.getCourseNum() && correspondingCoursesInSlot.get(i).getCourseType().equals(c.getCourseType())) {
                                return false;
                            }
                        }
                        //check hour before and half an hour after 
                    } else if (time == 900 || time == 1200 || time == 1500 || time == 1800) {
                        // Check -100 and +30
                        final CourseSlot s1 = (CourseSlot) TuThLec.get(s.getTime() + 30);
                        final ArrayList<Course> correspondingCoursesInSlot = s1.getAssignedCourses();
                        for (int i = 0; i < correspondingCoursesInSlot.size(); i++) {
                            if (correspondingCoursesInSlot.get(i).getCourseNum() == c.getCourseNum() && correspondingCoursesInSlot.get(i).getCourseType().equals(c.getCourseType())) {
                                return false;
                            }
                        }
                        final CourseSlot s2 = (CourseSlot) TuThLec.get(s.getTime() - 100);
                        final ArrayList<Course> correspondingCoursesInSlot1 = s2.getAssignedCourses();
                        for (int i = 0; i < correspondingCoursesInSlot1.size(); i++) {
                            if (correspondingCoursesInSlot1.get(i).getCourseNum() == c.getCourseNum() && correspondingCoursesInSlot1.get(i).getCourseType().equals(c.getCourseType())) {
                                return false;
                            }
                        }

                    }

                }
            }
        } else { // FRIDAY
            final int time = s.getTime();
            //check time and hour ahead of it
            if (time != 2000) {
                final CourseSlot s1 = (CourseSlot) TuThLec.get(s.getTime() + 100);
                final ArrayList<Course> correspondingCoursesInSlot = s1.getAssignedCourses();
                for (int i = 0; i < correspondingCoursesInSlot.size(); i++) {
                    if (correspondingCoursesInSlot.get(i).getCourseNum() == c.getCourseNum() && correspondingCoursesInSlot.get(i).getCourseType().equals(c.getCourseType())) {
                        return false;
                    }
                }
                final CourseSlot s2 = (CourseSlot) TuThLec.get(s.getTime());
                final ArrayList<Course> correspondingCoursesInSlot1 = s2.getAssignedCourses();
                for (int i = 0; i < correspondingCoursesInSlot1.size(); i++) {
                    if (correspondingCoursesInSlot1.get(i).getCourseNum() == c.getCourseNum()&& correspondingCoursesInSlot1.get(i).getCourseType().equals(c.getCourseType())) {
                        return false;
                    }
                }
            }

        }




        return toReturn;
    }


        //TODO UNWANTED
        //Not Compatible 


    public static boolean checkUnwanted(){
        return true;
    }

}