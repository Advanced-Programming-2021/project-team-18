package main;

public class MainConsole {
    private static ProgramController program;

    public static void main(String[] args) {
        program = new ProgramController();
        program.runConsole();
    }
}
