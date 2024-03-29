import parser.*;
import schedule.*;
import tree.Generator;
import coursesULabs.*;
import java.util.*;
import java.io.*;

public class Main {
  public static void main(String[] args) throws FileNotFoundException{
    //Instantiating the File class
    
    File file = new File("output.txt");
    //Instantiating the PrintStream class
    PrintStream stream = new PrintStream(file);
    System.out.println("From now on "+file.getAbsolutePath()+" will be your console");
    System.setOut(stream);
    
    //Printing values to file

    String filename;
    int courseMinPen;
    int pairsPen;
    int brothersPen;

    filename = args[0];
    courseMinPen = Integer.parseInt(args[1]);
    pairsPen = Integer.parseInt(args[2]);
    brothersPen = Integer.parseInt(args[3]); 

    Parser parser = new Parser(filename);

    int initialMinPenalty = courseMinPen * parser.getMinPenCount();
    int initialPairsPenalty = pairsPen * parser.getPairPenCount();
    int initialPreferencePenalty = parser.getPrefPen();

    

    Schedule initialSchedule = parser.getSchedule();
    System.out.println("The Schedule:");
    System.out.println(initialSchedule.toString());
    HashMap<String, Course> allCourses = parser.getCourseMap();
    HashMap<String, Lab> allLabs = parser.getLabMap();
    addConstraintsForSpecialClasses(allLabs, initialSchedule);
    int initialPenalty = parser.getInitialPenalty();

    initialPenalty = initialPenalty + initialMinPenalty + initialPairsPenalty + initialPreferencePenalty;

    ArrayList<Unit> unitsToProcess = orderedUnitsForAdding(allCourses, allLabs);
    System.out.println("Units Made");

    Generator search = new Generator(initialSchedule, initialPenalty, courseMinPen, pairsPen, brothersPen);
    search.generateFBoundBIG(unitsToProcess);
    System.out.println("Generator Obj Created");

    // Generator gen = new Generator();
    // gen.createFBound(parser.getMap());

  }

  private static void addConstraintsForSpecialClasses(HashMap<String, Lab> allCourses, Schedule schec) {
    boolean toremove813 = false;
    String id813 = "";
    boolean toremove913 = false;
    String id913 = "";
    for(Map.Entry<String, Lab> entry : allCourses.entrySet()){
      if(entry.getValue().getCourseNum() == 813 && entry.getValue().getCourseType().equals("CPSC")){
        Lab cpsc813 = entry.getValue();
        if (schec.getTuThLab().containsKey(1800)){
          schec.getTuThLab().get(1800).getClassAssignment().add(cpsc813);
          toremove813 = true;
          id813 = cpsc813.toString();
        }
        else{
          System.out.println("Slot Tuesday at 1800 doesn't exist but 813/913 should be there");
          System.exit(0);
        }
        for(Unit unit : cpsc813.getNotCompatible()){
          if(unit.getCourseNum()== 313 && unit.getCourseType().equals("CPSC")){
            Unit cpsc313 = unit; //purely for readability can be a lab or course
            cpsc813.addToNotCompatible(cpsc313);
            cpsc313.addToNotCompatible(cpsc813);
            cpsc313.incrementNonCompatible();
            cpsc813.incrementNonCompatible();
            for(Unit notCompatibleToadd : cpsc313.getNotCompatible()){
              cpsc813.addToNotCompatible(notCompatibleToadd);
              notCompatibleToadd.addToNotCompatible(cpsc813);
              notCompatibleToadd.incrementNonCompatible();
              cpsc813.incrementNonCompatible();
            }
          }
        }
      }
      else if(entry.getValue().getCourseNum() == 913 && entry.getValue().getCourseType().equals("CPSC")){
        Lab cpsc913 = entry.getValue();
        if (schec.getTuThLab().containsKey(1800)){
          schec.getTuThLab().get(1800).getClassAssignment().add(cpsc913);
          toremove913 = true;
          id913 = cpsc913.toString();
        }
        else{
          System.out.println("Slot Tuesday at 1800 doesn't exist but 813/913 should be there");
          System.exit(0);
        }
        for(Unit unit : cpsc913.getNotCompatible()){
          if(unit.getCourseNum()== 413 && unit.getCourseType().equals("CPSC")){
            Unit cpsc413 = unit; //purely for readability can be a lab or course
            cpsc913.addToNotCompatible(cpsc413);
            cpsc413.addToNotCompatible(cpsc913);
            cpsc413.incrementNonCompatible();
            cpsc913.incrementNonCompatible();
            for(Unit notCompatibleToadd : cpsc413.getNotCompatible()){
              cpsc913.addToNotCompatible(notCompatibleToadd);
              notCompatibleToadd.addToNotCompatible(cpsc913);
              notCompatibleToadd.incrementNonCompatible();
              cpsc913.incrementNonCompatible();
            }
          }
        }
      }
    }
    if (toremove813){
      allCourses.remove(id813);
    }
    if (toremove913){
      allCourses.remove(id913);
    }

  }

  /*
   * Function to order units as we want to process them
   * 
   * @param courses: hashmap of courses indexed by string
   * 
   * @param lab: hashmap of labs indexed by string
   * 
   * @return: ArrayList of units in order to process
   */
  private static ArrayList<Unit> orderedUnitsForAdding(HashMap<String, Course> courses, HashMap<String, Lab> labs) {
    ArrayList<Unit> toReturn = new ArrayList<Unit>();
    for (Map.Entry<String, Course> entry : courses.entrySet()) {
      Course c = (Course) entry.getValue();
      //This is where we do the fancy stuff
      toReturn.add(c);
    }
    for (Map.Entry<String, Lab> entry : labs.entrySet()) {
      Lab l = (Lab) entry.getValue();
      //This is where we do the fancy stuff
      toReturn.add(l);
    }
    System.out.println(toReturn);
    bubbleSort(toReturn);
    
    System.out.println(toReturn);

    return toReturn;
  }



  /**
   * takes a arraylist of units and orders them from most constrained to least
   * @param units
   */
  public static void bubbleSort(ArrayList<Unit> units) 
  { 
      int n = units.size();
      for (int i = 0; i < n-1; i++){ 
          for (int j = 0; j < n-i-1; j++){
              if (units.get(j).getConstrained() < units.get(j+1).getConstrained()) 
              { 
                  Unit temp = units.get(j); 
                  units.set(j,units.get(j+1)); 
                  units.set(j+1, temp); 
              } 
          }
      }
  }
  
  


}
