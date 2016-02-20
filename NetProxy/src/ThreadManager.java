
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.util.LinkedList;

public class ThreadManager {
	int portNumber;
	int maxThreads;
	boolean loggingEnabled;
	String loggingLocation;
	public ThreadManager(int portNumber, int maxThreads, boolean loggingEnabled, String loggingLocation){
		this.portNumber = portNumber;
		this.maxThreads = maxThreads;
		this.loggingEnabled = loggingEnabled;
		this.loggingLocation = loggingLocation;
	}
	public void run(){
		System.out.println("NetProxy launched");
		LinkedList<ProxyThread> threads = new LinkedList<ProxyThread>();
		int connections = 0;
		
		try(ServerSocket listener = new ServerSocket(portNumber);){
			while(true){
				if(threads.size() <= maxThreads){
					Socket connection = listener.accept();
					ProxyThread thread = new ProxyThread(connection, loggingEnabled, loggingLocation);
					thread.start();
					threads.push(thread);
				}else{
					for(ProxyThread thread : threads){
						if(!thread.isAlive()){
							threads.remove(thread);
						}
					}
				}
			}
		}catch(IOException e){
			System.out.println("connection error");
			System.exit(0);
		}
		
		
	}

}
