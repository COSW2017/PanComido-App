package cosw.eci.edu.pancomido.data.network;

import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.LoginWrapper;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.model.Todo;
import cosw.eci.edu.pancomido.data.model.Token;
import cosw.eci.edu.pancomido.data.model.User;
import cosw.eci.edu.pancomido.exception.NetworkException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by estudiante on 10/31/17.
 */

public class RetrofitNetwork
        implements Network
{

    private static final String BASE_URL = "http://pancomido-cosw.herokuapp.com/";

    private NetworkService networkService;

    private ExecutorService backgroundExecutor = Executors.newFixedThreadPool( 1 );

    public RetrofitNetwork()
    {
        Retrofit retrofit =
                new Retrofit.Builder().baseUrl( BASE_URL ).addConverterFactory( GsonConverterFactory.create() ).build();
        networkService = retrofit.create( NetworkService.class );
    }

    @Override
    public void login(final User loginWrapper, final RequestCallback<Token> requestCallback )
    {
        backgroundExecutor.execute( new Runnable()
        {
            @Override
            public void run()
            {
                Call<Token> call = networkService.login( loginWrapper );
                try
                {
                    Response<Token> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );
                }
                catch ( IOException e )
                {
                    requestCallback.onFailed( new NetworkException( null, e ) );
                }
            }
        } );

    }

    @Override
    public void createTodo(final Todo todo, final RequestCallback requestCallback) {

        backgroundExecutor.execute( new Runnable()
        {
            @Override
            public void run()
            {
                Call call = networkService.createTodo( todo );
                try
                {
                    Response execute = call.execute();
                    requestCallback.onSuccess( execute.body() );
                }
                catch ( IOException e )
                {
                    requestCallback.onFailed( new NetworkException( null, e ) );
                }
            }
        } );
    }

    @Override
    public void getTodoList(final RequestCallback<List<Todo>> requestCallback) {


        backgroundExecutor.execute( new Runnable()
        {
            @Override
            public void run()
            {
                Call call = networkService.getTodoList();
                try
                {
                    Response<List<Todo>> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );
                }
                catch ( IOException e )
                {
                    requestCallback.onFailed( new NetworkException( null, e ) );
                }
            }
        } );
    }

    @Override
    public void createUser(final User user, final RequestCallback<User> requestCallback) {
        backgroundExecutor.execute( new Runnable()
        {
            @Override
            public void run()
            {
                Call call = networkService.createUser( user );
                try
                {
                    Response<User> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );
                }
                catch ( IOException e )
                {
                    requestCallback.onFailed( new NetworkException( null, e ) );
                }
            }
        } );
    }

    public void getRestaurantInformation(final String name, final RequestCallback<Restaurant> requestCallback) {
        backgroundExecutor.execute( new Runnable()
        {
            @Override
            public void run()
            {
                Call call = networkService.getRestaurantInformation(name);
                try
                {
                    Response<Restaurant> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );
                }
                catch ( IOException e )
                {
                    requestCallback.onFailed( new NetworkException( null, e ) );
                }
            }
        } );
    }

    @Override
    public void getRestaurantDishes(final Integer idRestaurant, final RequestCallback<List<Dish>> requestCallback) {
        backgroundExecutor.execute( new Runnable()
        {
            @Override
            public void run()
            {
                Call call = networkService.getRestaurantDishes(idRestaurant);
                try
                {
                    Response<List<Dish>> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );
                }
                catch ( IOException e )
                {
                    requestCallback.onFailed( new NetworkException( null, e ) );
                }
            }
        } );
    }

    @Override
    public void getRestaurants(final Float latitude, final Float longitude, final RequestCallback<List<Restaurant>> requestCallback) {
        backgroundExecutor.execute( new Runnable()
        {
            @Override
            public void run()
            {
                Call call = networkService.getRestaurants(latitude, longitude);
                try
                {
                    Response<List<Restaurant>> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );
                }
                catch ( IOException e )
                {
                    requestCallback.onFailed( new NetworkException( null, e ) );
                }
            }
        } );

    }

    public void addSecureTokenInterceptor( final String token )
    {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor( new Interceptor()
        {
            @Override
            public okhttp3.Response intercept( Chain chain )
                    throws IOException
            {
                Request original = chain.request();

                Request request = original.newBuilder().header( "Accept", "application/json" ).header( "Authorization",
                        "Bearer "
                                + token ).method(
                        original.method(), original.body() ).build();
                return chain.proceed( request );
            }
        } );
        Retrofit retrofit =
                new Retrofit.Builder().baseUrl( BASE_URL ).addConverterFactory( GsonConverterFactory.create() ).client(
                        httpClient.build() ).build();
        networkService = retrofit.create( NetworkService.class );
    }

}
