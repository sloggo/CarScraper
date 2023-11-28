import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DonedealScraper {
    private String html;
    private Document doc;
    private Elements carListings;
    private List<Car> cars;
    public DonedealScraper(String html){
        this.html = html;
        this.doc = Jsoup.parse(html);
        this.carListings = doc.getElementsByClass("Link__SLinkButton-sc-9jmsfg-0 emzqZy WithLinkstyled__SLink-sc-1n81fcg-0 jhmvmi");
        this.cars = new ArrayList<>();
    }

    public void formatListings(){
        for(Element carListing: carListings){
            try{
                boolean warranty = false;
                String carName = carListing.getElementsByClass("BasicHeaderstyled__Title-sc-78ow8b-0 cGWGnJ").text();
                String price = carListing.getElementsByClass("Pricestyled__Text-sc-1dt81j8-5 eZyZve").first().text();
                List<String> info = carListing.getElementsByClass("BasicHeaderstyled__KeyInfoItem-sc-78ow8b-2 kenrEJ").eachText();

                List<String> potentialWarranty = carListing.getElementsByClass("Tag__STag-sc-1eakqyn-0 hcLfLq").eachText();

                for(String item: potentialWarranty){
                    if(item.equals("Warranty")){
                        warranty=true;
                    }
                }

                String[] pieces = price.split(".");
                char[] charsPriceEur = {};
                char[] charsPriceCent = {};

                if(pieces.length>1){
                    charsPriceEur = pieces[0].toCharArray();
                    charsPriceCent = pieces[1].toCharArray();
                } else{
                    charsPriceEur = price.toCharArray();
                }

                String formattedCharPriceEur = "";
                for(char item : charsPriceEur){
                    if(Character.isDigit(item)){
                        formattedCharPriceEur += String.valueOf(item);
                    }
                }

                String formattedCharPriceCent = "";
                for(char item : charsPriceCent){
                    if(Character.isDigit(item)){
                        formattedCharPriceCent += String.valueOf(item);
                    }
                }

                String priceString = formattedCharPriceEur+"."+formattedCharPriceCent;
                double carPrice = Double.parseDouble(priceString);

                String mileageBeforeFormat = info.get(2);
                int mileageFormatted;
                if(mileageBeforeFormat.contains("km")){
                    char[] mileageArr = mileageBeforeFormat.toCharArray();
                    String formattedMileage = "";
                    for(char item : mileageArr){
                        if(Character.isDigit(item)){
                            formattedMileage += String.valueOf(item);
                        }
                    }

                    mileageFormatted = Integer.parseInt(formattedMileage);
                } else if(mileageBeforeFormat.contains("mi")){
                    char[] mileageArr = mileageBeforeFormat.toCharArray();
                    String formattedMileage = "";
                    for(char item : mileageArr){
                        if(Character.isDigit(item)){
                            formattedMileage += String.valueOf(item);
                        }
                    }

                    mileageFormatted = (int)(Double.parseDouble(formattedMileage)*1.60934);
                } else{
                    mileageFormatted = 0;
                }

                String link = carListing.select("a[href]").attr("href");

                if(mileageFormatted <= 10000){
                    mileageFormatted*=1000;
                }
                Car newCar = new Car(carName, carPrice, Integer.parseInt(info.get(0)), info.get(3), warranty, mileageFormatted, link);
                cars.add(newCar);
            } catch(NumberFormatException e) {
                System.out.println("invalid car");
            }
        }
        System.out.println("Successfully scraped "+cars.size()+" cars off of DoneDeal.");
        rankList();
        printList();
    }

    public void rankList(){
        cars.sort((o1, o2)
                -> o1.getRating().compareTo(
                o2.getRating()));
    }

    public void printList(){
        for(Car car : cars){
            car.printInfo();
        }
    }

}
