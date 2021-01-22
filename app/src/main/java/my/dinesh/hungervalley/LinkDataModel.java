package my.dinesh.hungervalley;

public class LinkDataModel {

    String Image,Link;

    public LinkDataModel() {

    }

    public LinkDataModel(String image, String link) {
        Image = image;
        Link = link;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
