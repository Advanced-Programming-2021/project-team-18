package card;

public enum SpellType {
    NORMAL ("NORMAL"),
    RITUAL ("RITUAL"),
    EQUIP ("EQUIP"),
    FIELD ("FIELD"),
    CONTINUOUS ("CONTINOUS"),
    COUNTER ("COUNTER"),
    QUICKPLAY ("QUICKPLAY"),
    ALL ("ALL");
    private String typeString;
    SpellType(String typeString) {
        this.typeString = typeString;
    }
}
