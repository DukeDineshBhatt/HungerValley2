package my.dinesh.hungervalley;

public class ProductModel {

    String Name,Image,Stock;

    int Price,Discount;
    public ProductModel() {

    }

    public ProductModel(String name, String image, String stock, int price, int discount) {
        Name = name;
        Image = image;
        Stock = stock;
        Price = price;
        Discount = discount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        this.Price = price;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }
}
