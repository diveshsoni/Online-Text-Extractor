import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ServerClass {

	public static void main(String[] args) throws IOException {
		ServerClass server = new ServerClass();
		server.getUrlContent();
		server.setUpServer();
	}

	private void getUrlContent() {
//      Reading the html file. 
		String line = "";
		StringBuffer fileContent = new StringBuffer("");
		Pattern htmlTags = Pattern.compile("\\<.*?\\>");
		Pattern linebreak = Pattern.compile("\\<br\\>");
		try {
			URL NFL = new URL("http://sagarin.com/sports/nflsend.htm");
			BufferedReader fileIn = new BufferedReader(new InputStreamReader(NFL.openStream()));
			while((line = fileIn.readLine()) != null){
				line += "\n";
				fileContent.append(line);
//				System.out.println(line);
			}
//			System.out.println(fileContent);
			fileIn.close();
//			Eliminating html tags from the source file.
			String htmlFileContent = fileContent.toString();
			htmlFileContent = htmlFileContent.replaceAll(linebreak.pattern(), "\n");
			htmlFileContent = htmlFileContent.replaceAll(htmlTags.pattern(), "");
			htmlFileContent = htmlFileContent.replaceAll("&nbsp;", " ");
			htmlFileContent = htmlFileContent.replaceAll("null", "\n");
			htmlFileContent = htmlFileContent.replaceAll("$","\r\n");
			BufferedWriter fileOut = new BufferedWriter(new FileWriter("NFL File.txt"));
//			System.out.println(htmlFileContent);
			fileOut.write(htmlFileContent);
			System.out.println("'NFL File.txt' created, eliminating html tags.");
			fileOut.close();
//			Extracting the required text
			fileIn = new BufferedReader(new FileReader("NFL File.txt"));
			line = "";
			fileContent = new StringBuffer("");
			while((line = fileIn.readLine()) != null){
				fileContent.append(line);
			}
			fileIn.close();
			htmlFileContent = "";
			htmlFileContent = fileContent.toString();
			String startText = "By Division";
			String endText = "endfile";
			Pattern patternStart = Pattern.compile(startText);
			Pattern patternEnd = Pattern.compile(endText);

			Matcher matchStart = patternStart.matcher(htmlFileContent);
			Matcher matchEnd = patternEnd.matcher(htmlFileContent);
			
			fileOut = new BufferedWriter(new FileWriter("NFL Standings.txt"));
			if(matchStart.find() && matchEnd.find()){
				fileOut.write(htmlFileContent.substring(matchStart.start(), matchEnd.end()));
				System.out.println("'NFL Standings.txt' created.");
			}
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    private void setUpServer() throws IOException {
    	int port = 4020;
		ServerSocket serverSocket = new ServerSocket(port);
		Socket clientSocket = serverSocket.accept();
		String choice = "";
		BufferedReader serverIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		PrintWriter serverOut = new PrintWriter(clientSocket.getOutputStream());
		
		do{
			serverOut.println("Enter a team name to retrieve corresponding team information:\n"
					+ "(Press 'Q' to quit)");
			choice = serverIn.readLine();
			BufferedReader teamInfo = new BufferedReader(new FileReader("NFL Standings.txt"));
			String line = "";
			boolean teamValid = false;
			while((line = teamInfo.readLine()) != null){
				if(line.contains(choice)){
					serverOut.println(line);
					teamValid  = true;
					break;
				}
			}
			teamInfo.close();
			if(!teamValid){
				serverOut.println("Invalid team name!\nTry again.");
			}
		}while(choice != "Q" || choice != "q");
		serverIn.close();
		serverOut.close();
		clientSocket.close();
		serverSocket.close();
	}
}
