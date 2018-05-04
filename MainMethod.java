import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.*;
import org.jsoup.select.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainMethod {
	public static void main(String[] args) throws IOException {
		
		// Loading a document from the internet
		Document doc = Jsoup.connect("https://www.asl.org").get();
		
		/*// Loading a file
		File temp = new File("/tmp/asl.org/www.asl.org/index.html");
		// Making a document from the file
		Document doc = Jsoup.parse(temp, "UTF-8" , "https://www.asl.org/");*/
		
		// Finding all of the elements with a a[href] tag (basically finding buttons with links)
		Elements links = doc.select("a[href]");

		ArrayList<String[]> gah = new ArrayList<String[]>();
		
		// Going through all of the elements in links
		for (int i = 0 ; i < links.size(); i++) {
			// Taking an element
			Element asdf = links.get(i);
			// Checking if the element actually has a valid link
			if (!asdf.attr("href").equals("#") && !asdf.attr("href").contains("javascript:popUp") && asdf.hasText()) {
				// Setting the array to get the data
				String[] example = {asdf.text() , asdf.absUrl("href")};
				gah.add(example);
			}
		}
		
		// Printing out all of the word - link pairs
		for (int j = 0 ; j < gah.size() ; j++) {
			System.out.println(gah.get(j)[0] + " goes to " + gah.get(j)[1]);
		}
	}
}
