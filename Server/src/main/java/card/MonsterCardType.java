package card;

public enum MonsterCardType {
    WARRIOR("WARRIOR"),
    BEASTWARRIOR("BEAST-WARRIOR"),
    FIEND("FIEND"),
    AQUA("AQUA"),
    BEAST("BEAST"),
    PYRO("PYRO"),
    SPELLCASTER("SPELLCASTER"),
    THUNDER("THUNDER"),
    DRAGON("DRAGON"),
    MACHINE("MACHINE"),
    ROCK("ROCK"),
    INSECT("INSECT"),
    CYBERSE("CYBERSE"),
    FAIRY("FAIRY"),
    SEASERPENT("SEASERPENT"),
    ALL("ALL");
    private String typeString;

    MonsterCardType(String typeString) {
        this.typeString = typeString;
    }
}
