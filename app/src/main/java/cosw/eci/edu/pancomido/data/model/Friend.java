package cosw.eci.edu.pancomido.data.model;

public class Friend {

    private int id_friend;

    private User user_id;

    private User friend_id;
    private int state;

    public Friend(User user_id, User friend_id, int state){
        this.setUser_id(user_id);
        this.setFriend_id(friend_id);
        this.setState(state);
    }

    public Friend(){
    }

    public int getId_friend() {
        return id_friend;
    }

    public void setId_friend(int id_friend) {
        this.id_friend = id_friend;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public User getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(User friend_id) {
        this.friend_id = friend_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id_friend=" + id_friend +
                ", user_id=" + user_id +
                ", friend_id=" + friend_id +
                ", state=" + state +
                '}';
    }
}
