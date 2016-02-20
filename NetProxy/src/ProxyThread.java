//Source: http://www.jtmelton.com/2007/11/27/a-simple-multi-threaded-java-http-proxy-server/


import java.net.*;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ProxyThread extends Thread{
    Socket connection;
	boolean loggingEnabled;
	String loggingLocation;
	private static final int BUFFER_SIZE = 32768;
        
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
                String urlToCall = "";
                
                //fh = new FileHandler("C:/Users/Joe/Desktop/Proxy.log");
                //logger.addHandler(fh);
                //SimpleFormatter formatter = new SimpleFormatter();
                //fh.setFormatter(formatter);  
                
                
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
                    conn.setDoInput(true);
                    conn.setDoOutput(false);
                    System.out.println("Type is: " + conn.getContentType());
                    System.out.println("Content length: " + conn.getContentLength());
                    System.out.println("Allowed user interaction: " + conn.getAllowUserInteraction());
                    System.out.println("Content encoding: " + conn.getContentEncoding());
                    System.out.println("Content type: " + conn.getContentType());
                    //IP address of browser???
                    //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    //Date date = new Date();
                    //logger.info(dateFormat.format(date)+" " + urlToCall + " " + conn.getContentLength());
                    InputStream is = null;
                    HttpURLConnection huc = (HttpURLConnection)conn;
                    if(conn.getContentLength() > 0){
                        try{
                            is = conn.getInputStream();
                            rd = new BufferedReader(new InputStreamReader(is));
                        } catch (IOException ioe) {
                            System.out.println("IO EXCEPTION: " + ioe);
                        }
                    }

                    byte by[] = new byte[BUFFER_SIZE];
                    int index = is.read(by, 0, BUFFER_SIZE);
                    while(index != -1){
                        out.write(by, 0, index);
                        index = is.read(by, 0, BUFFER_SIZE);
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