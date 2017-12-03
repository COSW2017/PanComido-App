package cosw.eci.edu.pancomido.data.listener;

import java.util.List;

import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.Restaurant;

/**
 * @author Santiago Carrillo
 */

public interface RestaurantsListener {

    void onAddDishToOrder(Dish dish);

    int onGetTotalOrder();

    List<Restaurant> getRestaurants();

    List<Dish> getDishesByRestaurant(int idRestaurant);

    int getDishQuanty(int idDish);

    void deleteDishFromOrder(int idDish);



    void showMessage();
}
