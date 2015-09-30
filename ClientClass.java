import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientClass {
	
	public static void main(String[] args) {
		ClientClass client = new ClientClass();
		client.setUpClient();
	}

	private void setUpClient() {
		InetAddress hostName = null;
		try {
			hostName = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
		int port = 4020;
		try {
			Socket clientSocket = new Socket(hostName, port);
			PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//			String message = clientIn.readLine();
			BufferedReader withinClient = new BufferedReader(new InputStreamReader(System.in));
			String teamName = withinClient.readLine();
			clientOut.println(teamName);
			clientSocket.close();
			clientOut.close();
			clientIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
