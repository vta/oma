import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.List;

public class BabyGames {

    public static void main(String[] args) {
        Integer i = 3;

        int sum = i + 2;
        ArrayList<String> list = new ArrayList<>();
        list.add("asdfsadf");

        System.out.println(list);
        System.out.println(i);
        c(list);
        System.out.println(list);

        a(i);
        System.out.println(i);
        System.out.print("this");
    }

    public static void printHi() {
    }

    public static void a(int i) {
        i = 7;
    }

    public static void b(Integer i) {
        i = 7;
    }

    public static void c(ArrayList a) {
        a = new ArrayList();
    }
}
