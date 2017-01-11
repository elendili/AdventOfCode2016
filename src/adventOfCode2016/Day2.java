package adventOfCode2016;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class Day2 {

    public static void main(String[] args) {
        if (args.length > 0) {
            String filepath = args[0];
            try {
                String result = new Day2().processFirstWay(Files.readAllLines(Paths.get(filepath)));
                System.out.println(result);
                result = new Day2().processSecondWay(Files.readAllLines(Paths.get(filepath)));
                System.out.println(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no args");
        }

    }

    public String processFirstWay(List<String> instrs) {
        String code = "";
        Pointer pointer = new Pointer(firstKeypad, 1, 1);
        for (String instr : instrs) {
            pointer = processInstr(pointer, instr);
            code += pointer.getKey();
        }
        System.out.println(code);
        return code;
    }


    public String processSecondWay(List<String> instrs) {
        String code = "";
        Pointer pointer = new Pointer(secondKeypad, 0, 2);
        for (String instr : instrs) {
            pointer = processInstr(pointer, instr);
            code += pointer.getKey();
        }
        System.out.println(code);
        return code;
    }

    public Pointer processInstr(Pointer currentPointer, String instr) {
        for (String letter : instr.split("")) {
            switch (letter) {
                case "U":
                    currentPointer = currentPointer.up();
                    break;
                case "L":
                    currentPointer = currentPointer.left();
                    break;
                case "D":
                    currentPointer = currentPointer.down();
                    break;
                case "R":
                    currentPointer = currentPointer.right();
                    break;
            }
//            System.out.println(currentPointer);
        }
        return currentPointer;
    }

    @Test
    public void test() {
        List<String> list = new ArrayList<String>() {{
            add("ULL");
            add("RRDDD");
            add("LURDL");
            add("UUUUD");
        }};
        assertEquals("1985", processFirstWay(list));
        assertEquals("5DB3", processSecondWay(list));
    }

    final String[][] firstKeypad = {{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}};
    final String[][] secondKeypad = {{null, null, "1", null, null},
            {null, "2", "3", "4", null}, {"5", "6", "7", "8", "9"}, {null, "A", "B", "C", null}, {null, null, "D", null, null}};

}

class Pointer {
    final String[][] keypad;
    final int x, y;

    Pointer(String[][] keypad, int x, int y) {
        this.keypad = keypad;
        this.x = x;
        this.y = y;
    }

    Pointer left() {
        int newX = x <= 0 ? 0 : keypad[x - 1][y] == null ? x : x - 1;
        return new Pointer(keypad, newX, y);
    }

    Pointer right() {
        int lastIndex = keypad.length - 1;
        int newX = x >= lastIndex ? lastIndex : (keypad[x + 1][y] == null ? x : x + 1);
        return new Pointer(keypad, newX, y);
    }

    Pointer up() {
        int newY = y <= 0 ? 0 : keypad[x][y - 1] == null ? y : y - 1;
        return new Pointer(keypad, x, newY);
    }

    Pointer down() {
        int lastIndex = keypad[x].length - 1;
        int newY = y >= lastIndex ? lastIndex : (keypad[x][y + 1] == null ? y : y + 1);
        return new Pointer(keypad, x, newY);
    }

    String getKey() {
        return keypad[y][x];
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "] key=" + getKey();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Point)
                && ((Point) obj).x == x
                && ((Point) obj).y == y;
    }
}



