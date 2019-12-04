package coursesULabs;

import schedule.Slot;

public class Lab extends Unit{

    private int tutNum;
    private String tutOrLab = "LAB";

    public int getTutNum() {
        return tutNum;
    }

    public void setTutNum(int tutNum) {
        this.tutNum = tutNum;
    }

    public boolean isString(String parserInput){
        String formatLab = toString();

        return formatLab.equals(parserInput);
    }

    public String toString(){
        //CPSC433TUT01
        //CPSC433LEC01TUT01
        return (lectureNum == 0) ?
            courseType + courseNum + "TUT" + String.format("%02d", tutNum) :
            courseType + courseNum + "LEC" + String.format("%02d", lectureNum) + "TUT" + String.format("%02d", tutNum);
    }

    public String toPrettyString(){
        //CPSC433TUT01
        //CPSC433LEC01TUT01
        return (lectureNum == 0) ?
            courseType + " " + courseNum + " " + tutOrLab + String.format(" %02d", tutNum) :
            courseType + " " + courseNum + " LEC " + String.format("%02d ", lectureNum) + tutOrLab + String.format(" %02d", tutNum);
    }

    public Lab (int id, int lectureNum, String courseType, int courseNum, int tutNum, String tutOrLab){
        super.id = id;
        super.lectureNum = lectureNum;
        super.courseType = courseType;
        super.courseNum = courseNum;
        this.tutNum = tutNum;
        this.tutOrLab = tutOrLab;
    }

    public Lab (int id, int lectureNum, String courseType, int courseNum, int tutNum){
        super.id = id;
        super.lectureNum = lectureNum;
        super.courseType = courseType;
        super.courseNum = courseNum;
        this.tutNum = tutNum;
    }

    public Lab (int id, String courseType, int courseNum, int tutNum){
        super.id = id;
        super.courseType = courseType;
        super.courseNum = courseNum;
        this.tutNum = tutNum;
    }

    public Lab (int id, String courseType, int courseNum, int tutNum, String tutOrLab){
        super.id = id;
        super.courseType = courseType;
        super.courseNum = courseNum;
        this.tutNum = tutNum;
        this.tutOrLab = tutOrLab;
    }

    public Lab (int id, int lectureNum, String courseType, int courseNum, Slot slot, int tutNum) {
        super.id = id;
        this.lectureNum = lectureNum;
        this.courseType = courseType;
        this.courseNum = courseNum;
        this.tutNum = tutNum;
    }
    public Lab(Lab l){
        super.id = l.id;
        this.lectureNum= l.lectureNum;
        this.courseType = new String(l.courseType);
        this.courseNum = l.courseNum;
        //NOTE I'm not referencing back to the slot because I don't know when we use it... 
        this.tutNum = l.tutNum;
        this.tutOrLab = new String(l.tutOrLab);
    }

}
