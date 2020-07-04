import java.io.*;
import java.net.Socket;

public class ServerPath2 extends Thread{
    private Socket socket;
    private PrintWriter out;

    public ServerPath2(Socket s) throws IOException {
        socket = s;
        out = new PrintWriter(new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream())), true);
        start();
    }

    public void run(){
        String aforizm = getAforizm();
        out.println(aforizm);
    }

    public synchronized String getAforizm(){
        String randomLine = null;
        int fileSize = getFileSize("Aforizms.txt");
        int random = 1+(int)(Math.random()*fileSize);
        System.out.println("random:" + random);
        InputStream in = this.getClass().getResourceAsStream("Aforizms.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        for(int i = 0; i < random; ++i) {
            try {
                reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            randomLine = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return randomLine;
    }

    public synchronized int getFileSize(String name){
        File file = getResourceAsFile(name);
        int lineCount = 0;
        try {
            FileReader fileReader = new FileReader(file);
            LineNumberReader lnr = new LineNumberReader(fileReader);
            while(null!=lnr.readLine()) {
                lineCount++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("LineCount: " + lineCount);
        return lineCount;
    }

    public File getResourceAsFile(String name) {
        try {
            InputStream inStream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream(name);
            if (inStream == null) {
                return null;
            }
            File tempFile = File.createTempFile(String.valueOf(inStream.hashCode()), ".tmp");
            tempFile.deleteOnExit();

            try (FileOutputStream outStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}