package Task3.conection;
//interface for event system
public interface MyConnectionListener {
    //when connection is up
    void onConnect(MyConnection connection);
    //when message received
    void onReceive(MyConnection connection,String message );
    //when connection is down
    void onDisconnect(MyConnection connection);
    //when throw exception
    void onException(MyConnection connection, Exception e);
}
