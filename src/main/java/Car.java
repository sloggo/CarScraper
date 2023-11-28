public class Car {
    private String name;
    private double price;
    private int yearOfManufacture;
    private String location;
    private boolean warranty;
    private int mileage;
    private String link;
    private String rating;

    public String getRating(){
        return rating;
    }

    public Car(String name, double price, int yearOfManufacture, String location, boolean warranty, int mileage, String link) {
        this.name = name;
        this.price = price;
        this.yearOfManufacture = yearOfManufacture;
        this.location = location;
        this.warranty = warranty;
        this.mileage = mileage;
        this.link = link;
        this.rating = rateCar();
    }

    public String rateCar(){
        int score = 0;
        score -= yearOfManufacture*10;
        score += (mileage/10);
        score += price;
        if(warranty){
            score -= 10000;
        }
        return String.valueOf(score);
    }

    public void printInfo(){
        System.out.println(name);
        System.out.println();
        System.out.println("Price: "+price+" euro");
        System.out.println("Year: "+yearOfManufacture);
        System.out.println("Location: " + location);
        System.out.println("Mileage: "+mileage+"kms");
        System.out.println("Warranty: "+warranty);
        System.out.println("Link: "+link);
        System.out.println("Score: "+rating);
        System.out.println("------------------------------");
    }
}
