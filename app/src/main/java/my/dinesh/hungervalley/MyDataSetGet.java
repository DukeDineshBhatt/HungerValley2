package my.dinesh.hungervalley;

public class MyDataSetGet {


    String Restaurant_name, Restaurant_type, Status, Banner;

    double Rating;
    int Discount;

    public MyDataSetGet() {

    }

    public MyDataSetGet(String restaurant_name, String restaurant_type, String status, String banner, double rating, int discount) {
        Restaurant_name = restaurant_name;
        Restaurant_type = restaurant_type;
        Status = status;
        Banner = banner;
        Rating = rating;
        Discount = discount;
    }

    public String getRestaurant_name() {
        return Restaurant_name;
    }

    public void setRestaurant_name(String Restaurant_name) {
        Restaurant_name = Restaurant_name;
    }

    public String getRestaurant_type() {
        return Restaurant_type;
    }

    public void setRestaurant_type(String restaurant_type) {
        Restaurant_type = restaurant_type;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getBanner() {
        return Banner;
    }

    public void setBanner(String banner) {
        Banner = banner;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }
}
