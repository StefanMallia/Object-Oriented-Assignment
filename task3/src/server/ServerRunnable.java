package server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

class ServerRunnable implements Runnable{
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private	DataOutputStream outStream = null;
	private	BufferedReader inStream = null;
	private String webDirectory;
	private	Map<Integer, String> statusDictionary = new HashMap<Integer, String>() {
		{
			put(200, " OK");
			put(404, " Not Found");
			put(405, " Method Not Allowed");
			put(415, " Unsupported Media Type");
			put(500, " Internal Server Error");
		}
	};
	public ServerRunnable(String directory, Socket clientSocketArg, ServerSocket serverSocketArg) {
		webDirectory = directory;
		clientSocket = clientSocketArg;
		serverSocket = serverSocketArg;
		HomePage.update(directory+"/Content", Integer.toString(serverSocket.getLocalPort()));


	}

	private void sendHeaderResponse(int statusCode, String contentType) throws IOException{
		String statusCodeString = String.valueOf(statusCode) + statusDictionary.get(statusCode);
		outStream.writeBytes("HTTP/1.1 " + statusCodeString + "\r\n");
		outStream.writeBytes("Content-type: text/" + contentType + "; charset=UTF-8\r\n");
		outStream.writeBytes("Date:" + GetDate.getDate() + "\r\n");
		outStream.writeBytes("Server: Simple Java Server\r\n\r\n");			
	}
	

	private boolean fileExists(String fileName) {
		File f = new File(webDirectory + "/Content/" + fileName);
		if (f.exists() && !f.isDirectory())
			return true;
		else
			return false;
	}
	

	private void sendFile(String fileName, String contentOrError) throws IOException{
		byte[] buffer = new byte[1024];
		int bytesRead;
		FileInputStream fileIn = new FileInputStream(webDirectory + "/" + contentOrError + "/" + fileName);
		while ((bytesRead = fileIn.read(buffer)) > 0) {
			outStream.write(buffer, 0, bytesRead);	
		}
		fileIn.close();		
	}
	
	
	public void run() {
		try {
			outStream = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
			String headerLine = inStream.readLine();
			System.out.println(headerLine);

	/*
			while(inStream.ready()) {
				System.out.println(inStream.readLine());
			}
	*/				
			String fileName = ServerParser.parseHeaderFileRequest(headerLine);
			if (FunctionalityChecker.isMethodAllowed(ServerParser.parseHeaderRequest(headerLine))) {
				if (fileName.equals("")) {
					sendHeaderResponse(200, "html");
					sendFile("homepage.html", "Content");
				}					
				else if (FunctionalityChecker.isFileAllowed(fileName)) {
					if (fileExists(fileName)) {
						sendHeaderResponse(200, ServerParser.contentType(fileName));
						sendFile(fileName, "Content");
					}
					else {
						System.out.println(fileName);
						sendHeaderResponse(404, "html");
						sendFile("404.html", "Errors");
					}
				}
				else {
					sendHeaderResponse(415, "html");
					sendFile("415.html", "Errors");
				}
			
			}
			else if (!FunctionalityChecker.isMethodAllowed(ServerParser.parseHeaderRequest(headerLine))){
				sendHeaderResponse(405, "html");
				sendFile("405.html", "Errors");
			}
			else {
				sendHeaderResponse(500, "html");
				sendFile("500.html", "Errors");
			}
		}	
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				clientSocket.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}		
		}
	
	}
}
	
			
