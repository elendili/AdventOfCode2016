package adventOfCode2016;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;


public class Day3 {

    public static void main(String[] args) {
        if (args.length > 0) {
            String filepath = args[0];
            try {
                int result = new Day3().processFirstWay(Files.readAllLines(Paths.get(filepath)));
                System.out.println(result);
                result = new Day3().processSecondWay(Files.readAllLines(Paths.get(filepath)));
                System.out.println(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no args");
        }

    }

    public int processFirstWay(List<String> lines) {
        int count = 0;
        List<String> list;
        for (String line : lines) {
            list = Arrays.asList(line.split("\\s+"));
            count += isTriangle(list.stream().filter(e -> !e.isEmpty()).map(Integer::valueOf).collect(Collectors.toList())) ? 1 : 0;
        }
        return count;
    }

    List<List<Integer>> convert(List<String> lines) {
        List<List<Integer>> x = lines.stream().map(e ->
                Stream.of(e.split("\\s+")).filter(m -> !m.isEmpty()).map(Integer::valueOf).collect(Collectors.toList())
        ).collect(Collectors.toList());
        return x;
    }

    public int processSecondWay(List<String> lines) {
        int count = 0;
        List<List<Integer>> list = convert(lines);
        for (int i = 0; i <= list.size() - 3; i += 3) {
            count += isTriangle(list.get(i).get(0), list.get(i + 1).get(0), list.get(i + 2).get(0)) ? 1 : 0;
            count += isTriangle(list.get(i).get(1), list.get(i + 1).get(1), list.get(i + 2).get(1)) ? 1 : 0;
            count += isTriangle(list.get(i).get(2), list.get(i + 1).get(2), list.get(i + 2).get(2)) ? 1 : 0;
        }
        return count;
    }

    public boolean isTriangle(Integer _1, Integer _2, Integer _3) {
        return isTriangle(Arrays.asList(_1, _2, _3));
    }

    public boolean isTriangle(List<Integer> lengths) {
        boolean isTriangle = true;
        isTriangle &= lengths.get(0) + lengths.get(1) > lengths.get(2);
        isTriangle &= lengths.get(1) + lengths.get(2) > lengths.get(0);
        isTriangle &= lengths.get(2) + lengths.get(0) > lengths.get(1);
        return isTriangle;
    }

    @Test
    public void test1() {
        List<String> list = new ArrayList<String>() {{
            add("3 4 5");
            add("5 10 25");
        }};
        assertEquals(1, processFirstWay(list));
        list = new ArrayList<String>() {{
            add("5 10 25");
        }};
        assertEquals(0, processFirstWay(list));
        list = new ArrayList<String>() {{
            add("  554  421  618");
        }};
        assertEquals(1, processFirstWay(list));
        list = new ArrayList<String>() {{
            add("  1  2  3");
            add("  3  2  1");
            add("  2  1  3");
        }};
        assertEquals(0, processFirstWay(list));
        list = new ArrayList<String>() {{
            add("  3  4  5");
            add("  5  6  7");
            add("  6  7  8");
        }};
        assertEquals(3, processFirstWay(list));
    }

    @Test
    public void test2() {
        List<String> list = new ArrayList<String>() {{
            add("100 301 501");
            add("100 302 502");
            add("200 303 503");
            add("201 401 601");
            add("202 803 602");
            add("203 402 603");
        }};
        assertEquals(4, processSecondWay(list));
    }
}



