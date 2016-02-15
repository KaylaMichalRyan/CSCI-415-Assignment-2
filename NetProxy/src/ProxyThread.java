import java.net.Socket;
import java.io.IOException;

public class ProxyThread extends Thread{
	Socket connection;
	boolean loggingEnabled;
	String loggingLocation;
	
	boolean clientConnected;
	ProxyThread(Socket connection, boolean loggingEnabled, String loggingLocation){
		this.connection = connection;
		this.loggingEnabled = loggingEnabled;
		this.loggingLocation = loggingLocation;
		clientConnected = true;
	}

}
