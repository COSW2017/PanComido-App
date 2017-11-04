package cosw.eci.edu.pancomido.data.model;

public class Command_Dish {

    private int id_command_dish;

    private Command id_command;

    private Dish id_dish;

    public Command_Dish() {
    }

    public Command_Dish(Command command, Dish dish){
        this.id_command = command;
        this.id_dish = dish;

    }
    public int getId_command_dish() {
        return id_command_dish;
    }

    public void setId_command_dish(int id_command_dish) {
        this.id_command_dish = id_command_dish;
    }

    public Command getId_command() {
        return id_command;
    }

    public void setId_command(Command id_command) {
        this.id_command = id_command;
    }

    public Dish getId_dish() {
        return id_dish;
    }

    public void setId_dish(Dish id_dish) {
        this.id_dish = id_dish;
    }
}
