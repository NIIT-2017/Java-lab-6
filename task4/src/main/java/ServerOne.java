import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;


class ServerOne extends Thread {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Elements Valuet;
    private Document doc;


    // Информация с сайта центрального банка
    private void Getinformation() {
        try {
            doc = Jsoup.connect("https://www.cbr.ru/currency_base/daily/").get();
            Elements table = doc.getElementsByTag("tbody");
            Element ourtable = table.get(0);
            Valuet = ourtable.children();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ServerOne(Socket s) throws IOException {
        socket = s;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run() {
        try {
            while (true) {
                String str = in.readLine();
                if (str.equals("END"))
                    break;
                if (str.equalsIgnoreCase("valuet")){
                    Getinformation();
                    for (Element el: Valuet){
                        out.println(el.text());
                        out.println("\n");
                    }
                }
                System.out.println("Получено: " + str);
                out.println(str);
            }
            System.out.println("Соединение закрыто");
        }
        catch (IOException e) {
            System.err.println("Ошибка чтения/записи");
        }
        finally {
            try {
                socket.close();
            }
            catch (IOException e) {
                System.err.println("Сокет не закрыт");
            }
        }
    }
}