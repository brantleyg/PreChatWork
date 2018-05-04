import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BasicBot {
	public static String file = "/tmp/ASLIntentsFullRun.csv";
	public static BufferedReader br = null;
	public static String line = "";
	public static String cvsSplitBy = ",";
	
	public static String iLikePie(String word) {
		String result = "NO RESULT FOUND";
		try {
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				String[] text = line.split(cvsSplitBy);
				if (text[0].toLowerCase().contains(word.toLowerCase())) {
					result = text[2];
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		

		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(iLikePie(args[0]));
	}
	
}
