import parser.*;
import schedule.*;
import tree.*;

class main{
  public static void main(String[] args){

    String filename;
    int courseMinPen;
    int pairsPen;
    int brothersPen;




    filename = args[0];
    courseMinPen = Integer.parseInt(args[1]);
    pairsPen = Integer.parseInt(args[2]);
    brothersPen = Integer.parseInt(args[3]);

    Parser parser = new Parser();
    parser.parseInput(filename, courseMinPen, pairsPen, brothersPen);

    Schedule schedule = new Schedule(parser.partialAssignments, parser.courseSlots, parser.labSlots);

    //Pen not gonna stay at 0. Ask Kaitlyn what it'll be.
    Generator gen = new Generator(schedule, 0);
    gen.createFBound(parser.getMap());


  }
}
