package Constraints;

import schedule.*;

import java.util.ArrayList;

import coursesULabs.*;

public class HardConstrainsts {
    
    
    public static boolean checkAssignmentHardConstriantsCourse(Course course, CourseSlot slot){
        return checkCourseMax(course, slot) && 
        checkDuplicateCourse(course, slot) && 
        checkEveningClasses(course, slot) && 
        checkConflict500Levels(course, slot) && 
        checkLabConflicts(course, slot);
    }
    public static boolean checkCourseMax(Course c, CourseSlot s){
       return (s.getCourseCount() +1 < s.getCourseMax());
        
    }
    public static boolean checkDuplicateCourse(Course c, CourseSlot s){
        //Go through courses in slot to see if theres a duplicate
        //Right now its a runthrough but ideally once its implemented correctly it should be a lookup??????????????
        boolean result = true;
        ArrayList<Course> lookup = s.getAssignedCourses();
        for (int i = 0; i< lookup.size(); i++){
            if (lookup.get(i).getID() ==  c.getID()){
                result = false;
            }   
        }
        return result;

    }
    public static boolean checkEveningClasses(Course c, CourseSlot s){
        boolean toReturn = true;
        if (Integer.toString(c.getLectureNum()).substring(0,1).equals("9")){
            //Check evening class 
            toReturn =  s.getTime() >= 1800;
        } 
        return toReturn;

        
    }
    public static boolean checkConflict500Levels(Course c, CourseSlot s){
        boolean result = true;
        if (c.getCourseNum() >=500){
            ArrayList<Course> lookup = s.getAssignedCourses();
            for (int i = 0; i< lookup.size(); i++){
                if (lookup.get(i).getCourseNum() >= 500){
                    result = false;
                }   
            }
        }
        return result;
    }
    public static boolean checkLabConflicts(Course c, CourseSlot s){
        //Go through the labslots with the same time
        return true;
    }


    public static boolean checkAssignmentHardConstriantsLab(Lab lab, LabSlot slot){
        return false; 
    }
    public static boolean checkLabMax(Lab c, LabSlot s){
        return (s.getLabCount() +1 < s.getLabMax());
    }
    public static boolean checkDuplicateLab(Lab c, LabSlot s){
        //Go through courses in slot to see if theres a duplicate
        //Right now its a runthrough but ideally once its implemented correctly it should be a lookup??????????????
        boolean result = true;
        ArrayList<Lab> lookup = s.getAssignedLabs();
        for (int i = 0; i< lookup.size(); i++){
            if (lookup.get(i).getID() ==  c.getID()){
                result = false;
            }   
        }
        return result;

    }
    public static boolean checkConflictingCourses(Lab c, LabSlot s){
        //TODO: once hash maps are implemented 
        return true;
    }


}