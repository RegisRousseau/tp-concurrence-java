package exo3;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket server = new ServerSocket(8081);
        ExecutorService serverWorkers = Executors.newFixedThreadPool(3); //Nb de client max : 3
        
        
        try {
	        while (true) {
	        	System.out.println("Listening to request");
	            Socket socket = server.accept();
	            
	            Runnable servirClient = () -> {
	            	try {
	    				serviceClient(socket);
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	            };
	            System.out.println("Accepting request");
	            serverWorkers.submit(servirClient);
	        }
        }finally {
        	serverWorkers.shutdownNow(); //Si erreur dans le while(true), bien fermer les autres threads !
        	server.close();
        }
    }
    
    
    public static void serviceClient(Socket socket) throws IOException {
    	

        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        OutputStream outputStream = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));

        String order = reader.readLine();
        while(order != null) {
        	if(order.startsWith("GET")) {
        		writer.printf("Received GET order : %s\n", order);
        		writer.flush();
        		System.out.printf("Received GET order : %s\n", order);
        	} else if(order.startsWith("bye")) {
        		writer.printf("Closing connection\n");
        		socket.close();
        	} 
        	try {
        		order = reader.readLine();
        	}catch (SocketException e) {
        		order = null;
        		System.out.printf("Client is gone\n");
        	}
        }
    }
}


