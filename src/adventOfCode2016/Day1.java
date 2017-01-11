package adventOfCode2016;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static adventOfCode2016.Direction.NORTH;
import static java.lang.Math.abs;
import static org.junit.Assert.assertEquals;


public class Day1 {

    public static void main(String[] args) {
        if (args.length > 0) {
            String filepath = args[0];
            try {
                String line = Files.readAllLines(Paths.get(filepath)).get(0);
                int result = new Day1().process(Arrays.asList(line.split(",\\s*"))).distanceFromZero();
                System.out.println(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Point process(List<String> steps) {
        Tripper tripper = new Tripper();

        for (String s : steps) {
            String turn = s.replaceAll("\\d+", "");
            tripper.turn(turn);
            int distance = Integer.parseInt(s.replaceAll("\\D+", ""));
            tripper.move(distance);
        }

        return tripper.trace.point;
    }

    @Test
    public void test() {
        String line = "R2, L3";
        assertEquals(5, new Day1().process(Arrays.asList(line.split(",\\s*"))).distanceFromZero());
        line = "R2, R2, R2";
        assertEquals(2, new Day1().process(Arrays.asList(line.split(",\\s*"))).distanceFromZero());
        line = "R5, L5, R5, R3";
        assertEquals(12, new Day1().process(Arrays.asList(line.split(",\\s*"))).distanceFromZero());
        line = "R8, R4, R4, R8";
        assertEquals(8, new Day1().process(Arrays.asList(line.split(",\\s*"))).distanceFromZero());
    }
}

enum Direction {
    NORTH, EAST, SOUTH, WEST;

    Direction right() {
        return values()[(ordinal() + 1) % values().length];
    }

    Direction left() {
        return values()[(ordinal() == 0 ? 3 : ordinal() - 1)];
    }
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

    int distanceFromZero() {
        return abs(x) + abs(y);
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
}

class Trace {
    protected Point point = new Point(0, 0);
    List<Point> trace = new ArrayList<Point>() {{
        add(point);
    }};

    void moveY(int dy) {
        Point _point = point;
        int sign = (0 > dy) ? -1 : 1;
        for (int v = point.y + sign; v != point.y + dy + sign; v = v + sign) {
            stepAndCheck(_point = new Point(point.x, v));
        }
        this.point = _point;
    }

    void moveX(int dx) {
        Point _point = point;
        int sign = (0 > dx) ? -1 : 1;
        for (int v = point.x + sign; v != point.x + dx + sign; v = v + sign) {
            stepAndCheck(_point = new Point(v, point.y));
        }
        this.point = _point;
    }

    void stepAndCheck(Point point) {
        if (trace.contains(point)) {
            System.out.println("wow! I was in " + point + ", distance: " + point.distanceFromZero());
        }
        trace.add(point);
    }
}

class Tripper {
    Trace trace = new Trace();
    Direction direction = NORTH;


    public void turn(String turn) {
        switch (turn) {
            case "L":
                direction = direction.left();
                break;
            case "R":
                direction = direction.right();
                break;
            default:
                System.out.println("Step direction mistake");
        }
    }

    public void move(int distance) {
        switch (direction) {
            case NORTH:
                trace.moveY(+distance);
                break;
            case EAST:
                trace.moveX(+distance);
                break;
            case SOUTH:
                trace.moveY(-distance);
                break;
            case WEST:
                trace.moveX(-distance);
                break;
        }
    }

}
