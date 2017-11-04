package cosw.eci.edu.pancomido.data.model;

import java.util.Date;

/**
 * Created by 2100038 on 9/12/17.
 */
public class Comment {

    private int id_comment;

    private String comment;
    private Date date;

    private Restaurant id_restaurant;

    public Comment(String comment, Date date) {
        this.comment = comment;
        this.date = date;
    }

    public Comment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId_comment() {
        return id_comment;
    }

    public void setId_comment(int id_comment) {
        this.id_comment = id_comment;
    }

    public Restaurant getId_restaurant() {
        return id_restaurant;
    }

    public void setId_restaurant(Restaurant id_restaurant) {
        this.id_restaurant = id_restaurant;
    }
}
