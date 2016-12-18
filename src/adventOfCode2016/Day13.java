package adventOfCode2016;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Math.abs;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;


public class Day13 {

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

    class PathFinder {
        Maze maze;
        Point initialPoint = new Point(1, 1);
        Point currentPoint = initialPoint;

        PathFinder(Maze maze) {
            this.maze = maze;
        }


        Trace findPathTo(Point targetPoint) {
//            int distance = initialPoint.distanceTo(targetPoint);
            List<Trace> l = depthSearch(initialPoint, targetPoint, 96);
            l = l.stream().filter(e -> e.contains(targetPoint)).collect(toList());
            l = l.stream().sorted(Comparator.comparingInt(e2 -> e2.stepsFor(targetPoint))).collect(toList());
            return l.get(0);
        }

        Set<Point> findLocationsInSteps(int stepsCount) {
//            int distance = initialPoint.distanceTo(targetPoint);
            List<Trace> l = depthSearch(initialPoint, null, stepsCount);
            Set<Point> uniqueLp = l.stream().map(e -> e.points).flatMap(Collection::stream).collect(toSet());
            return uniqueLp;
        }

        List<Trace> depthSearch(Point from, Point to, int steps) {
            ArrayList<Point> l = new ArrayList<Point>();
            l.add(from);
            return depthSearch(new Trace(maze, l), to, steps);
        }

        private List<Trace> depthSearch(Trace trace, Point to, int steps) {
            List<Trace> variations1, variations = new ArrayList<>();
            List<Point> allowableSteps = trace.allowableNextSteps();
            if (allowableSteps.contains(to) || trace.contains(to)) {
                variations.add(new Trace(trace).add(to));
            } else if (steps < 1) {
            } else {
                for (Point point1 : allowableSteps) {
                    Trace variation = new Trace(trace);
                    variation.add(point1);
                    if (steps > 1) {
                        variations1 = depthSearch(variation, to, steps - 1);
                        if (variations1.isEmpty()) variations.add(variation);
                        else variations.addAll(variations1);
                    } else {
                        variations.add(variation);
                    }
                }
            }
            return variations;

        }


    }

    class Trace {
        private Maze maze;
        private List<Point> points;

        Trace(Trace trace) {
            this.maze = trace.maze;
            this.points = new ArrayList<>(trace.points);
        }

        Trace(Maze maze, List<Point> points) {
            if (points.isEmpty()) throw new RuntimeException("no points in path");
            if (points.stream().anyMatch(p -> !maze.isOpenSpace(p.x, p.y))) throw new RuntimeException("wrong path");
            this.points = points;
            this.maze = maze;
        }

        Point getLast() {
            return points.get(points.size() - 1);
        }

        Trace add(Point point) {
            if (!contains(point)) {
                points.add(point);
            }
            return this;
        }

        boolean contains(Point point) {
            return points.contains(point);
        }

        int stepsFor(Point point) {
            return points.indexOf(point);
        }

        List<Point> allowableNextSteps() {
            Point fromPoint = getLast();
            List<Point> allowableSteps = new ArrayList<>();
            Point toPoint;
            if (fromPoint.x - 1 >= 0
                    && !points.contains(toPoint = new Point(fromPoint.x - 1, fromPoint.y)))
                allowableSteps.add(toPoint);
            if (fromPoint.x + 1 < maze.width
                    && !points.contains(toPoint = new Point(fromPoint.x + 1, fromPoint.y)))
                allowableSteps.add(toPoint);
            if (fromPoint.y - 1 >= 0
                    && !points.contains(toPoint = new Point(fromPoint.x, fromPoint.y - 1)))
                allowableSteps.add(toPoint);
            if (fromPoint.y + 1 < maze.height
                    && !points.contains(toPoint = new Point(fromPoint.x, fromPoint.y + 1)))
                allowableSteps.add(toPoint);
            allowableSteps = allowableSteps.stream()
                    .filter(p -> maze.isOpenSpace(p.x, p.y)).collect(toList());
            return allowableSteps;
        }

        @Override
        public String toString() {
            String out = "\n";
            for (int y = 0; y < maze.height; y++) {
                for (int x = 0; x < maze.width; x++) {
                    if (points.contains(new Point(x, y))) {
                        out += "O";
                    } else {
                        out += maze.isOpenSpace(x, y) ? "." : "#";
                    }

                }
                out += "\n";
            }
            return out;
        }
    }

