package adventOfCode2016;

import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static org.junit.Assert.assertEquals;


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

        String getHashForSuffix(int i) {
            byte[] bytes = (salt + i).getBytes();
            byte[] digestedBytes = md5.digest(bytes);
            String out = new BigInteger(digestedBytes).toString(16);
            return out;
        }

        List<Integer> findKey(int length){
            int limit = 1000000;
            List<Integer> keys = new ArrayList<>();
            for (int i = 0; i < limit && keys.size()<length; i++) {
                int index = findIndexForGoodCombination(i);
                i=index;
                keys.add(index);
            }
            return keys;
        }

        int findIndexForGoodCombination(int from) {
            final Pattern triple = Pattern.compile("(\\w)\\1{2}(?!\\1)");
            int limit = 1000000;
            int outIndex=from;
            char targetChar;
            String targetString;

            out:
            for (int i = from; i < from+limit; i++) {
                String hash = getHashForSuffix(i);
                Matcher matcher = triple.matcher(hash);
                if (matcher.find()) {
                    outIndex = i;
                    targetChar = matcher.group().charAt(0);
                    targetString = String.format("%0" + 5 + "d", 0).replace('0', targetChar);
                    for (int j = outIndex + 1; j <= outIndex + 1000; j++) {
                        if (getHashForSuffix(j).contains(targetString))
                            break out;
                    }
                }
            }
            return outIndex;
        }
    }

    @Test
    public void test() {
        KeySearcher k = new KeySearcher("abc");
        System.out.println(k.getHashForSuffix(39));
        System.out.println(k.getHashForSuffix(816));
        int z;
        System.out.println(z=k.findIndexForGoodCombination(0));
        System.out.println(k.findIndexForGoodCombination(z+1));
        System.out.println("=============");
        List<Integer> key = k.findKey(80);
        IntStream.range(1,key.size()+1).forEach(
                idx-> System.out.println( idx +". "+key.get(idx-1))
        );

        // cc38887a5
    }
}

