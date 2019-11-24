import parser.*;
public class Main{
  public static void main(String[] args){

    String filename;
    int courseMinPen;
    int pairsPen;
    int brothersPen;


    filename = args[0];
    courseMinPen = Integer.parseInt(args[1]);
    pairsPen = Integer.parseInt(args[2]);
    brothersPen = Integer.parseInt(args[3]);

    Parser parser = new Parser(filename);
    



    // Generator gen = new Generator();
    // gen.createFBound(parser.getMap());


  }
}
