package srvs;

import javafx.scene.control.TextArea;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class AphorismModule extends IOconnection {
    private static String fileName = "wisdom.txt";
    private static int numberOfLines;

    public AphorismModule(TextArea taLog){
        super(taLog);
        try {
            numberOfLines = numberOfLines();
        } catch (FileNotFoundException ex) {
            doLog("File :\"" + fileName + "\" not found\n");
        }
    }

    @Override
    public void requestProcessing() {
        String input;
        String aphorism = "";
        try {
            while ((input = in.readLine()) != null) {
                if (input.equalsIgnoreCase("exit")){
                    doLog("[Client] : " + input);
                    out.println(input);
                    break;
                }
                if (input.equalsIgnoreCase("get")) {
                    try {
                        aphorism = getRandomAphorism();
                    } catch (IOException ex) {
                        doLog("[Server] : File :\"" + fileName + "\" not found");
                    }
                    out.println(aphorism);
                    doLog("[Client] : " + input);
                    doLog("[Server] : " + aphorism);
                    continue;
                }
                out.println("[Server] : " + input);
                doLog("[Server] : " + input);
            }
        } catch (IOException ex) {
            doLog("[Server] : Input / Output failed");
        }
    }

    private static int numberOfLines() throws FileNotFoundException {
        try {
            try (BufferedReader is = new BufferedReader(new InputStreamReader(AphorismModule.class.getClassLoader().getResourceAsStream(fileName), StandardCharsets.UTF_8))) {
                boolean empty = true;
                int count = 0;
                while (is.ready()) {
                    empty = false;
                    is.readLine();
                    count++;
                }
                return (empty) ? 0 : count;
//                byte[] c = new byte[1024];
//                int count = 0;
//                int readChars = 0;
//                boolean empty = true;
//                while ((readChars = is.read(c)) != -1) {
//                    empty = false;
//                    for (int i = 0; i < readChars; ++i) {
//                        if (c[i] == '\n') {
//                            ++count;
//                        }
//                    }
//                }
//                return (count == 0 && !empty) ? 1 : count;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private static String getRandomAphorism() throws IOException{
        BufferedReader bf = new BufferedReader(new InputStreamReader(AphorismModule.class.getClassLoader().getResourceAsStream(fileName), StandardCharsets.UTF_8));
        for (int i = 0; i < new Random().nextInt(numberOfLines - 1); i++) {
            bf.readLine();
        }
        String aphorism = bf.readLine();
        bf.close();
        return aphorism;
    }
}
