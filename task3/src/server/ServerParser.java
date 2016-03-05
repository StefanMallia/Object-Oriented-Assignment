package server;

class ServerParser {


	public static String parseHeaderRequest(String headerLine) {
		int i = 0;
		String method = "";
		while (headerLine.charAt(i) != ' ') {
			method = method + headerLine.charAt(i);
			i++;
		}
		return method;
	}
	
	
	public static String parseHeaderFileRequest(String headerLine) {
		int i = 0;
		String fileRequested = "";
		while (headerLine.charAt(i) != ' ')
			i++;		
		i = i + 2;
		while (headerLine.charAt(i) != ' ') {
			fileRequested = fileRequested + headerLine.charAt(i);
			i++;
		}		
		return fileRequested;
	}



	public static String contentType(String fileName) {
		int i = fileName.length() - 1;			
		while (fileName.charAt(i) != '.' && i > 0)
			i--;			
		String fileType = "";		
		while (i < fileName.length()) {
			fileType = fileType + fileName.charAt(i);
			i++;
		}
		if (fileType.equals(".html")){
			return "html";
		}
		else
			return "plain";
	}
}
