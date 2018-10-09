package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

  private  Socket socket;
  private  ObjectOutputStream output;
  private ObjectInputStream input;

  public Client(){
      try {
          socket = new Socket("localhost",1234);
      } catch (IOException e) {
          e.printStackTrace();
      }
  }

    public ObjectOutputStream getOutput() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public ObjectInputStream getInput() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    public Socket getSocket() {
        return socket;
    }
}
