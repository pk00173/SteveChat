package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileHandler {
	
	public FileHandler(){
		
	} 
	
	
	
	public boolean checkIfFileContains(String filename, String searchItem) {
		File file = new File(filename);
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				if (line.contains(searchItem)) {
					scanner.close();
					return true;
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			return false;
		}

		return false;

	}
	
	/**
	 * Appends text to file
	 * @param filename
	 * @param text
	 */
	public void appendToFile(String filename, String text) {
		try {
			FileWriter fw = new FileWriter(filename, true);
			fw.write(text + "\n");
			fw.close();
		} catch (IOException ioe) {
			
		}
	}
}
