package adventOfCode2016;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class Day12 {

    public static void main(String[] args) {
        if (args.length > 0) {
            String filepath = args[0];
            try {
                List<String> lines = Files.readAllLines(Paths.get(filepath));
                Computer c = new Computer(lines);
                c.process();
                System.out.println(c.registers.get("a"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no args");
        }

    }

    static class  Computer {
        HashMap<String, Integer> registers = new HashMap<>();
        List<List<String>> commands = new ArrayList<>();
        int pointer=0;
        Computer(List<String> lines){
            parse(lines);
            registers.put("a",0);
            registers.put("b",0);
            registers.put("c",1); // change this to 0 for part 1 of Day 12
            registers.put("d",0);
        }
        void process(){
            for (;pointer<commands.size() && pointer>=0;){
//                System.out.println(pointer);
                execute(commands.get(pointer));
            }
        }
        void parse(List<String> lines){
            for(String line:lines){
                String[] splitted = line.split(" ");
                commands.add(Arrays.asList(splitted[0],splitted[1],splitted.length>2?splitted[2]:""));
            }
        }
        void execute(List<String> command){
            switch (command.get(0)){
                case "cpy":
                    cpyXY(command.get(1),command.get(2));
                    break;
                case "dec":
                    decX(command.get(1));
                    break;
                case "inc":
                    incX(command.get(1));
                    break;
                case "jnz":
                    jnzXY(command.get(1),command.get(2));
                    break;
            }

        }
        void cpyXY(String value, String register) {
            registers.put(register, getValue(value));
            pointer++;
        }

        void incX(String key) {
            registers.put(key, registers.get(key) + 1);
            pointer++;
        }

        void decX(String key) {
            registers.put(key, registers.get(key) - 1);
            pointer++;
        }

        void jnzXY(String x, String y) {
            if (getValue(x)!=0){
                pointer = pointer+getValue(y);
            }else{
                pointer++;
            }
        }

        Integer getValue(String keyOrValue) {
            if (Character.isDigit(keyOrValue.charAt(keyOrValue.length()-1)) ) {
                return Integer.valueOf(keyOrValue);
            } else{
                return registers.get(keyOrValue);
            }
        }
    }

    @Test
    public void test(){
        Computer c = new Computer(Arrays.asList(("cpy 41 a\n" +
                "inc a\n" +
                "inc a\n" +
                "dec a\n" +
                "jnz a 2\n" +
                "dec a").split("\n")));
        c.process();
        assertEquals(new Integer(42),c.registers.get("a"));
    }
    {
        /*
        cpy x y copies x (either an integer or the value of a register) into register y.
inc x increases the value of register x by one.
dec x decreases the value of register x by one.
jnz x y jumps to an instruction y away (positive means forward; negative means backward), but only if x is not zero.
         */
    }
}

