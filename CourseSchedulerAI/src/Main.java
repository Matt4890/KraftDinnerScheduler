import parser.*;
import schedule.*;
import tree.Generator;
import coursesULabs.*;
import java.util.*;

public class Main {
  public static void main(String[] args) {

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
    return toReturn;
  }
}
