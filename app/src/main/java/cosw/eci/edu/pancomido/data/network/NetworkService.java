package cosw.eci.edu.pancomido.data.network;

import java.util.List;

import cosw.eci.edu.pancomido.data.model.Command;
import cosw.eci.edu.pancomido.data.model.Command_Dish;
import cosw.eci.edu.pancomido.data.model.Comment;
import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.LoginWrapper;
import cosw.eci.edu.pancomido.data.model.Order;
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

    @POST( "user/register" )
    Call<User> createUser(@Body User user);

    @POST("user/search")
    Call<User> getUserByEmail(@Body String email);

    @GET("restaurant/{name}")
    Call<Restaurant> getRestaurantInformation(@Path("name") String name);

    @GET("restaurant/{idRestaurant}/dish")
    Call<List<Dish>> getRestaurantDishes(@Path("idRestaurant") int idRestaurant);

    @GET("restaurant/{idRestaurant}/dish/{idDish}")
    Call<Dish> getDishById(@Path("idRestaurant") int idRestaurant, @Path("idDish") int idDish);

    @GET("restaurant/near/{latitudelongitude}/")
    Call<List<Restaurant>> getRestaurants(@Path("latitudelongitude") String latitude);

    @GET("user/{email}/friends")
    Call<List<User>> getFriends(@Path("email") String userMail);

    @POST( "restaurant/command" )
    Call<Command> addCommand(@Body Command command);

    @POST( "user/order" )
    Call<Order> addOrder(@Body Order order);

    @POST( "commandDish/{id_command}/{id_dish}" )
    Call<Boolean> addCommandDish(@Path("id_command") Integer id_command, @Path("id_dish")  Integer id_dish);

    @GET("restaurant/{idRestaurant}/comment")
    Call<List<Comment>> getRestaurantComments(@Path("idRestaurant") int idRestaurant);

    @GET("user/{idUser}/order")
    Call<List<Order>> getOrdersByUser(@Path("idUser") int idUser);

    @GET("user/order/{idOrder}")
    Call<Order> getOrderById(@Path("idOrder") int idOrder);

    @GET("restaurant/command/{idOrder}")
    Call<List<Command>> getCommandsByOrder(@Path("idOrder") int idOrder);

    @GET("restaurant/0/command/{idCommand}")
    Call<List<Dish>> getDishesByCommand(@Path("idCommand") int idCommand);

}
