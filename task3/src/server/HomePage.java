package server;

import java.io.*;
import java.lang.*;

class HomePage {

	
	public static void update(String directory, String portNum) {
		BufferedWriter writer = null;
		File folder = new File(directory);
		File[] fileArray = folder.listFiles();


		try {
			File homePage = new File(directory + "/" + "homepage.html");
			writer = new BufferedWriter(new FileWriter(homePage));
			writer.write("<h1>Home page</h1>\n");
			
			for (int i = 0; i < fileArray.length; i++) {
				if (!fileArray[i].getName().equals("homepage.html") && (fileArray[i].getName().endsWith(".txt") || fileArray[i].getName().endsWith(".html")))
					writer.write("<a href=\"http://127.0.0.1:" + portNum + "/" + fileArray[i].getName() + "\">" + fileArray[i].getName() + "</a><br>");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				writer.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	
	}	



}
