package Constraints;

import schedule.*;
import coursesULabs.*;

public class HardConstrainsts {
    
    
    public static boolean checkAssignmentHardConstriantsCourse(Course course, CourseSlot slot){
        return false; 
    }
    public static boolean checkCourseMax(Course c, CourseSlot s){
       return (s.getCourseCount() +1< s.getCourseMax());
        
    }
    public static boolean checkDuplicate(Course c, CourseSlot s){
        return true;


    }


    public static boolean checkAssignmentHardConstriantsLab(Lab lab, LabSlot slot){
        return false; 
    }
    public static boolean checkLabMax(Lab c, LabSlot s){
        return (s.getLabCount() +1< s.getLabMax());
    }


}