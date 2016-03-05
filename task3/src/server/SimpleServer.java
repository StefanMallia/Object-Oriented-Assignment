package server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;


public class SimpleServer{
	private ServerSocket serverSocket = null;
	private String webDirectory;
	
	public SimpleServer(int port, String directory) throws IOException {	
		serverSocket = new ServerSocket(port);
		webDirectory = directory;
	}

	
	public void waitConnection() throws IOException{
		//waits for connection. if accepted, launches new thread that handles request.
		while (true) {
			System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
			Socket clientSocket = serverSocket.accept();
			System.out.println("Connected to " + clientSocket.getRemoteSocketAddress() + '\n');
			new Thread(new ServerRunnable(webDirectory, clientSocket, serverSocket)).start();
		}
	
	}

}
