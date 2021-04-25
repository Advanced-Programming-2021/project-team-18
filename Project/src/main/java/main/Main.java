package main;

import data.DataManager;

public class Main {
    private static ProgramController program;

    public static void main(String[] args) {
        DataManager.loadCardsIntoAllCards();
        if(true) { return ; }
        program = new ProgramController();
        program.run();
    }
}
