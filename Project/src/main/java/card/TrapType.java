package card;

public enum TrapType {
    NORMAL ("NORMAL"),
    CONTINUOUS ("CONTINOUS"),
    COUNTER ("COUNTER");
    private String typeString;
    TrapType(String typeString) {
        this.typeString = typeString;
    }
}
