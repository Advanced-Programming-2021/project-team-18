package debug;

import data.DataManager;
import game.User;
import utility.Utility;

import java.util.HashMap;

public class Debug {
    public static void main(String[] args) {
        HashMap<String,String> map = Utility.getCommand("--deck-name a");
        for(String x : map.keySet()) {
            System.out.println(x + " : " + map.get(x));
        }
    }
}
