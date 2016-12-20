package adventOfCode2016;

import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;


public class Day14 {

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

    class KeySearcher {
        final String salt;
        final MessageDigest md5;

        {
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        KeySearcher(String salt) {
            this.salt = salt;
            byte[] bytes = salt.getBytes();
            md5.digest(bytes);
        }

        String getHashFor(Object o) {
//            md5.reset();
            byte[] bytes = String.valueOf(o).getBytes();
            byte[] digestedBytes = md5.digest(bytes);
//            String out = new BigInteger(digestedBytes).toString(16);
            String out = getHex(digestedBytes).toLowerCase();
            return out;
        }

        String getHex(byte[] bytes) {
            BigInteger bi = new BigInteger(1, bytes);
            return String.format("%0" + (bytes.length << 1) + "X", bi);
        }

        List<Integer> findKey(int length) {
            int limit = 1000000;
            List<Integer> keys = new ArrayList<>();
            for (int i = 0; i < limit && keys.size() < length; i++) {
                if (isItProperKey(i)) keys.add(i);
            }
            return keys;
        }

        boolean isItProperKey(int i) {
            final Pattern triple = Pattern.compile("(\\w)\\1{2}");
            char targetChar;
            String hash = getHashFor(salt + i);
            Matcher matcher = triple.matcher(hash);
            if (matcher.find()) {
                targetChar = matcher.group().charAt(0);
                    if (quintFind(targetChar, i+1, i + 1000+1) > 0) return true;
            }
            return false;
    }

    int quintFind(char c, int from, int to) {
        String quint = repeat(c, 5);
        for (int i = from; i < to; i++) {
            String hash = getHashFor(salt + i);
            if (hash.contains(quint))
                return i;
        }
        return -1;
    }

    String repeat(char c, int count) {
        return String.format("%0" + count + "d", 0).replace('0', c);
    }

}

    @Test
    public void test() {
        KeySearcher k = new KeySearcher("abc");

        int i = 39;
        System.out.println(i + " ======");
        System.out.println(k.getHashFor("abc" + i));
        System.out.println(k.quintFind('e', i, i + 1000));
        i = 92;
        System.out.println(i + " ======");
        System.out.println(k.getHashFor("abc" + i));
        System.out.println(k.quintFind('9', i, i + 1000));
        i = 110;
        System.out.println(i + " ======");
        System.out.println(k.getHashFor("abc" + i));
        System.out.println(k.quintFind('9', i, i + 1000));
        i = 184;
        System.out.println(i + " ======");
        System.out.println(k.getHashFor("abc" + i));
        System.out.println(k.quintFind('9', i, i + 1000));
        i = 291;
        System.out.println(i + " ======");
        System.out.println(k.getHashFor("abc" + i));
        System.out.println(k.quintFind('4', i, i + 1000));
        i = 8811;
        System.out.println(i + " ======");
        System.out.println(k.getHashFor("abc" + i));
        System.out.println(k.quintFind('1', i+1, i + 1000));

    }

    @Test
    public void test2() {
        KeySearcher k = new KeySearcher("abc");
        List<Integer> key = k.findKey(64);
        IntStream.range(1, key.size() + 1).forEach(
                idx -> System.out.println(idx+". "+key.get(idx - 1))
        );
    }
    @Test
    public void part1() {
        KeySearcher k = new KeySearcher("cuanljph");
        List<Integer> key = k.findKey(64);
        IntStream.range(1, key.size() + 1).forEach(
                idx -> System.out.println(idx+". "+key.get(idx - 1))
        );
    }
}

