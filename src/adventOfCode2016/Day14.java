package adventOfCode2016;

import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


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


    @Test
    public void test() {
        KeySearcher k = new KeySearcher("abc", 1);

        int i = 39;
        System.out.println(i + " ======");
        System.out.println(k.getHash.apply("abc" + i));
        System.out.println(k.quintFind('e', i, i + 1000));
        i = 92;
        System.out.println(i + " ======");
        System.out.println(k.getHash.apply("abc" + i));
        System.out.println(k.quintFind('9', i, i + 1000));
        i = 110;
        System.out.println(i + " ======");
        System.out.println(k.getHash.apply("abc" + i));
        System.out.println(k.quintFind('9', i, i + 1000));
        i = 184;
        System.out.println(i + " ======");
        System.out.println(k.getHash.apply("abc" + i));
        System.out.println(k.quintFind('9', i, i + 1000));
        i = 291;
        System.out.println(i + " ======");
        System.out.println(k.getHash.apply("abc" + i));
        System.out.println(k.quintFind('4', i, i + 1000));
        i = 8811;
        System.out.println(i + " ======");
        System.out.println(k.getHash.apply("abc" + i));
        System.out.println(k.quintFind('1', i + 1, i + 1000));

    }

    @Test
    public void testABC() {
        KeySearcher k = new KeySearcher("abc", 1);
//        List<Integer> key = k.findKey(64);
        assertTrue(k.isItProperKey(39));
        assertTrue(k.isItProperKey(92));
        assertTrue(k.isItProperKey(22193));
        assertTrue(k.isItProperKey(22728));
    }

    @Test
    public void testABC_2() {
        KeySearcher k2016 = new KeySearcher("abc", 2);
        assertEquals("a107ff634856bb300138cac6568c0f24", k2016.hasher.apply("abc0"));
        System.out.println(k2016.hasher.apply("abc5").contains("222"));

        System.out.println(k2016.hasher.apply("abc10").contains("eee"));
        System.out.println(k2016.hasher.apply("abc22551").contains("fff"));
        System.out.println(k2016.hasher.apply("abc22859").contains("fffff"));

        assertTrue(k2016.isItProperKey(10));
        assertTrue(k2016.isItProperKey(22551));
//        List<Integer> key = k2016.findKey(64);
//        IntStream.range(1, key.size() + 1).forEach(
//                idx -> System.out.println(idx + ". " + key.get(idx - 1))
//        );
    }

    @Test
    public void part1() {
        KeySearcher k = new KeySearcher("cuanljph", 1);
        List<Integer> key = k.findKey(64);
        // answer: 64th key is 23769
    }

    @Test
    public void part2_ABC() {
        KeySearcher k = new KeySearcher("abc", 2);
        List<Integer> key = k.findKey(64);
        // 64th is 22551
    }

    @Test
    public void part2() {
        KeySearcher k = new KeySearcher("cuanljph", 2);
        List<Integer> key = k.findKey(64);
    }


}

//==============================================================================================
//==============================================================================================
//==============================================================================================

class KeySearcher {

    final MessageDigest md5;

    {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    final String salt;
    public final Function<String, String> hasher;
    private Map<String, String> hashes = new HashMap<>();

    KeySearcher(String salt, int mode) {
        this.salt = salt;
        if (mode == 1) {
            this.hasher = getHash;
        } else if (mode == 2) {
            this.hasher = getHash2016times;
        } else
            this.hasher = null;
    }


    String repeat(char c, int count) {
        return String.format("%0" + count + "d", 0).replace('0', c);
    }


    protected String hashOnce(String o) {
        md5.reset();
        md5.update(o.getBytes(Charset.forName("UTF-8")));
        byte[] digestedBytes = md5.digest();
        String hash = getHex(digestedBytes).toLowerCase();
        return hash;
    }

    final Function<String, String> getHash = (String o) -> {
        if (!hashes.containsKey(o)) {
            String hash = hashOnce(o);
            hashes.put(o, hash);
        }
        return hashes.get(o);
    };

    final Function<String, String> getHash2016times = (String o) -> {
        if (!hashes.containsKey(o)) {
            String hash = o;
            for (int i = 0; i <= 2016; i++) {
                 hash  = hashOnce(hash);
            }
            hashes.put(o, hash);
        }
        return hashes.get(o);
    };

    List<Integer> findKey(int count) {
        int limit = 1000000;
        List<Integer> keys = new ArrayList<>();
        for (int i = 0; i < limit && keys.size() < count; i++) {
            if (isItProperKey(i)) {
                keys.add(i);
                System.out.println(keys.size() + ". " + i);
            }
        }
        return keys;
    }

    boolean isItProperKey(int i) {
        final Pattern triple = Pattern.compile("(\\w)\\1{2}");
        char targetChar;
        String string = salt + i;
        String hash = hasher.apply(string);
        Matcher matcher = triple.matcher(hash);
        if (matcher.find()) {
            targetChar = matcher.group().charAt(0);
            if (quintFind(targetChar, i + 1, i + 1000 + 1) > 0) return true;
        }
        return false;
    }

    int quintFind(char c, int from, int to) {
        String quint = repeat(c, 5);
        for (int i = from; i < to; i++) {
            String string = salt + i;
            String hash = this.hasher.apply(string);
            if (hash.contains(quint))
                return i;
        }
        return -1;
    }

    private static String getHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();

        for (byte aDigest : digest) {
            if ((0xff & aDigest) < 0x10) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(0xff & aDigest));
        }
        return sb.toString();
    }

    String getHex2(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "x", bi);
    }

}


