package cosw.eci.edu.pancomido.data.network;

import java.util.List;

import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.LoginWrapper;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.model.Todo;
import cosw.eci.edu.pancomido.data.model.Token;
import cosw.eci.edu.pancomido.data.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Nicolás Gómez Solano on 10/31/17.
 */

interface NetworkService
{

    @POST( "user/login" )
    Call<Token> login(@Body User user);

    @GET( "api/todo" )
    Call<List<Todo>> getTodoList();

    @POST( "api/todo" )
    Call createTodo(@Body Todo todo);

    @GET("restaurant/{name}")
    Call<Restaurant> getRestaurantInformation(@Path("name") String name);

    @GET("restaurant/{idRestaurant}/dish")
    Call<List<Dish>> getRestaurantDishes(@Path("idRestaurant") int idRestaurant);

    @GET( "restaurant/near/{latitude}/{longitude}")
    Call<List<Restaurant>> getRestaurants(@Path("latitude") Float latitude,
                                          @Path("longitude") Float longitude);
}
