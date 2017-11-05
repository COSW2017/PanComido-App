package cosw.eci.edu.pancomido.data.network;

import java.util.List;

import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.LoginWrapper;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.model.Todo;
import cosw.eci.edu.pancomido.data.model.Token;
import cosw.eci.edu.pancomido.data.model.User;

/**
 * Created by estudiante on 10/31/17.
 */

public interface Network
{
    void login(User loginWrapper, RequestCallback<Token> requestCallback);

    void createTodo(Todo todo, RequestCallback requestCallback);

    void getTodoList(RequestCallback<List<Todo>> requestCallback);

    void createUser(User user, RequestCallback<User> requestCallback);

    void getRestaurantInformation(String name,  RequestCallback<Restaurant> requestCallback);

    void getRestaurantDishes(Integer idRestaurant, RequestCallback<List<Dish>> requestCallback);

    void getDishById(Integer idRestaurant, Integer idDish, RequestCallback<Dish> requestCallback);

    void getRestaurants(Float latitude, Float longitude, RequestCallback<List<Restaurant>> requestCallback);

    void getFriends(String userMail, RequestCallback<List<User>> requestCallback);

    void getUserByEmail(String user, RequestCallback<User> requestCallback);
}
