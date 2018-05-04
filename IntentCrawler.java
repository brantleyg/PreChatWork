import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.helper.*;
import org.jsoup.select.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class IntentCrawler {
	
	private static ArrayList<String[]> hintLinkPairs;
	private static HashMap<String , String> myBonnie;
	private ArrayList<String> visitedLinks;
	private ArrayList<String> whiteListedLinks;
	private static String[] goodSites = {
			"https://www.asl.org/",
			"http://www.asl.org/"
	};
																				public int counter = 0;
	
	public IntentCrawler()  {
		myBonnie = new HashMap<String,String>();
		hintLinkPairs = new ArrayList<String[]>();
		visitedLinks = new ArrayList<String>();
		whiteListedLinks = new ArrayList<String>();
	}
	
	public void getPairs(String url) {
		if (!visitedLinks.contains(url)) {
			try {
				visitedLinks.add(url);
				System.out.println(url);
				
				Document doc = Jsoup.connect(url).get();
				
				myBonnie.put(url, doc.title());
																							counter++;
				Elements links = doc.select("a[href]");
				
				for (int i = 0 ; i < links.size(); i++) {
					Element page = links.get(i);
					if (aLinkWorthFightingFor(page)) {
						String[] example = {page.text() , page.absUrl("href")};
						boolean there = false;
						for (String[] gah : hintLinkPairs) {
							if (gah[0].equals(example[0]) && gah[1].equals(example[1])) {
								there = true;
								break;
							}
						}
						if (!there) {
							hintLinkPairs.add(example);
						}
																					if (counter < 10) {
						getPairs(page.absUrl("href"));
																										}
					} else {
						
					}
				}
			} catch (IOException e) {
				System.err.println("For '" + url + "': " + e.getMessage());
			}
		}
	}
	
	private boolean aLinkWorthFightingFor(Element page) {
		if (page.absUrl("href").startsWith("mailto:") ||
				page.absUrl("href").endsWith(".pdf") ||
				page.attr("href").equals("#") ||
				page.absUrl("href").equals("") ||
				page.absUrl("href").contains("javascript:popUp") ||
				!page.hasText()) {
			return false;
		}
		boolean whiteCond = false;
		for (int i = 0 ; i < goodSites.length ; i++) {
			if (page.absUrl("href").startsWith(goodSites[i])) {
				whiteCond = true;
				break;
			}
		}
		
		if (!whiteCond && !whiteListedLinks.contains(page.absUrl("href"))) {
			whiteListedLinks.add(page.absUrl("href"));
			System.out.println("WhiteList blocks " + page.absUrl("href"));
		}
		
		return whiteCond;
	}
	
	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		new IntentCrawler().getPairs("https://www.asl.org/");
		
		String fileName = "/tmp/ASLIntentsFullRun3.csv";
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		
		for (int j = 0 ; j < hintLinkPairs.size() ; j++) {
			String hint = hintLinkPairs.get(j)[0];
			String url = hintLinkPairs.get(j)[1];
			String title = myBonnie.get(url);
			String intent;
			

			if (title != null) {
				String prefix = "The American School in London: ";
				if (title.contains(prefix)) {
					intent = title.substring(prefix.length());
				} else {
					intent = title;
				}
				intent = intent.replaceAll(" ", "_");
				hint = hint.replaceAll(",", "");
				writer.write(hint + "," + intent + "\n");
			}
		}
		long endTime = System.currentTimeMillis();
		double totalTime = (double) (endTime - startTime) / 1000.0;
		System.out.println(totalTime + " seconds");
		writer.close();
		
	}
}
