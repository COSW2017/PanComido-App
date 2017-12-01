package cosw.eci.edu.pancomido.data.listener;

import cosw.eci.edu.pancomido.data.model.Dish;

/**
 * @author Santiago Carrillo
 */

public interface RestaurantsListener
{

    void onAddDishToOrder( Dish dish );
}
