package cosw.eci.edu.pancomido.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Santiago Carrillo
 */

public class OrdersManager
{

    private List<Dish> orderDishes = new ArrayList<>();


    public void addDish( Dish dish )
    {
        orderDishes.add( dish );
    }



    public List<Dish> getDishesByRestaurant(int restaurantId )
    {
        //TODO implement this
        return null;
    }
}
