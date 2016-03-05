import server.*;
import java.io.*;
public class Launcher {
	public static void main(String[] args) {
		try {
			//first argument is the web directory location
			//second argument is the port number
			int portNum = Integer.parseInt(args[1]);
			SimpleServer simpleServer = new SimpleServer(portNum, args[0]);
			simpleServer.waitConnection();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
}
