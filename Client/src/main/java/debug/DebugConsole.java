package debug;


import lombok.SneakyThrows;
import utility.Utility;

public class DebugConsole {

    @SneakyThrows
    public static void main(String[] args) {
        String[] s = Utility.makeArray("a" , "b");
        System.out.println(s.length);
    }
}