    class Maze {
        public final int width;
        public final int height;
        boolean[][] maze;

        Maze(int width, int height, int number) {
            this.width = width;
            this.height = height;
            maze = generate(width, height, number);
        }

        private boolean[][] generate(int width, int height, int number) {
            boolean[][] maze = new boolean[height][width];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    maze[y][x] = isOpenSpaceForGeneration(x, y, number);

                }
            }
            return maze;
        }

        boolean isOpenSpace(int x, int y) {
            return maze[y][x];
        }

        boolean isOpenSpaceForGeneration(int x, int y, int number) {
            int z = x * x + 3 * x + 2 * x * y + y + y * y;
            z = z + number;
            long oneCounts = Integer.toBinaryString(z).chars().filter(e -> e == '1').count();
            return oneCounts % 2 == 0;
        }
        String showWithPoints(Collection<Point> collection){
            String out = "\n";
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (collection.contains(new Point(x, y))) {
                        out += "o";
                    } else {
                        out += isOpenSpace(x, y) ? "." : "#";
                    }

                }
                out += "\n";
            }
            return out;
        }
        @Override
        public String toString() {
            return Arrays.stream(maze).map(e -> {
                String out = "";
                for (boolean b : e) {
                    out += b ? "." : "#";
                }
                return out;
            })
                    .reduce((e1, e2) -> e1 + "\n" + e2).get();
        }
    }

    @Test
    public void generationTest() {
        String mact = new Maze(10, 7, 10).toString();
        String mexp = (".#.####.##\n" +
                "..#..#...#\n" +
                "#....##...\n" +
                "###.#.###.\n" +
                ".##..#..#.\n" +
                "..##....#.\n" +
                "#...##.###");
        assertEquals(mexp, mact);
    }

    @Test
    public void searchTest() {
        Maze m = new Maze(3, 3, 10);
        PathFinder p = new PathFinder(m);
        List<Trace> list;
        System.out.println("======== 0");
        list = p.depthSearch(new Point(1, 1), new Point(2, 2), 0);
        System.out.println(list);
        System.out.println(p.findLocationsInSteps(0).size());
        System.out.println("======== 1");
        list = p.depthSearch(new Point(1, 1), new Point(2, 2), 1);
        System.out.println(list);
        System.out.println(p.findLocationsInSteps(1).size());
        System.out.println("======== 2");
        list = p.depthSearch(new Point(1, 1), new Point(2, 2), 2);
        System.out.println(list);
        System.out.println(p.findLocationsInSteps(2).size());
        System.out.println("======== 3 ");
        list = p.depthSearch(new Point(1, 1), new Point(2, 2), 3);
        System.out.println(list);
        System.out.println(p.findLocationsInSteps(3).size());
    }

    @Test
    public void findTest() {
        Maze m = new Maze(10, 7, 10);
        PathFinder p = new PathFinder(m);
        Point target = new Point(7, 4);
        Trace path = p.findPathTo(target);
        System.out.println(m);
//        System.out.println(path);
        assertEquals(11, path.stepsFor(target));
        Set<Point> locations = p.findLocationsInSteps(11);
        System.out.println(locations.size());
        System.out.println(m.showWithPoints(locations));
    }

    @Test
    public void part1() {
        Maze m = new Maze(70, 70, 1358);
        PathFinder p = new PathFinder(m);
        Point target = new Point(31, 39);
        Trace z = p.findPathTo(target);
        System.out.println(m);
        System.out.println(z);
        System.out.println(z.stepsFor(target));
    }

    @Test
    public void part2() {
        Maze m = new Maze(70, 70, 1358);
        PathFinder p = new PathFinder(m);
        Set<Point> z = p.findLocationsInSteps(50);
        System.out.println(m.showWithPoints(z));
        System.out.println(z.size());
    }

    @Test
    public void lookLike() {
        String mact = new Maze(31, 39, 1358).toString();
        System.out.println(mact);
    }

    class Point {
        final int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Point changeX(int dx) {
            return new Point(x + dx, y);
        }

        Point changeY(int dy) {
            return new Point(x, y + dy);
        }

        int distanceTo(Point point) {
            return abs(point.x - x) + abs(point.y - y);
        }

        @Override
        public String toString() {
            return "[" + x + "," + y + "]";
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof Point)
                    && ((Point) obj).x == x
                    && ((Point) obj).y == y;
        }

        @Override
        public int hashCode() {
            return 37*x*y+x+y;
        }
    }
}

