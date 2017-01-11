package adventOfCode2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


public class Day11 {

    public static void main(String[] args) {
        if (args.length > 0) {
            String filepath = args[0];
            try {
                List<String> lines = Files.readAllLines(Paths.get(filepath));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no args");
        }

    }

}

