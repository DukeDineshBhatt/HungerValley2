package my.dinesh.hungervalley;

public class SubModel {

    public SubModel() {
    }

    String FoodName,Res,Type;
    int Price;

    public SubModel(String foodName, String res, String type, int price) {
        FoodName = foodName;
        Res = res;
        Type = type;
        Price = price;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }


    public String getRes() {
        return Res;
    }

    public void setRes(String res) {
        Res = res;
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
}
