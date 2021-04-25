package card;

public enum TrapType {
    NORMAL ("NORMAL"),
    CONTINUOUS ("CONTINUOUS"),
    COUNTER ("COUNTER");
    private String typeString;
    TrapType(String typeString) {
        this.typeString = typeString;
    }
}
