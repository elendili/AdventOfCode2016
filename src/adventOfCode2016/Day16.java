package adventOfCode2016;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.toList;

public class Day16 {
    class DragonList extends ArrayList<Boolean> {
        public DragonList() {
            super();
        }

        public DragonList(String string) {
            super();
            this.addAll(Arrays.stream(string.split("")).map("1"::equals).collect(toList()));
        }

        @Override
        public String toString() {
            return this.stream().map(e -> e ? "1" : "0").reduce((e1, e2) -> e1 + e2).get();
        }

        DragonList expandByDragonCurve() {
            List<Boolean> b = (List<Boolean>) this.clone();
            Collections.reverse(b);
            b = b.stream().map(e -> !e).collect(Collectors.toCollection(DragonList::new));
            this.add(false);
            this.addAll(b);
            return this;
        }

        DragonList fillByDragon(int size) {
            while (this.size() < size) {
                expandByDragonCurve();
            }
            this.removeRange(size, this.size());
            return this;
        }


    }

    private DragonList _checksum(final DragonList s) {
        List<Boolean> checksum = IntStream.range(0, s.size() - 1)
                .filter(e -> e % 2 == 0)
                .mapToObj(e -> s.subList(e, e + 2))
                .filter(e -> e.size() == 2)
                .map(e -> e.get(0) == e.get(1) ? TRUE : FALSE)
                .collect(toList());
        DragonList d = new DragonList();
        d.addAll(checksum);
        return d;
    }

    DragonList degradeToChecksum(DragonList d) {
        do {
            d = _checksum(d);
        } while (d.size() % 2 == 0);
        return d;
    }

    @Test
    public void dragonCurveTest() {
        Assert.assertEquals("100", new DragonList("1").expandByDragonCurve().toString());
        Assert.assertEquals("001", new DragonList("0").expandByDragonCurve().toString());
        Assert.assertEquals("11111000000", new DragonList("11111").expandByDragonCurve().toString());
        Assert.assertEquals("1111000010100101011110000",
                new DragonList("111100001010").expandByDragonCurve().toString());
    }

    @Test
    public void fillByDragonTest() {
        Assert.assertEquals("10000011110010000111110", new DragonList("10000").fillByDragon(23).toString());
        Assert.assertEquals("10000011110010000111", new DragonList("10000").fillByDragon(20).toString());
        Assert.assertEquals("100", new DragonList("1").fillByDragon(3).toString());
        Assert.assertEquals("001", new DragonList("0").fillByDragon(3).toString());
        Assert.assertEquals("1111100000", new DragonList("11111").fillByDragon(10).toString());
    }

    @Test
    public void checksumTest() {
        Assert.assertEquals("100", degradeToChecksum(new DragonList("110010110100")).toString());
        Assert.assertEquals("0", degradeToChecksum(new DragonList("100")).toString());
    }


    @Test
    public void fullCycleTest() {
        Assert.assertEquals("01100", degradeToChecksum(new DragonList("10000").fillByDragon(20)).toString());
    }

    @Test
    public void part1() {
        DragonList d = new DragonList("11110010111001001").fillByDragon(272);
        d=degradeToChecksum(d);
        System.out.println(d + "   " + d.size()); //01110011101111011
    }

    @Test
    public void part2() {
        DragonList d = new DragonList("11110010111001001").fillByDragon(35651584);
        System.out.println("" + d.size() +" "+ (d.size() == 35651584));
        d = degradeToChecksum(d);
        System.out.println(d + "   " + d.size()); //11001111011000111
    }

}
