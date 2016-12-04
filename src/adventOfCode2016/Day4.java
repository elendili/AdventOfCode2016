package adventOfCode2016;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;


public class Day4 {

    public static void main(String[] args) {
        if (args.length > 0) {
            String filepath = args[0];
            try {
                int result = new Day4().processFirstWay(Files.readAllLines(Paths.get(filepath)));
                System.out.println(result);
                int result2 = new Day4().processSecondWay(Files.readAllLines(Paths.get(filepath)));
                System.out.println(result2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no args");
        }

    }

    public int processFirstWay(List<String> lines) {
        int sum = 0;
        for (String line : lines) {
            if (calcCheckSum(getName(line)).equals(getOriginCheckSum(line))) {
                sum += getId(line);
            }
        }
        return sum;
    }

    public int processSecondWay(List<String> lines) {
        String result;
        for (String line : lines) {
            if (calcCheckSum(getName(line)).equals(getOriginCheckSum(line))) {
                result = rotateLetters(line);
                if (result.contains("northpole object storage"))
                    return getId(line);
            }
        }
        return 0;
    }

    String rotateLetters(String line) {
        return rotateLetters(getName(line), getId(line));
    }

    ;

    String rotateLetters(String line, int rotations) {
        String out = Arrays.asList(line.split("")).stream().map(e -> {
            if (!"-".equals(e)) {
                char c = e.charAt(0);
                int shift = rotations % 26;
                char newc = (char) (c + shift);
                if (newc > 'z') {
                    newc = (char) (96 + (shift - (122 - c)));
                    // x=120 shift=5 exp = 96+3=100
                    // z=122 shift=1 exp = 96+1=100
                    // 97+(shift - (122 - x))
                    // 97+(5 - (122 - 97))
                }
                return "" + newc;
            } else return " ";
        }).collect(Collectors.joining());
        return out.trim();
        //97  a
        //122  z
    }

    static public String getName(String line) {
        return line.replaceAll("\\d*", "").replaceAll("\\[.*\\]$", "");
    }

    static public String getOriginCheckSum(String line) {
        return line.replaceAll("^[^\\[]+\\[", "").replaceAll("\\]", "");
    }

    static int getId(String line) {
        return Integer.parseInt(line.replaceAll("\\D*", ""));
    }

    String calcCheckSum(String name) {
        name = name.replaceAll("\\W*", "");
        HashMap<String, Integer> frq = new HashMap<>();
        Arrays.asList(name.split("")).stream().forEach(e -> frq.put(e, frq.containsKey(e) ? frq.get(e) + 1 : 1));
        final StringBuilder calcCheckSum = new StringBuilder();
        frq.values().stream().sorted((v1, v2) -> v2.compareTo(v1)).distinct()
                .forEach(e -> {
                    frq.entrySet().stream().filter(e2 -> e2.getValue().equals(e))
                            .sorted((k1, k2) -> k1.getKey().compareTo(k2.getKey()))
                            .forEach(b -> calcCheckSum.append(b.getKey()));
                });
        calcCheckSum.setLength(calcCheckSum.length() < 5 ? calcCheckSum.length() : 5);
        return calcCheckSum.toString();
    }

    @Test
    public void test1() {
        List<String> list = new ArrayList<String>() {{
        }};
        list.add("aaaaa-bbb-z-y-x-123[abxyz]");
        list.add("a-b-c-d-e-f-g-h-987[abcde]");
        list.add("not-a-real-room-404[oarel]");
        list.add("totally-real-room-200[decoy]");
        assertEquals(1514, processFirstWay(list));

        assertEquals("very encrypted name", rotateLetters("qzmt-zixmtkozy-ivhz-343"));
    }

}



