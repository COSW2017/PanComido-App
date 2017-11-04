package cosw.eci.edu.pancomido.data.model;

/**
 * @author Santiago Carrillo
 * 8/21/17.
 */
public class User {

    private Integer user_id;

    private String email;
    private String user_password;
    private String firstname;
    private String lastname;
    private String city;
    private String image;
    private String cellphone;

    public User(){}

    public User(Integer user_id){
        this.user_id = user_id;
    }

    public User( String email, String password, String firstname, String lastname, String image, String city, String cellphone) {
        this.setUser_password(password);
        this.setFirstname(firstname);
        this.setEmail(email);
        this.setLastname(lastname);
        this.setImage(image);
        this.setCity(city);
        this.setCellphone(cellphone);
    }


    public PaymentMethod getPaymentMethod(){
        return null;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String password) {
        this.user_password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    @Override
    public String toString()
    {
        return "User{" + "id=" + getUser_id() + ", email='" + getEmail() + '\'' + ", password='" + getUser_password() + '\'' + ", firstname='"
                + getFirstname() + '\'' + '}';
    }

}
