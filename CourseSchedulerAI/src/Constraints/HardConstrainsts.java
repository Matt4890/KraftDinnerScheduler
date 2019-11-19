package Constraints;

import schedule.*;

import java.util.*;

import coursesULabs.*;
import enums.*;

public class HardConstrainsts {

    public static boolean checkAssignmentHardConstriantsCourse(Course course, CourseSlot slot,
            HashMap<Integer, Slot> MWFLec, HashMap<Integer, Slot> TuThLec, HashMap<Integer, Slot> MWLab,
            HashMap<Integer, Slot> TuThLab, HashMap<Integer, Slot> FLab) {
        return checkCourseMax(course, slot) && checkDuplicateCourse(course, slot) && checkEveningClasses(course, slot)
                && checkConflict500Levels(course, slot)
                && checkLabConflicts(course, slot, MWFLec, TuThLec, MWLab, TuThLab, FLab);
    }

    public static boolean checkCourseMax(Course c, CourseSlot s) {
        return (s.getCourseCount() + 1 < s.getCourseMax());

    }

    public static boolean checkDuplicateCourse(Course c, CourseSlot s) {
        // Go through courses in slot to see if theres a duplicate
        // Right now its a runthrough but ideally once its implemented correctly it
        // should be a lookup??????????????
        boolean result = true;
        ArrayList<Course> lookup = s.getAssignedCourses();
        for (int i = 0; i < lookup.size(); i++) {
            if (lookup.get(i).getID() == c.getID()) {
                result = false;
            }
        }
        return result;

    }

    public static boolean checkEveningClasses(Course c, CourseSlot s) {
        boolean toReturn = true;
        if (Integer.toString(c.getLectureNum()).substring(0, 1).equals("9")) {
            // Check evening class
            toReturn = s.getTime() >= 1800;
        }
        return toReturn;

    }

    public static boolean checkConflict500Levels(Course c, CourseSlot s) {
        boolean result = true;
        if (c.getCourseNum() >= 500) {
            ArrayList<Course> lookup = s.getAssignedCourses();
            for (int i = 0; i < lookup.size(); i++) {
                if (lookup.get(i).getCourseNum() >= 500) {
                    result = false;
                }
            }
        }
        return result;
    }

    public static boolean checkLabConflicts(Course c, CourseSlot s, HashMap<Integer, Slot> MWFLec,
            HashMap<Integer, Slot> TuThLec, HashMap<Integer, Slot> MWLab, HashMap<Integer, Slot> TuThLab,
            HashMap<Integer, Slot> FLab) {
        boolean toReturn = true;
        // Go through the labslots with the same time
        if (s.getDay().toString().equals(CourseDays.MONWEDFRI.toString())) {
            // Check the Lab slots at the same time on Monday Wednesday
            LabSlot s1 = (LabSlot) MWLab.get(s.getTime());
            ArrayList<Lab> correspondingLabsInSlot = s1.getAssignedLabs();
            for (int i = 0; i < correspondingLabsInSlot.size(); i++) {
                if (correspondingLabsInSlot.get(i).getCourseNum() == c.getCourseNum()) {
                    toReturn = false;
                }
            }
            // Check at match point or -1
            LabSlot s2 = (LabSlot) FLab.get(s.getTime());
            ArrayList<Lab> correspondingLabsInSlot1 = s2.getAssignedLabs();
            for (int i = 0; i < correspondingLabsInSlot1.size(); i++) {
                if (correspondingLabsInSlot1.get(i).getCourseNum() == c.getCourseNum()) {
                    toReturn = false;
                }
            }

            LabSlot s3 = (LabSlot) FLab.get(s.getTime() - 100);
            ArrayList<Lab> correspondingLabsInSlot2 = s3.getAssignedLabs();
            for (int i = 0; i < correspondingLabsInSlot2.size(); i++) {
                if (correspondingLabsInSlot2.get(i).getCourseNum() == c.getCourseNum()) {
                    toReturn = false;
                }
            }
        } else if (s.getDay().toString().equals(CourseDays.TUETHR.toString())) {
            // Check lab at same time and time +1
            LabSlot s1 = (LabSlot) TuThLab.get(s.getTime() - 30); // Same time
            ArrayList<Lab> correspondingLabsInSlot = s1.getAssignedLabs();
            for (int i = 0; i < correspondingLabsInSlot.size(); i++) {
                if (correspondingLabsInSlot.get(i).getCourseNum() == c.getCourseNum()) {
                    toReturn = false;
                }
            }
            LabSlot s2 = (LabSlot) MWLab.get(s.getTime() + 70); // Time +1
            ArrayList<Lab> correspondingLabsInSlot1 = s2.getAssignedLabs();
            for (int i = 0; i < correspondingLabsInSlot1.size(); i++) {
                if (correspondingLabsInSlot1.get(i).getCourseNum() == c.getCourseNum()) {
                    toReturn = false;
                }
            }

        }
        return toReturn;
    }

    public static boolean checkAssignmentHardConstriantsLab(Lab lab, LabSlot slot, HashMap<Integer, Slot> MWFLec,
    HashMap<Integer, Slot> TuThLec, HashMap<Integer, Slot> MWLab, HashMap<Integer, Slot> TuThLab,
    HashMap<Integer, Slot> FLab) {
        return checkLabMax(lab, slot) && checkDuplicateLab(lab, slot) && checkCourseConflicts(lab, slot, MWFLec, TuThLec, MWLab, TuThLab, FLab);
    }

