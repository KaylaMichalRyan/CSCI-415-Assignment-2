import java.io.File;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Interpret command line flags here
		int portNumber = 8080;
		int maxThreads = 10;
		boolean loggingEnabled = true;
		String loggingLocation = "";
		for(int i = 0; i <  args.length; i++){
			switch(args[i]){
			case "--port-number": 
				try{portNumber = Integer.parseInt(args[++i]);}
				catch(Exception e){
					System.out.println("malformed argument --port-number " + args[i]);
					System.exit(0);
				}
				break;
			case "--disable-logging": 
				loggingEnabled = false;
				break;
			case "--logging-location": 
				try{
					File f = new File(args[++i]);
					if(!f.exists()){
						System.out.println("Could not find directory: " + args[i]);
						System.exit(0);
					}
				}catch(Exception e){
					System.out.println("malformed argument --logging-locationr " + args[i]);
					System.exit(0);
				}
				break;
			case "--max-threads": 
				try{
					maxThreads = Integer.parseInt(args[++i]);
				}catch(Exception e){
					System.out.println("malformed argument --max-threads " + args[i]);
					System.exit(0);
				}
				break;
			}
		}
		
		
		//Launch the threadManager which will handle proxy threads and new connections
		
		ThreadManager manager = new ThreadManager(portNumber, maxThreads, loggingEnabled, loggingLocation);
		manager.run();

	}

}
