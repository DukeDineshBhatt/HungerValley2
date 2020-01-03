package my.dinesh.hungervalley;


public class CartDataSetGet {

  private String pName,Type;
  private Long price,quantity;

  public CartDataSetGet() {

  }

  public CartDataSetGet(String pName, Long price, Long quantity,String Type) {
    this.pName = pName;
    this.price = price;
    this.quantity = quantity;
    this.Type = Type;
  }

  public String getpName() {
    return pName;
  }

  public void setpName(String pName) {
    this.pName = pName;
  }

  public Long getQuantity() {
    return quantity;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }

  public Long getPrice() {
    return price;
  }

  public String getType() {
    return Type;
  }

  public void setType(String type) {
    this.Type = type;
  }

  public void setPrice(Long price) {
    this.price = price;
  }
}
