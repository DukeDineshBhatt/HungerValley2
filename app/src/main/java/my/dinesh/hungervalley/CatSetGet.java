package my.dinesh.hungervalley;

public class CatSetGet {

    String Image,Name;

    public CatSetGet() {

    }

    public CatSetGet(String image, String name) {
        Image = image;
        Name = name;
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

    public void setName(String name) {
        Name = name;
    }
}
