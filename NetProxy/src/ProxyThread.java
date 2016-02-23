
import java.net.*;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Dictionary;

public class ProxyThread extends Thread{
    	Socket connection;
	boolean loggingEnabled;
	String loggingLocation;
	private static final int BUFFER_SIZE = 32768;
        static Hashtable<String,String> cache = new Hashtable<String,String>();
        static String[][] urlArray = new String[10][2];
        
        
	boolean clientConnected;
	ProxyThread(Socket connection, boolean loggingEnabled, String loggingLocation){
                super("ProxyThread");
		this.connection = connection;
		this.loggingEnabled = loggingEnabled;
		this.loggingLocation = loggingLocation;
		clientConnected = true;
	}
        @Override
        public void run() {
            Logger logger = Logger.getLogger("MyLog"); 
            FileHandler fh;
            
            try{
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String inputLine, outputLine;
                int cnt = 0;
                int fileCount = 0;
                String urlToCall = "";
                
                fh = new FileHandler("C:/Users/Joe/Desktop/Proxy.log");
                logger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);  
                
                for(int i = 0;i < 10;i++){
                    if (urlArray[i][1] == null){
                        fileCount = i;
                        break;
                    }
                }
                
                while((inputLine = in.readLine()) != null){
                    try{
                        StringTokenizer tok = new StringTokenizer(inputLine);
                        tok.nextToken();
                    } catch(Exception e) {
                        break;
                    }

                    if(cnt == 0){
                        String[] tokens = inputLine.split(" ");
                        urlToCall = tokens[1];
                        System.out.println("Request for : " + urlToCall);
                    }

                    cnt++;
                }        
                
                BufferedReader rd = null;
                try{
                    System.out.println("sending request to real server for url: " + urlToCall);

                    URL url = new URL(urlToCall);
                    URLConnection conn = url.openConnection();
                    URLConnection conn2 = url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(false);
                    conn.setUseCaches(true);
                    String host = url.getHost();
                    InetAddress address = InetAddress.getByName(host);
                    String ip = address.getHostAddress();
                    System.out.println(ip);
                    System.out.println("Type is: " + conn.getContentType());
                    System.out.println("Content length: " + conn.getContentLength());
                    System.out.println("Allowed user interaction: " + conn.getAllowUserInteraction());
                    System.out.println("Content encoding: " + conn.getContentEncoding());
                    System.out.println("Content type: " + conn.getContentType());
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = new Date();
                    logger.info(dateFormat.format(date)+ " " + ip + " " + urlToCall + " " + conn.getContentLength());

                    boolean cached = false;
                    
                    if(conn.getContentLength() > 0){
                    
                    for (int i = 0;i < 10;i++)
                    {
                        if(urlArray[i][0] != null && urlArray[i][0].equals(urlToCall)){
                            
                            System.out.println("THIS IS BEING ACCESSED FROM CACHE:");
                            System.out.println("");
                            System.out.println("---------------------------------");
                            System.out.println("\n\n");
                            String file = urlArray[i][1];
                            FileInputStream fstream = new FileInputStream(file);
                            DataInputStream input = new DataInputStream(fstream);
                            BufferedReader br = new BufferedReader(new InputStreamReader(input));                       
                            String fileInput;
                            while((fileInput = br.readLine()) != null){
                                out.writeChars(fileInput + "\n");
                            }
                            cached = true;
                        }
                    }
                        while(cached == false){
                            
                            InputStream is = null;
                            PrintWriter fileout = null;
                                              
                                System.out.println("WRITING TO FILE \n\n");
                                is = conn.getInputStream();
                                rd = new BufferedReader(new InputStreamReader(is));                            
                                String line;
                                String fileName = "File" + fileCount + ".txt";
                                fileout = new PrintWriter(fileName);
                                while ((line = rd.readLine()) !=null){
                                    fileout.println(line);
                                    if (line.equals("</HTML>")){
                                        fileout.close();
                                    break;
                                    }
                                }
                                urlArray[fileCount][1] = fileName;
                                urlArray[fileCount][0] = urlToCall;
                            
                            
                            InputStream is2 = conn2.getInputStream();
                            byte by[] = new byte[BUFFER_SIZE];
                            int index = is2.read(by, 0, BUFFER_SIZE);
                            while(index != -1){
                                out.write(by, 0, index);
                                System.out.println(index);
                                index = is2.read(by, 0, BUFFER_SIZE);
                            }
                            
                          
                        
                            
                        }
                }
                    out.flush();
                } catch (Exception e) {
                    System.err.println("Encountered exception: " + e);
                    out.writeBytes("");
                }

                if(rd != null){
                    rd.close();
                }
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
                if(connection != null){
                    connection.close();
                }
                

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}

