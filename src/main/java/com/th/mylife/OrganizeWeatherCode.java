import java.io.*;
import java.util.*;

public class OrganizeWeatherCode {
	public static void main (String[] args) throws Exception{
		BufferedReader fileReader = new BufferedReader(new FileReader("/Users/Thomas/Documents/Data Structure Programming/Small Project/Weather Codes.txt"));
		BufferedWriter fileWriter = new BufferedWriter( new FileWriter("/Users/Thomas/Documents/Data Structure Programming/Small Project/New Weather Codes.txt"));
		String line;
		String prev_line = "         ";
		while((line = fileReader.readLine()) != null){
			int max = 4; 
			if(line.length() < max){
				max = line.length();
				// System.out.println(line.length());
			}
			// System.out.println(line);
			if(!line.substring(0, max).contentEquals("USCA")){
				prev_line += String.format(" %s", line); 
			}else{
				fileWriter.write(String.format("%s %s\n", prev_line.substring(0, 8), prev_line.substring(8, prev_line.length())));
				prev_line = line;
			}

		}
		fileReader.close();
		fileWriter.close();

	}
}