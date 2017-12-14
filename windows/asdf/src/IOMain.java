import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;


public class IOMain {

    static List<Point> points;

    public static void main(String[] args) {
        points = new ArrayList<>();
        readPoints(args[0]);


        filterPoints();
        writePoints();
    }


    public static void readPoints(String filename) {
        try {
            Scanner sc = new Scanner(new File(filename));
            while (sc.hasNext()) {
                String line = sc.nextLine();
                String[] arr = line.split(", ");
                points.add(new Point(Double.parseDouble(arr[0]), Double.parseDouble(arr[1]), Double.parseDouble(arr[2])));
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Error occurred while trying to read file");
            e.printStackTrace();
        }
    }

    public static void writePoints() {
        try {
            PrintWriter writer = new PrintWriter("drawMe.txt");
            for (int i = 0; i < points.size() - 1; i++) {
                Point p = points.get(i);
                writer.println(p.getX() + ", " + p.getY() + ", " + (int) (p.getZ()));
            }
            writer.print(points.get(points.size() - 1).getX() + ", " + points.get(points.size() - 1).getY() + ", " + (int) (points.get(points.size() - 1).getZ()));
            writer.close();
        } catch (Exception e) {
            System.out.println("Error occurred while trying to write to file");
            e.printStackTrace();
        }
    }

    public static void filterPoints() {
        points = points
                .stream()
                .filter(p -> p.getZ() <= 2.0)
                .map(p -> new Point(p.getX() * 0.5, p.getY() * 0.5, p.getZ() * 0.5))
                .map(p -> new Point(p.getX() - 150, p.getY() - 37, p.getZ()))
                .collect(Collectors.toList());
    }
}
