package cosw.eci.edu.pancomido.data.model;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreditCard implements PaymentMethod {

    String regex = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|" +
            "(?<mastercard>5[1-5][0-9]{14})|" +
            "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|" +
            "(?<amex>3[47][0-9]{13})|" +
            "(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|" +
            "(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$";


    private int securityCode;

    private String car_number;
    private String expirationDate;
    private String userName;
    private String type;

    public CreditCard(){ }

    public CreditCard(int codigoSeguridad, String car_number, String fechaVencimiento, String nombreUsuario, String tipoTarjeta){
        this.securityCode = codigoSeguridad;
        this.setCar_number(car_number);
        this.expirationDate = fechaVencimiento;
        this.userName = nombreUsuario;
        this.type = tipoTarjeta;
    }


    public boolean isValid(){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(getCar_number());
        return matcher.matches();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(int securityCode) {
        this.securityCode = securityCode;
    }

    public String getCar_number() {
        return car_number;
    }

    public void setCar_number(String number) {
        this.car_number = number;
    }
}
