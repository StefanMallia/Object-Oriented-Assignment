package server;

class FunctionalityChecker {
	
	
	public static boolean isMethodAllowed(String method) {
		if (method.equals("GET"))
			return true;
		else
			return false;

	}


	public static boolean isFileAllowed(String fileName){
		int i = fileName.length() - 1;		
		while (fileName.charAt(i) != '.' && i > 0)
			i--;			
		String fileType = "";		
		while (i < fileName.length()) {
			fileType = fileType + fileName.charAt(i);
			i++;
		}
		if (fileType.equals(".txt") || fileType.equals(".html"))
			return true;
		else {
			System.out.println(fileType);
			return false;
		}
	}
}
