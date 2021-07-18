package game;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class Question {
    private String message;
    private ArrayList<String> options;
}
