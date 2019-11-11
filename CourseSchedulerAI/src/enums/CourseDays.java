package enums;

public enum CourseDays {

    MONWEDFRI("MO"),
    TUETHR("TU");

    private final String STRING_REPR;

    private CourseDays(String stringRepr) {
        this.STRING_REPR = stringRepr;
    }

    public String toString() {
        return this.STRING_REPR;
    }

    public static CourseDays fromString(String str) {
        for (CourseDays ld : CourseDays.values()) {
            if (ld.toString().equalsIgnoreCase(str)) {
                return ld;
            }
        }
        return null;
    }

}