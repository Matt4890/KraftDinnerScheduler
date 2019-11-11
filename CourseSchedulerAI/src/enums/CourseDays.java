package enums;

enum CourseDays {

    MONWEDFRI("MO"),
    TUETHR("TU");

    private final String STRING_REPR;

    private CourseDays(String stringRepr) {
        this.STRING_REPR = stringRepr;
    }

    public String toString() {
        return this.STRING_REPR;
    }

}