import parser.*;
import schedule.*;
import tree.Generator;
import coursesULabs.*;
import java.util.*;
import java.io.*;

public class Main {
  public static void main(String[] args) throws FileNotFoundException{
    //Instantiating the File class
    // File file = new File("output.txt");
    // //Instantiating the PrintStream class
    // PrintStream stream = new PrintStream(file);
    // System.out.println("From now on "+file.getAbsolutePath()+" will be your console");
    // System.setOut(stream);
    // //Printing values to file

    String filename;
    int courseMinPen;
    int pairsPen;
    int brothersPen;

    filename = args[0];
    courseMinPen = Integer.parseInt(args[1]);
    pairsPen = Integer.parseInt(args[2]);
    brothersPen = Integer.parseInt(args[3]);

    Parser parser = new Parser(filename);
    Schedule initialSchedule = parser.getSchedule();
    System.out.println("The Schedule:");
    System.out.println(initialSchedule.toString());
    HashMap<String, Course> allCourses = parser.getCourseMap();
    HashMap<String, Lab> allLabs = parser.getLabMap();
    int initialPenalty = parser.getInitialPenalty();
    ArrayList<Unit> unitsToProcess = orderedUnitsForAdding(allCourses, allLabs);
    System.out.println("Units Made");

    Generator search = new Generator(initialSchedule, initialPenalty);
    search.generateFBound(unitsToProcess);
    System.out.println("Generator Obj Created");

    // Generator gen = new Generator();
    // gen.createFBound(parser.getMap());

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

    bubbleSort(toReturn);

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
