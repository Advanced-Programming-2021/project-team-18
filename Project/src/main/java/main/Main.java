package main;

import data.DataManager;

public class Main {
    private static ProgramController program;

    public static void main(String[] args) {
        program = new ProgramController();
        program.run();
    }
}
