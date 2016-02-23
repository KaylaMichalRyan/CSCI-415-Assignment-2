
import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;;

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
		
		try(ServerSocket listener = new ServerSocket(portNumber);){
			Runtime.getRuntime().addShutdownHook(new Thread(){public void run(){
			    try {
			        listener.close();
			        System.out.println("The server is shut down!");
			    } catch (IOException e) { /* failed */ }
			}});
			while(true){
				if(threads.size() <= maxThreads){
					Socket connection = listener.accept();
					ProxyThread thread = new ProxyThread(connection, loggingEnabled, loggingLocation);
					thread.start();
					threads.push(thread);
				}else{
					for(Iterator<ProxyThread> iterator = threads.iterator(); iterator.hasNext();){
						ProxyThread thread = iterator.next();
						if (!thread.isAlive()){
							threads.remove(thread);
						}
					}
				}if(threads.size() <= maxThreads){
					Socket connection = listener.accept();
					ProxyThread thread = new ProxyThread(connection, loggingEnabled, loggingLocation);
					thread.start();
					threads.push(thread);
				}
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		
	}

}
