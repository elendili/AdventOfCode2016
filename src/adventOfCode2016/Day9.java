package adventOfCode2016;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

import static org.junit.Assert.assertEquals;


public class Day9 {

    public static void main(String[] args) {
        if (args.length > 0) {
            String filepath = args[0];
            try {
                Decompressor d = new Decompressor1().decompress(Files.readAllLines(Paths.get(filepath)).get(0));
                System.out.println(d.size());
                d = new Decompressor2().decompress(Files.readAllLines(Paths.get(filepath)).get(0));
                System.out.println(d.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no args");
        }

    }


    @Test
    public void test() {
        assertEquals(7, new Decompressor1().decompress("A(1x5)BC").size());
        assertEquals(9, new Decompressor1().decompress("(3x3)XYZ").size());
        assertEquals(11, new Decompressor1().decompress("A(2x2)BCD(2x2)EFG").size());
        assertEquals(6, new Decompressor1().decompress("(6x1)(1x3)A").size());
        assertEquals(18, new Decompressor1().decompress("X(8x2)(3x3)ABCY").size());
    }

    @Test
    public void test2() {
        assertEquals(9, new Decompressor2().decompress("(3x3)XYZ").size());
        assertEquals(20, new Decompressor2().decompress("X(8x2)(3x3)ABCY").size());
        assertEquals(241920, new Decompressor2().decompress("(27x12)(20x12)(13x14)(7x10)(1x12)A").size());
        assertEquals(445, new Decompressor2().decompress("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN").size());
    }

    public static class Decompressor {
        final Pattern group = Pattern.compile("\\((\\d+)x(\\d+)\\)");
        long sizeForDecompressed = 0;

        long size() {
            return sizeForDecompressed;
        }

    }

    public static class Decompressor1 extends Decompressor {
        public Decompressor decompress(String input) {

            for (String residue = input; !residue.isEmpty(); ) {
                Matcher matcher = group.matcher(residue);
                if (matcher.find()) {
                    int size = Integer.parseInt(matcher.group(1));
                    int repetitions = Integer.parseInt(matcher.group(2));
                    String[] s = residue.split(group.pattern(), 2);
                    sizeForDecompressed += s[0].length();
                    if (s.length == 1) break;
                    size = s[1].length() < size ? s[1].length() : size;
                    String extracted = String.join("", Collections.nCopies(repetitions, s[1].substring(0, size)));
                    sizeForDecompressed += extracted.length();
                    residue = s[1].substring(size);

                } else {
                    sizeForDecompressed += residue.length();
                    break;
                }
            }
            return this;
        }

    }

    public static class Decompressor2 extends Decompressor {

        public Decompressor2 decompress(String input) {
            long[] counts = new long[input.length()];
            Arrays.fill(counts, 1);
            Matcher matcher = group.matcher(input);
            for (int i = 0; ; ) {
                if (matcher.find(i)) {
                    i = matcher.end();
                    int size = Integer.parseInt(matcher.group(1));
                    int repetitions = Integer.parseInt(matcher.group(2));
                    Arrays.fill(counts, matcher.start(), matcher.end(), 0);
                    for (int j = i; j < i + size; j++) {
                        counts[j] = counts[j] * repetitions;
                    }
                } else {
                    break;
                }
            }
            sizeForDecompressed = LongStream.of(counts).sum();
            return this;
        }


    }


}

