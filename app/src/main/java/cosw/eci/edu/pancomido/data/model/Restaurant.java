package cosw.eci.edu.pancomido.data.model;

/**
 * Created by 2100038 on 9/12/17.
 */

public class Restaurant {

    private Integer id_restaurant;

    private String name;
    private Float latitude;
    private Float longitude;
    private Integer likes;
    private Integer dislike;
    private Integer love;
    private Integer angry;

    private User user_id;

    public Restaurant(Integer id_restaurant, String name, Float latitude, Float longitude, Integer like, Integer dislike, Integer love, Integer angry) {
        this.id_restaurant = id_restaurant;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.likes = like;
        this.dislike = dislike;
        this.love = love;
        this.angry = angry;
    }


    public Restaurant() { }

    public Integer getId_restaurant() {
        return id_restaurant;
    }

    public void setId_restaurant(Integer id_restaurant) {
        this.id_restaurant = id_restaurant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer like) {
        this.likes = like;
    }

    public Integer getDislike() {
        return dislike;
    }

    public void setDislike(Integer dislike) {
        this.dislike = dislike;
    }

    public Integer getLove() {
        return love;
    }

    public void setLove(Integer love) {
        this.love = love;
    }

    public Integer getAngry() {
        return angry;
    }

    public void setAngry(Integer angry) {
        this.angry = angry;
    }


    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    /*public List<Order> getOrders() {
        return orders;
    }*/

    /*public void setOrders(List<Order> orders) {
        this.orders = orders;
    }*/
}
