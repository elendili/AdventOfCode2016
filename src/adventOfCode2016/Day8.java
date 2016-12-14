package adventOfCode2016;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;


public class Day8 {

    public static void main(String[] args) {
        if (args.length > 0) {
            String filepath = args[0];
            try {
                String result = new Day8().processFirstWay(Files.readAllLines(Paths.get(filepath)));
                System.out.println(result);
                String result2 = new Day8().processSecondWay(Files.readAllLines(Paths.get(filepath)));
                System.out.println(result2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no args");
        }

    }

    public String processFirstWay(List<String> lines) {
        Screen screen= new Screen();
        lines.stream().forEach(screen::exec);
        return ""+screen.sum();
    }

    public String processSecondWay(List<String> lines) {
        Screen screen= new Screen();
        lines.stream().forEach(screen::exec);
        return ""+screen;
    }


    @Test
    public void test1() {
//        System.out.println(new Screen());
//        System.out.println(new Screen().rectAxB(50,6));
        System.out.println(new Screen(7,3).exec("rect 3x2"));
        System.out.println(new Screen(7,3).exec("rect 3x2")
                .exec("rotate column x=1 by 1"));
        System.out.println(new Screen(7,3).exec("rect 3x2")
                .exec("rotate column x=1 by 1").exec("rotate row y=0 by 4"));
        Screen x;
        System.out.println(x= new Screen(7,3).exec("rect 3x2   ")
                .exec("rotate column x=1 by 1").exec("rotate row y=0 by 4").exec("rotate column x=1 by 1"));
        System.out.println(x.sum());
//        System.out.println(new Screen().rectAxB(2,2).rotateCol(0,5));
    }

    class Screen {
        final int width;
        final int height;
        final int[][] pixels;
        Screen(){
            this(50,6);
        }
        Screen(int width,int height){
            this.width=width;
            this.height=height;
            pixels = new int[height][width];
        }
        Screen exec(String line){
            List<Integer> arg = Arrays.asList(line.split("\\D+"))
                    .stream().filter(e->!e.isEmpty())
                    .map(Integer::valueOf).collect(toList());
            if (line.contains("row")) rotateRow(arg.get(0),arg.get(1));
            else if (line.contains("column")) rotateCol(arg.get(0),arg.get(1));
            else if (line.contains("rect")) rectAxB(arg.get(0),arg.get(1));
            else throw new IllegalArgumentException(line);
            return this;
        }
        public Screen rectAxB(int wide, int height) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < wide; j++) {
                    pixels[i][j] = 1;
                }
            }
            return this;
        }
        public int sum() {
            int sum=0;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    sum+=pixels[i][j];
                }
            }
            return sum;
        }

        public Screen rotateRow(int rowNumber, int shift){
            int[] _copyOfRow = Arrays.copyOf(pixels[rowNumber],width);
            for (int i=0;i<width;i++){
                    pixels[rowNumber][(i+shift)%width] = _copyOfRow[i];
            }
            return this;
        }

        public Screen rotateCol(int colNumber, int shift){
            int[] _copyOfCol =new int[height];
            for (int i=0;i<height;i++){ _copyOfCol[i] = pixels[i][colNumber];}
            for (int i=0;i<height;i++){
                pixels[(i+shift)%height][colNumber]= _copyOfCol[i];
            }
            return this;
        }

        @Override
        public String toString() {
            String out="";
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (pixels[i][j] == 1) out+="#";
                    else out+=" ";
                }
                out+=System.lineSeparator();
            }
            return out;
        }
    }
}

