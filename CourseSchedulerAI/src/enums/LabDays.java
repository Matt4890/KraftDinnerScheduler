package enums;

enum LabDays {
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

}