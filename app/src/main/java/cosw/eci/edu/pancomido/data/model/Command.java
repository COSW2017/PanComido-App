package cosw.eci.edu.pancomido.data.model;

import java.util.Date;
import java.util.List;

public class Command {

    private int id_command;

    private Integer state;
    private String creation_date;

    private Order id_order;

    public Command(){ }

    public Command(int idPedido){
        this.id_command = idPedido;
    } //¿Se necesitan los platos o no?

    public int getId_command() {
        return id_command;
    }

    public void setId_command(int idPedido) {
        this.id_command = idPedido;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public Order getId_order() {
        return id_order;
    }

    public void setId_order(Order order) {
        this.id_order = order;
    }

    @Override
    public String toString() {
        return "Command{" +
                "id_command=" + id_command +
                ", state=" + state +
                ", creation_date='" + creation_date + '\'' +
                ", id_order=" + id_order +
                '}';
    }
}
