package my.dinesh.hungervalley;

public class MenuModel {

    String name,price;


    public MenuModel(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public MenuModel(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}