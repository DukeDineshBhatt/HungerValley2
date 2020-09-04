package my.dinesh.hungervalley;

public class MenuModel {

    String FoodName,Type,Status;
    int Price;

    public MenuModel(String foodName, String type, String status, int price) {
        FoodName = foodName;
        Type = type;
        Status = status;
        Price = price;
    }

    public MenuModel(){

    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String FoodName) {
        FoodName = FoodName;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}