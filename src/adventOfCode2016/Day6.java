package adventOfCode2016;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertEquals;


public class Day6 {

    public static void main(String[] args) {
        if (args.length > 0) {
            String filepath = args[0];
            try {
                String result = new Day6().processFirstWay(Files.readAllLines(Paths.get(filepath)));
                System.out.println(result);
                String result2 = new Day6().processSecondWay(Files.readAllLines(Paths.get(filepath)));
                System.out.println(result2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no args");
        }

    }

    public String processFirstWay(List<String> lines) {
        return process(lines, mostFrequent);
    }

    public String processSecondWay(List<String> lines) {
        return process(lines, lessFrequent);
    }

    Comparator<? super Map.Entry<Character, Integer>> lessFrequent = Comparator.comparing(Map.Entry::getValue);
    Comparator<? super Map.Entry<Character, Integer>> mostFrequent = (e1, e2) -> e2.getValue().compareTo(e1.getValue());

    public String process(List<String> lines, Comparator<? super Map.Entry<Character, Integer>> comparatorForSorting) {
        Character freqCh;
        String out = "";
        for (int i = 0; i < lines.get(0).length(); i++) {
            Map<Character, Integer> charFreqInPosition = new HashMap<>();
            for (String line : lines) {
                Character c = line.charAt(i);
                charFreqInPosition.put(c, charFreqInPosition.containsKey(c) ? charFreqInPosition.get(c) + 1 : 1);

            }
            freqCh = charFreqInPosition.entrySet().stream()
                    .sorted(comparatorForSorting)
                    .map(Map.Entry::getKey).findFirst().get();
            out += freqCh;
        }
        return out;
    }


    @Test
    public void test1() {
        List<String> list = new ArrayList<>();
        list.add("eedadn");
        list.add("drvtee");
        list.add("eandsr");
        list.add("raavrd");
        list.add("atevrs");
        list.add("tsrnev");
        list.add("sdttsa");
        list.add("rasrtv");
        list.add("nssdts");
        list.add("ntnada");
        list.add("svetve");
        list.add("tesnvt");
        list.add("vntsnd");
        list.add("vrdear");
        list.add("dvrsen");
        list.add("enarar");
        assertEquals("easter", processFirstWay(list));
        assertEquals("advent", processSecondWay(list));
    }

}