    public static boolean checkLabMax(Lab c, LabSlot s) {
        return (s.getLabCount() + 1 < s.getLabMax());
    }

    public static boolean checkDuplicateLab(Lab c, LabSlot s) {
        // Go through courses in slot to see if theres a duplicate
        // Right now its a runthrough but ideally once its implemented correctly it
        // should be a lookup??????????????
        boolean result = true;
        ArrayList<Lab> lookup = s.getAssignedLabs();
        for (int i = 0; i < lookup.size(); i++) {
            if (lookup.get(i).getID() == c.getID()) {
                result = false;
            }
        }
        return result;

    }

    public static boolean checkCourseConflicts(Lab c, LabSlot s, HashMap<Integer, Slot> MWFLec,
            HashMap<Integer, Slot> TuThLec, HashMap<Integer, Slot> MWLab, HashMap<Integer, Slot> TuThLab,
            HashMap<Integer, Slot> FLab) {
        boolean toReturn = true;
        // Go through the labslots with the same time
        if (s.getDay().toString().equals(LabDays.MONWED.toString())) {
            // Check the Course Slots at the same time on Monday Wednesday
            CourseSlot s1 = (CourseSlot) MWFLec.get(s.getTime());
            ArrayList<Course> correspondingCoursesInSlot = s1.getAssignedCourses();
            for (int i = 0; i < correspondingCoursesInSlot.size(); i++) {
                if (correspondingCoursesInSlot.get(i).getCourseNum() == c.getCourseNum()) {
                    toReturn = false;
                }
            }

        } else if (s.getDay().toString().equals(LabDays.TUETHR.toString())) {

            int time = s.getTime();
            if (time != 2000) {
                if (time != 800) {

                    if (time == 1000 || time == 1300 || time == 1600 || time == 1900) {
                        CourseSlot s1 = (CourseSlot) TuThLec.get(s.getTime() + 70);
                        ArrayList<Course> correspondingCoursesInSlot = s1.getAssignedCourses();
                        for (int i = 0; i < correspondingCoursesInSlot.size(); i++) {
                            if (correspondingCoursesInSlot.get(i).getCourseNum() == c.getCourseNum()) {
                                toReturn = false;
                            }
                        }

                    } else if (time == 1100 || time == 1400 || time == 1700) {
                        CourseSlot s1 = (CourseSlot) TuThLec.get(s.getTime());
                        ArrayList<Course> correspondingCoursesInSlot = s1.getAssignedCourses();
                        for (int i = 0; i < correspondingCoursesInSlot.size(); i++) {
                            if (correspondingCoursesInSlot.get(i).getCourseNum() == c.getCourseNum()) {
                                toReturn = false;
                            }
                        }
                    } else if (time == 900) {
                        // Check 9:30
                        CourseSlot s1 = (CourseSlot) TuThLec.get(s.getTime() + 30);
                        ArrayList<Course> correspondingCoursesInSlot = s1.getAssignedCourses();
                        for (int i = 0; i < correspondingCoursesInSlot.size(); i++) {
                            if (correspondingCoursesInSlot.get(i).getCourseNum() == c.getCourseNum()) {
                                toReturn = false;
                            }
                        }
                    } else if (time == 1200 || time == 1500 || time == 1800) {
                        // Check -100 and +30
                        CourseSlot s1 = (CourseSlot) TuThLec.get(s.getTime() + 30);
                        ArrayList<Course> correspondingCoursesInSlot = s1.getAssignedCourses();
                        for (int i = 0; i < correspondingCoursesInSlot.size(); i++) {
                            if (correspondingCoursesInSlot.get(i).getCourseNum() == c.getCourseNum()) {
                                toReturn = false;
                            }
                        }
                        CourseSlot s2 = (CourseSlot) TuThLec.get(s.getTime() - 100);
                        ArrayList<Course> correspondingCoursesInSlot1 = s2.getAssignedCourses();
                        for (int i = 0; i < correspondingCoursesInSlot1.size(); i++) {
                            if (correspondingCoursesInSlot1.get(i).getCourseNum() == c.getCourseNum()) {
                                toReturn = false;
                            }
                        }

                    }

                }
            }
        } else { // FRIDAY
            int time = s.getTime();
            if (time != 2000) {
                CourseSlot s1 = (CourseSlot) TuThLec.get(s.getTime() + 100);
                ArrayList<Course> correspondingCoursesInSlot = s1.getAssignedCourses();
                for (int i = 0; i < correspondingCoursesInSlot.size(); i++) {
                    if (correspondingCoursesInSlot.get(i).getCourseNum() == c.getCourseNum()) {
                        toReturn = false;
                    }
                }
                CourseSlot s2 = (CourseSlot) TuThLec.get(s.getTime());
                ArrayList<Course> correspondingCoursesInSlot1 = s2.getAssignedCourses();
                for (int i = 0; i < correspondingCoursesInSlot1.size(); i++) {
                    if (correspondingCoursesInSlot1.get(i).getCourseNum() == c.getCourseNum()) {
                        toReturn = false;
                    }
                }
            }

        }

        return toReturn;
    }

}