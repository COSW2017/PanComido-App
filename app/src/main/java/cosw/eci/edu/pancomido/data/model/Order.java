package cosw.eci.edu.pancomido.data.model;

import java.util.Date;

/**
 * Created by NGS on 9/12/17.
 */

public class Order {

    private Integer id_order;
    private String creation_date;

    public Integer state;

    public Boolean ready;

    //private List<Command> commands; //Falta guardar los pedidos

    private User user_id;


    public Order() {
        this.state = -1;
        this.ready = true;
    }

    public Order(Integer id_order, User user) {
        this.id_order = id_order;
        this.user_id = user;
        this.state = -1;
        this.ready = true;
    }

    public Integer getId_order() {
        return id_order;
    }

    public void setId_order(Integer id_order) {
        this.id_order = id_order;
    }


    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user) {
        this.user_id = user;
    }

    /*

    public void addPedido(Command p){
        this.getCommands().add(p);
    }

    public Command getPedidoById(int id){
        return null;
    }

    public void delPedido(int idPedido){
        List<Command> pedidosBack = getCommands();
        boolean found = false;
        for(int i=0; i<pedidosBack.size() && !found; i++){
            if(idPedido== getCommands().get(i).getId_command()){
                getCommands().remove(getCommands().get(i));
                found=true;
            }
        }
    }

    */

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id_order +
                ", users=" + "" +
                '}';
    }

    public void setReady(boolean ready){
        this.ready = ready;
    }

    /*
    En cola: 0, En progreso: 1, Listo=2
     */
    public boolean isReady(){
        return ready;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    //ToDo: falta calcular los pagos

}
