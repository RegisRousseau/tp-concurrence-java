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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ServerMagasin {

    public static void main(String[] args) throws IOException {
//    1) Premier arrivé, premier servis. Les autres clients ne pourront pas accéder au serveur. Il faudrait que le serveur mette dans un nouveau thread à chaque fois qu'un client arrivera.
//    2) Toute la partie d'initialisation du reader et la boucle qui suit devront être mis dans un thread.(ligne 18 pour le programme initial)
//    3) On peut créer un ExecutorService avec le nombre de client que l'on souhaite.

    	//Variables pour le serveur :
        ServerSocket server = new ServerSocket(8081);
        ExecutorService serverWorkers = Executors.newFixedThreadPool(3); 	//Nb de client max : 3
        Lock lock = new ReentrantLock(); 									//lock pour bloquer l'accès à la liste
        Map<String, Integer> map = new ConcurrentHashMap<String,Integer>(); //Liste du magasin
        map.put("Default",0); //Contenu du magasin initial
        
        try {
	        while (true) {
	        	System.out.println("Listening to request");
	            Socket socket = server.accept();
	            
	            Runnable servirClient = () -> {
	            	try {
	    				serviceClient(socket, map,lock);
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
    
    
    
public static void serviceClient(Socket socket, Map<String, Integer> map, Lock lock) throws IOException {

        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        OutputStream outputStream = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));

        String order = reader.readLine();
		writer.printf("Hello to the thread named %s",Thread.currentThread().getName());

        while(order != null) {
        	if(order.startsWith("GET")) {
        		writer.printf("Received GET order : %s\n", order);
        		writer.flush();
        		System.out.printf("Received GET order : %s\n", order);
        		
        	} else if (order.startsWith("LIST")) {
            	//LISTAGE :
            	writer.printf("Received LIST order : \n");
            	writer.print("" + map);
                writer.flush();
                
            } else if (order.startsWith("BUY")) {
            	//ACHAT :
            	String values[]  = order.split(" ");
            	writer.printf("Received BUY order : %s\n", values[1]);
            	map.remove(values[1]);
                writer.flush();
                
            } else if(order.startsWith("quit")) {
        		writer.printf("Closing connection\n");
        		writer.printf("Goodbye to the thread named %s",Thread.currentThread().getName());
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