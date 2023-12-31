import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    List<Car> cars = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a search keyword (leave empty for all):");
        String searchQuery = scanner.nextLine();
        String[] queries = searchQuery.split(" ");
        searchQuery = "";

        for(int i=0; i<queries.length; i++){
            if(i != 0){
                searchQuery+="%20";
            }
            searchQuery += queries[i];
        }

        String html = "";
        for(int i=0; i<10; i++){
            html += httpReq("https://www.donedeal.ie/cars?words="+searchQuery+"&start="+(i*30));
        }

        DonedealScraper scraper = new DonedealScraper(html);
        scraper.formatListings();
    }


    public static String httpReq(String url) throws IOException {
        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        int responseCode = con.getResponseCode();
        System.out.println("Response code: "+responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuilder response = new StringBuilder();

        while((inputLine = in.readLine()) != null){
            response.append(inputLine);
        }

        in.close();
        String html = response.toString();
        return html;
    }
}
