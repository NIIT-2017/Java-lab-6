import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

public class Conventer {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        Double amount;
        System.out.println("What is your currency ?");
        String from = scanner.next();

        System.out.println("Convert " + from + " to");
        String to = scanner.next();

        System.out.println("Amount");
        amount = scanner.nextDouble();

        java.net.URL url = new java.net.URL(
                "https://api.fixer.io/latest?base=" + from + "&symbols=" + to);
        java.util.Scanner sc = new java.util.Scanner(url.openStream());
        String str = sc.nextLine().replaceAll("[^0-9.]", "");
        Double rate = Double.parseDouble(str.substring(8));
        sc.close();

        amount = amount * rate;
        System.out.println(amount);

    }
}
