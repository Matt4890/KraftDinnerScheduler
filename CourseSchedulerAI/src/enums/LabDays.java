package enums;

public enum LabDays {

    MONWED("MO"),
    TUETHR("TU"),
    FRI("FR");

    private final String STRING_REPR;

    private LabDays(String stringRepr) {
        this.STRING_REPR = stringRepr;
    }

    public String toString() {
        return this.STRING_REPR;
    }

    public static LabDays fromString(String str) {
        for (LabDays ld : LabDays.values()) {
            if (ld.toString().equalsIgnoreCase(str)) {
                return ld;
            }
        }
        return null;
    }

}