package adventOfCode2016;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class Day7 {

    public static void main(String[] args) {
        if (args.length > 0) {
            String filepath = args[0];
            try {
                String result = new Day7().processFirstWay(Files.readAllLines(Paths.get(filepath)));
                System.out.println(result);
                String result2 = new Day7().processSecondWay(Files.readAllLines(Paths.get(filepath)));
                System.out.println(result2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no args");
        }

    }

    public String processFirstWay(List<String> lines) {
        return ""+lines.stream().filter(e->new Address(e).supportsTls()).count();
    }
    public String processSecondWay(List<String> lines) {
        return ""+lines.stream().filter(e->new Address(e).supportsSsl()).count();
    }


    @Test
    public void test1() {
        assertTrue(new Address("abba[mnop]qrst").supportsTls());
        assertFalse(new Address("abcd[bddb]xyyx").supportsTls());
        assertFalse(new Address("aaaa[qwer]tyui").supportsTls());
        assertTrue(new Address("ioxxoj[asdfgh]zxcvbn").supportsTls());
        assertEquals("2",processFirstWay(new ArrayList<String>(){{add("abba");add("aaaa[a]");add("zy[zy]zyyz");}}));
    }

    @Test
    public void test2() {
        assertTrue(new Address("aba[bab]xyz").supportsSsl());
        assertFalse(new Address("xyx[xyx]xyx").supportsSsl());
        assertTrue(new Address("aaa[kek]eke").supportsSsl());
        assertTrue(new Address("zazbz[bzb]cdb").supportsSsl());
        assertEquals("2",processSecondWay(new ArrayList<String>(){{add("aba[bab]");add("[121]12123");add("aba[aba]aba");add("aab[a]");}}));
    }

}


class Address {
    final List<String> unbracketedList, bracketedList, splitted;

    Address(String line) {
        splitted = Arrays.asList(line.split("\\[|\\]"));
        unbracketedList = IntStream.range(0, splitted.size()).filter(i -> i % 2 == 0).mapToObj(splitted::get).collect(toList());
        bracketedList = IntStream.range(0, splitted.size()).filter(i -> i % 2 != 0).mapToObj(splitted::get).collect(toList());
    }

    boolean supportsTls(){
        return ! bracketedList.stream().anyMatch(this::hasAbba)
                && unbracketedList.stream().anyMatch(this::hasAbba);
    }

    boolean supportsSsl(){
        Set<String> allAbas = unbracketedList.stream().map(this::getAbas).flatMap(Collection::stream).collect(toSet());
        return bracketedList.stream().anyMatch(e->hasBab(allAbas,e));
    }

    boolean hasAbba(String word){
        return word.matches(".*(.)(?!\\1)(.)\\2\\1.*");
    }
    List<String> getAbas(String word){
        return IntStream.rangeClosed(0,word.length()-3)
                .mapToObj(i->{
                       String s=word.substring(i,i+3);
                       return s.matches("(.)(?!\\1).\\1")?s:"";
                     })
                .filter(s->!s.isEmpty()).collect(toList());
    }
    boolean hasBab(Collection<String> abas, String word){
        return abas.stream().map(e->""+e.charAt(1)+e.charAt(0)+e.charAt(1)).anyMatch(word::contains);
    }

}
