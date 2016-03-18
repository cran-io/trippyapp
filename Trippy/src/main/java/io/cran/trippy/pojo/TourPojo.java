package io.cran.trippy.pojo;

/**
 * Created by MariaSol on 16/03/2016.
 */
public class TourPojo {
    private long id;
    private String name;
    private String description;
    private int imageId;
    private String transportation;
    private String weather;
    private String type;

    public TourPojo(long id, String name, String description, int imagePath, String transportation, String weather, String type) {
        this.id = id;
        this.setName(name);
        this.setDescription(description);
        this.setImageId(imagePath);
        this.setWeather(weather);
        this.setTransportation(transportation);
        this.setType(type);
    }

    public long getId()
    {
        return id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTransportation() {
        return transportation;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
