package menus;

import java.util.HashMap;

// By Sina
public abstract class Menu {
    protected static final HashMap<String, String[]> mustInputs = new HashMap<>();
    protected static final HashMap<String, String[]> mayInputs = new HashMap<>();
    protected static final String INVALID_COMMAND = "invalid command";
    protected static final String MENU_EXIT = "menu exit";
    protected static final String SHOW_MENU = "menu show-current";
    protected static final String NAVIGATION_DENIED = "menu navigation is not possible";
    protected static String name;

    public abstract void runMenu();

}
