package exo3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 8081);

        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        OutputStream outputStream = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));

        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();

        while (!"bye".equals(command)) {

            System.out.println("Sending command = " + command);
            writer.println(command);
            writer.flush();

            String answer = reader.readLine();
            System.out.println("Got answer = " + answer);

            System.out.print("> ");
            command = scanner.nextLine();
        }
        writer.println(command);
        writer.flush();
    }
}