package my.dinesh.hungervalley;

public class CatSetGet {
    String Name,Image;

    public CatSetGet() {

    }

    public CatSetGet(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
}
