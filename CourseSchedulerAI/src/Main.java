class main{
  public static void main(String[] args){

    string filename;
    int courseMinPen;
    int pairsPen;
    int brothersPen;


    filename = args[0];
    courseMinPen = args[1];
    pairsPen = args[2];
    brothersPen = args[3];

    Parser parser = new Parser;
    parser.parseInput(filename, courseMinPen, pairsPen, brothersPen);

    Generator gen = new Generator;
    gen.createFBound(parser.getMap());


  }
}
