package cosw.eci.edu.pancomido.data.model;

import java.util.Date;

public class Payment {

    private Integer id_payment;
    private Date payment_date;
    private Integer transaction_state;

    private Command id_command;

    private User user;

    public Payment(User usuario, Command command){
        this.user = usuario;
        this.id_command = command;
    }

    public Integer getId_payment() {
        return id_payment;
    }

    public void setId_payment(Integer id_payment) {
        this.id_payment = id_payment;
    }

    public Date getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }

    public Integer getTransaction_state() {
        return transaction_state;
    }

    public void setTransaction_state(Integer transactionState) {
        this.transaction_state = transactionState;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Command getId_command(){
        return id_command;
    }

    public void setId_command(Command command) {
        this.id_command = command;
    }

    public Double getMonto() {
        return 0.0;
    }
}
