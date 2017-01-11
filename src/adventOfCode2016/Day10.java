package adventOfCode2016;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;


public class Day10 {
    List<Out> outputs = IntStream.range(0, 100).mapToObj(Out::new).collect(toList());
    List<Bot> bots = IntStream.range(0, 210).mapToObj(Bot::new).collect(toList());
    List<Integer> values = new ArrayList<>();

    public static void main(String[] args) {
        if (args.length > 0) {
            String filepath = args[0];
            try {
                List<String> lines = Files.readAllLines(Paths.get(filepath));
                Day10 day10 = new Day10();
                day10.initializeSystem(lines);
                day10.process(Arrays.asList(17, 61));

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no args");
        }

    }

    void initializeSystem(List<String> lines) {
        lines.forEach(e -> {
                    List<Integer> d = Arrays.stream(e.split("\\D+")).filter(z -> !z.isEmpty()).map(Integer::parseInt).collect(Collectors.toList());
                    if (e.startsWith("value")) {
                        bots.get(d.get(1)).take(d.get(0));
                        values.add(d.get(0));
                    } else if (e.startsWith("bot")) {
                        Bot bot = bots.get(d.get(0));
                        if (e.matches(".*low.*output.*high.*bot.*")) {
                            bot.set(outputs.get(d.get(1)), bots.get(d.get(2)));
                        } else if (e.matches(".*low.*bot.*high.*bot.*")) {
                            bot.set(bots.get(d.get(1)), bots.get(d.get(2)));
                        } else if (e.matches(".*low.*bot.*high.*output.*")) {
                            bot.set(bots.get(d.get(1)), outputs.get(d.get(2)));
                        } else if (e.matches(".*low.*output.*high.*output.*")) {
                            bot.set(outputs.get(d.get(1)), outputs.get(d.get(2)));
                        }
                    }
                }
        );
        /*
        System.out.println(
                String.join("\n", bots.stream().filter(e -> e.chips.size() > 0 || e.higher != null)
                        .map(Bot::toString).collect(toList()))
        );*/
    }

    void process(List<Integer> forCompare) {
        System.out.println("======= PROCESS STARTED ======");
        while (values.size() > outputs.stream().mapToInt(e -> e.chips.size()).sum()) {
            bots.forEach(Bot::distribute);
        }
        System.out.println("======= BOTs which ever had deal with specific chips ======");
        bots.stream().filter(b -> b.history.contains(forCompare.get(0)) && b.history.contains(forCompare.get(1))).forEach(System.out::println);

        System.out.println("======= OUTS =======");
        System.out.println(String.join("\n", outputs.stream().filter(e -> !e.chips.isEmpty()).map(Out::toString).collect(toList())));

        System.out.println("======= MULTIPLY OUTS =======");
        System.out.println(outputs.stream().filter(e -> e.number < 3).mapToInt(e -> e.chips.get(0)).reduce((a, b) -> a * b).getAsInt());

    }


    @Test
    public void test() {
        List<String> lines = Arrays.asList(("value 5 goes to bot 2\n" +
                "bot 2 gives low to bot 1 and high to bot 0\n" +
                "value 3 goes to bot 1\n" +
                "bot 1 gives low to output 1 and high to bot 0\n" +
                "bot 0 gives low to output 2 and high to output 0\n" +
                "value 2 goes to bot 2").split("\\n"));

        Day10 day10 = new Day10();
        day10.initializeSystem(lines);
        day10.process(Arrays.asList(2, 5));
        System.out.println();
        System.out.println(String.join("\n", day10.outputs.stream().filter(e -> e.chips.size() > 0).map(Out::toString).collect(toList())));
    }

    class Out {
        List<Integer> chips = new ArrayList<>();
        Integer number;
        List<Integer> history = new ArrayList<>();

        Out(int number) {
            this.number = number;
        }

        void take(Integer chip) {
            chips.add(chip);
            history.add(chip);
            Collections.sort(chips);
            if (chips.size() > 2) throw new RuntimeException("more than 2+\n" + chips);
        }

        @Override
        public String toString() {
            return "# " + number + " chips:" + chips;
        }
    }

    class Bot extends Out {
        private Out lower, higher;

        Bot(int number) {
            super(number);
        }


        void set(Out lower, Out higher) {
            this.lower = lower;
            this.higher = higher;
        }

        void distribute() {
            if (chips.size() == 2) {
                lower.take(chips.get(0));
                higher.take(chips.get(1));
                chips.clear();
            }
        }

        @Override
        public String toString() {
            return super.toString() + " lower: " + lower.getClass().getSimpleName() + " " + lower.number
                    + ", higher: " + higher.getClass().getSimpleName() + " " + higher.number + "  ";
        }
    }
}

