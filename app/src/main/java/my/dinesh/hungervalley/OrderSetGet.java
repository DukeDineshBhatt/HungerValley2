package my.dinesh.hungervalley;

public class OrderSetGet {

    String pName,price,quantity,res,type;

    public OrderSetGet() {

    }


    public OrderSetGet(String pName, String price, String quantity, String res, String type) {
        this.pName = pName;
        this.price = price;
        this.quantity = quantity;
        this.res = res;
        this.type = type;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

