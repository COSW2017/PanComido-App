package cosw.eci.edu.pancomido.ui.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.model.User;
import cosw.eci.edu.pancomido.data.network.RequestCallback;
import cosw.eci.edu.pancomido.data.network.RetrofitNetwork;
import cosw.eci.edu.pancomido.exception.NetworkException;
import cosw.eci.edu.pancomido.misc.SessionManager;
import cosw.eci.edu.pancomido.ui.fragment.OrderDetailFragment;
import cosw.eci.edu.pancomido.ui.fragment.PaymentFragment;
import cosw.eci.edu.pancomido.ui.fragment.RestaurantFragment;
import cosw.eci.edu.pancomido.ui.fragment.RestaurantListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static RestaurantListFragment restaurantListFragment = new RestaurantListFragment();
    public static RestaurantFragment restaurantFragment = new RestaurantFragment();
    private SessionManager session;
    private TextView userEmail;
    private TextView userName;
    private ImageView userImage;

    String[] permissions = { android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION };

    private final int ACCESS_LOCATION_PERMISSION_CODE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //session = new SessionManager(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ActivityCompat.requestPermissions(this,  permissions, ACCESS_LOCATION_PERMISSION_CODE);
        //cambiar el correo y el nombre del usuario

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        userEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.email);
        userName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        userImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.userImage);
        RetrofitNetwork retrofitNetwork = new RetrofitNetwork();
        RequestCallback<User> requestCallback = new RequestCallback<User>() {
            @Override
            public void onSuccess(final User response) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        session.setUserId(""+response.getUser_id());
                        userName.setText(response.getFirstname()+" "+response.getLastname());
                        userEmail.setText(response.getEmail());
                        if (response.getImage() != null) {
                            byte[] decodedString = Base64.decode(response.getImage(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            userImage.setImageBitmap(decodedByte);
                        }
                    }
                });
            }

            @Override
            public void onFailed(NetworkException e) {

            }
        };
        retrofitNetwork.getUserByEmail(session.getEmail(),requestCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        for ( int grantResult : grantResults ) {
            if ( grantResult == -1 ){
                return;
            }
        }
        switch ( requestCode )
        {
            case ACCESS_LOCATION_PERMISSION_CODE:
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, restaurantListFragment).addToBackStack(null).commit();
                break;
            default:
                super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            //FragmentManager manager = getFragmentManager();
            //manager.beginTransaction().replace(R.id.fragment_container, new PaymentFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_logout) {
            session.logoutUser();
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToOrderDetail() {
        OrderDetailFragment paymentFragment = new OrderDetailFragment();
        FragmentManager manager = getFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container, paymentFragment).addToBackStack("detail").commit();
    }

    public void showMessage(){
        Snackbar snackbar = Snackbar
                .make(this.findViewById(android.R.id.content).getRootView(), "See order ("+(session.getQ()+1)+") Total: "+session.getPrice(), Snackbar.LENGTH_LONG)
                .setAction("Go", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToOrderDetail();
                    }
                });

        snackbar.show();
    }
}
