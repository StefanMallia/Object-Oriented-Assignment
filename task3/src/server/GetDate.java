package server;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class GetDate {
	public static void main(String[] args) {
		System.out.println(getDate());
	}
		
	public static String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z");
		Date date = new Date();
		
		return dateFormat.format(date);
	  }
}
