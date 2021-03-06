package cosw.eci.edu.pancomido.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.listener.RestaurantsListener;
import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.OrdersManager;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.model.User;
import cosw.eci.edu.pancomido.data.network.RequestCallback;
import cosw.eci.edu.pancomido.data.network.RetrofitNetwork;
import cosw.eci.edu.pancomido.exception.NetworkException;
import cosw.eci.edu.pancomido.misc.SessionManager;
import cosw.eci.edu.pancomido.ui.fragment.OrderDetailFragment;
import cosw.eci.edu.pancomido.ui.fragment.OrderHistoryFragment;
import cosw.eci.edu.pancomido.ui.fragment.RestaurantFragment;
import cosw.eci.edu.pancomido.ui.fragment.RestaurantListFragment;

public class RestaurantsActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RestaurantsListener
{

    public static RestaurantListFragment restaurantListFragment = new RestaurantListFragment();

    public static RestaurantFragment restaurantFragment = new RestaurantFragment();

    private SessionManager session;

    private TextView userEmail;

    private TextView userName;

    private ImageView userImage;

    private String real_email;

    private final OrdersManager ordersManager = new OrdersManager();

    String[] permissions =
            { android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION };

    private final int ACCESS_LOCATION_PERMISSION_CODE = 1;


    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        session = new SessionManager( this );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle( this, drawer, toolbar, R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close );
        drawer.setDrawerListener( toggle );
        toggle.syncState();
        ActivityCompat.requestPermissions( this, permissions, ACCESS_LOCATION_PERMISSION_CODE );
        //cambiar el correo y el nombre del usuario
        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );
        userEmail = (TextView) navigationView.getHeaderView( 0 ).findViewById( R.id.email );
        userName = (TextView) navigationView.getHeaderView( 0 ).findViewById( R.id.username );
        userImage = (ImageView) navigationView.getHeaderView( 0 ).findViewById( R.id.userImage );
        RetrofitNetwork retrofitNetwork = new RetrofitNetwork();
        RequestCallback<User> requestCallback = new RequestCallback<User>()
        {
            @Override
            public void onSuccess( final User response )
            {
                RestaurantsActivity.this.runOnUiThread( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        session.setUserId( "" + response.getUser_id() );
                        userName.setText( response.getFirstname() + " " + response.getLastname() );
                        userEmail.setText( response.getEmail() );
                        if ( response.getImage() != null )
                        {
                            try {
                                byte[] decodedString = Base64.decode(response.getImage(), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                userImage.setImageBitmap(decodedByte);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                } );
            }

            @Override
            public void onFailed( NetworkException e ){
            }
        };
        retrofitNetwork.getUserByEmail( session.getEmail(), requestCallback );
        real_email = session.getEmail().replaceAll("\\.","dot").replaceAll("@","at");
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("user~"+real_email);
        // [END subscribe_topics]
    }

    @Override
    public void onRequestPermissionsResult( int requestCode, String[] permissions, int[] grantResults )
    {
        for ( int grantResult : grantResults ){
            if ( grantResult == -1 ){
                return;
            }
        }
        switch ( requestCode )
        {
            case ACCESS_LOCATION_PERMISSION_CODE:
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace( R.id.fragment_container, restaurantListFragment ).addToBackStack(
                        null ).commit();
                break;
            default:
                super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        }
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if ( drawer.isDrawerOpen( GravityCompat.START ) )
        {
            drawer.closeDrawer( GravityCompat.START );
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        int id = item.getItemId();
        if ( id == R.id.action_settings )
        {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings( "StatementWithEmptyBody" )
    @Override
    public boolean onNavigationItemSelected( MenuItem item )
    {
        int id = item.getItemId();

        if ( id == R.id.nav_camera )
        {
            //FragmentManager manager = getFragmentManager();
            //manager.beginTransaction().replace(R.id.fragment_container, new PaymentFragment()).addToBackStack(null).commit();
        }
        else if ( id == R.id.nav_gallery )
        {
            Fragment fragment = new OrderHistoryFragment();
            FragmentManager manager =  getFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("history").commit();
        }
        else if ( id == R.id.nav_slideshow )
        {

        } 
		else if (id == R.id.nav_view) 
		{
            Intent user = new Intent(RestaurantsActivity.this, UserActivity.class);
            startActivity(user);
        }
        else if ( id == R.id.nav_logout )
        {
            session.logoutUser();
            Intent i = new Intent( RestaurantsActivity.this, LoginActivity.class );
            FirebaseMessaging.getInstance().unsubscribeFromTopic(real_email);
            startActivity( i );
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    public void goToOrderDetail()
    {
        OrderDetailFragment paymentFragment = new OrderDetailFragment();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace( R.id.fragment_container, paymentFragment ).addToBackStack(
                "detail" ).commit();
    }

    public void showMessage()
    {
        Snackbar snackbar = Snackbar.make( this.findViewById( android.R.id.content ).getRootView(),
                "See order (" + (ordersManager.getDishesCount() ) + ") Total: " + ordersManager.getOrderTotalPrice(),
                Snackbar.LENGTH_LONG ).setAction( "Go", new View.OnClickListener()
        {
            @Override
            public void onClick( View view )
            {
                goToOrderDetail();
            }
        } );

        snackbar.show();
    }

    @Override
    public void onAddDishToOrder( Dish dish )
    {
        ordersManager.addDish( dish );
    }

    @Override
    public int onGetTotalOrder() {
        return ordersManager.getOrderTotalPrice();
    }

    @Override
    public List<Restaurant> getRestaurants() {
        return ordersManager.getRestaurants();
    }

    @Override
    public List<Dish> getDishesByRestaurant(int idRestaurant) {
        return ordersManager.getDishesByRestaurant(idRestaurant);
    }

    @Override
    public int getDishQuanty(int idDish) {
        return ordersManager.getDishCount(idDish);
    }

    @Override
    public void deleteDishFromOrder(int idDish) {
        ordersManager.delDish(idDish);
    }
}
