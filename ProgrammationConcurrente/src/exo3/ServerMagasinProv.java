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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMagasinProv {

    public static void main(String[] args) throws IOException {
//    1)Premier arrivé, premier servis. Les autres clients ne pourront pas accéder au serveur. Il faudrait que le serveur mette dans un nouveau thread à chaque fois qu'un client arrivera.
//    2) Toute la partie d'initialisation du reader et la boucle qui suit devront être mis dans un thread.(ligne 18 pour le programme initial)
//    3) On peut créer un ExecutorService avec le nombre de client que l'on souhaite.

        ServerSocket server = new ServerSocket(8081);
        ExecutorService executor = Executors.newFixedThreadPool(3); //Nb de client max : 3
        
        Map<String, Integer> map = new ConcurrentHashMap<String,Integer>();
        map.put("Default",0); //Contenu du magasin initial
        
        
        while (true) {
            System.out.println("Listening to request");
            Socket socket = server.accept();
            System.out.println("Accepting request");

            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));

            //Debut ici :
            
            Runnable addclient = () -> {
            	try {
		            String order = reader.readLine();
		            while (order != null) {
		                if (order.startsWith("PUT")) {
		                	// ENREGISTREMENT :
		                	String values[]  = order.split(" ");
		                	map.put(values[1],Integer.parseInt(values[2]));
		                    writer.printf("Received PUT order : %s\n", order);
		                    writer.flush();
		                    System.out.printf("Received PUT order : %s\n", order);
		                }else if (order.startsWith("LIST")) {
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
		                }else if (order.equals("quit")) {
		                    System.out.printf("Closing connection\n");
		                    socket.close();
		                }
		                order = reader.readLine();
		            }
            	} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            };     
            executor.submit(addclient);
        }
    }
}