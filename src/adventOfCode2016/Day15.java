package adventOfCode2016;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


public class Day15 {

    class DiscSystem {
        final List<Disc> disks;

        DiscSystem(Disc... ds) {
            disks = Arrays.asList(ds);
        }

        boolean isPassable(int moment) {
            boolean isPassable = true;
//            System.out.println(_disks);
            for (int i = 0; i < disks.size(); i++) {
                Disc disk = disks.get(i);
                disk.reset();
                int shift = moment + i + 1;
                isPassable &= disk.tickNTimes(shift) == 0;
            }
//            System.out.println(_disks);
            return isPassable;
        }

        int findMoment() {
            for (int i = 0; i < 100_000_000; i++) {
                if (isPassable(i)) return i;
            }
            return -1;
        }
    }

    class Disc {
        final int positionsCount;
        final int originalPosition;
        int currentPosition; //from 0 to positionsCount-1

        public Disc(int positionsCount, int originalPosition) {
            this.positionsCount = positionsCount;
            this.originalPosition = originalPosition;
            this.currentPosition = originalPosition;
        }

        void reset() {
            currentPosition = originalPosition;
        }

        int tickNTimes(int n) {
            int reminder = n % positionsCount;
            currentPosition = (currentPosition + reminder > (positionsCount - 1)) ?
                    (currentPosition + reminder - positionsCount) :
                    (currentPosition + reminder)
            ;
            return currentPosition;
        }

        boolean isSlot() {
            return currentPosition == 0;
        }

        @Override
        public String toString() {
            return "Disc in " + currentPosition + " from [0-" + positionsCount + "]";
        }
    }

    @Test
    public void tickNTimesTest() throws Exception {
        Assert.assertEquals(0, new Disc(2, 1).tickNTimes(1));
        Assert.assertEquals(1, new Disc(2, 1).tickNTimes(2));
        Assert.assertEquals(0, new Disc(3, 1).tickNTimes(2));
        Assert.assertEquals(1, new Disc(3, 2).tickNTimes(2));
        Assert.assertEquals(0, new Disc(3, 0).tickNTimes(0));
        Assert.assertEquals(1, new Disc(3, 0).tickNTimes(1));
        Assert.assertEquals(0, new Disc(5, 4).tickNTimes(5 + 1));
        Assert.assertEquals(0, new Disc(2, 1).tickNTimes(5 + 2));
    }

    @Test
    public void isPassableTest() throws Exception {
        DiscSystem ds = new DiscSystem(new Disc(5, 4), new Disc(2, 1));
        Assert.assertTrue(ds.isPassable(5));
        Assert.assertTrue(ds.isPassable(5));
        Assert.assertFalse(ds.isPassable(4));
        Assert.assertFalse(ds.isPassable(0));
    }

    @Test
    public void findMomentTest() throws Exception {
        DiscSystem ds = new DiscSystem(new Disc(5, 4), new Disc(2, 1));
        Assert.assertEquals(5, ds.findMoment());
    }

    @Test
    public void part1() throws Exception {
        DiscSystem ds = new DiscSystem(
                new Disc(13, 10),
                new Disc(17, 15),
                new Disc(19, 17),
                new Disc(7, 1),
                new Disc(5, 0),
                new Disc(3, 1));

        System.out.println(ds.findMoment()); //203660
    }

    @Test
    public void part2() throws Exception {
        DiscSystem ds = new DiscSystem(
                new Disc(13, 10),
                new Disc(17, 15),
                new Disc(19, 17),
                new Disc(7, 1),
                new Disc(5, 0),
                new Disc(3, 1),
                new Disc(11, 0)
        );

        System.out.println(ds.findMoment()); // 2408135
    }


/*
Disc #1 has 13 positions; at time=0, it is at position 10.
Disc #2 has 17 positions; at time=0, it is at position 15.
Disc #3 has 19 positions; at time=0, it is at position 17.
Disc #4 has 7 positions; at time=0, it is at position 1.
Disc #5 has 5 positions; at time=0, it is at position 0.
Disc #6 has 3 positions; at time=0, it is at position 1.
     */

}
