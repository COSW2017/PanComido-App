package cosw.eci.edu.pancomido.data.model;

import java.util.Date;

/**
 * Created by 2100038 on 9/12/17.
 */
public class Comment {

    private int id_comment;

    private String comment_des;
    private Date comment_date;

    private Restaurant id_restaurant;

    public Comment(String comment, Date date) {
        this.comment_des = comment;
        this.comment_date = date;
    }

    public Comment() {
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

    public String getComment_des() {
        return comment_des;
    }

    public void setComment_des(String comment_des) {
        this.comment_des = comment_des;
    }

    public Date getComment_date() {
        return comment_date;
    }

    public void setComment_date(Date comment_date) {
        this.comment_date = comment_date;
    }
}
