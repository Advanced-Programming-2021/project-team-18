package card;

public enum MonsterCardAttribute {
    DARK("DARK"),
    EARTH("EARTH"),
    FIRE("FIRE"),
    LIGHT("LIGHT"),
    WATER("WATER"),
    WIND("WIND"),
    ALL("ALL");
    private String typeString;

    MonsterCardAttribute(String typeString) {
        this.typeString = typeString;
    }
}
