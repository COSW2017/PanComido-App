package cosw.eci.edu.pancomido.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.adapter.RestaurantListAdapter;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.network.RequestCallback;
import cosw.eci.edu.pancomido.data.network.RetrofitNetwork;
import cosw.eci.edu.pancomido.exception.NetworkException;
import cosw.eci.edu.pancomido.misc.SessionManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestaurantListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestaurantListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int permissionCheck=2;
    private final int ACCESS_LOCATION_PERMISSION_CODE=1;
    LocationManager locationManager;
    private Float longitudeBest;
    private Float latitudeBest;
    private Button refreshButton;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;

    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout swipeLayout;

    public RestaurantListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestaurantListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantListFragment newInstance(String param1, String param2) {
        RestaurantListFragment fragment = new RestaurantListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        sessionManager = new SessionManager(getActivity());
    }

    @SuppressWarnings("MissingPermission")
    public void showMyLocation()
    {
        String[] permissions = { android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION };

        if ( hasPermissions( getActivity(), permissions ) )
        {
            getLocation();
        }
        else {

            ActivityCompat.requestPermissions(getActivity(), permissions, ACCESS_LOCATION_PERMISSION_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showDialog();
            }
        });

        LocationListener locationListenerBest = new LocationListener() {
            public void onLocationChanged(final Location location) {
                longitudeBest = Float.parseFloat(location.getLongitude()+"");
                latitudeBest = Float.parseFloat(location.getLatitude()+"");

                RetrofitNetwork retrofitNetwork = new RetrofitNetwork();
                RequestCallback<List<Restaurant>> rc = new RequestCallback<List<Restaurant>>() {
                    @Override
                    public void onSuccess(final List<Restaurant> response) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                configureRecyclerView(response);
                                sessionManager.setLocation(longitudeBest+"", latitudeBest+"");
                            }
                        });
                    }

                    @Override
                    public void onFailed(NetworkException e) {

                    }
                };
                retrofitNetwork.getRestaurants(latitudeBest, longitudeBest, rc);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //Toast.makeText(getActivity(), "Latitude "+longitudeBest+" "+latitudeBest, Toast.LENGTH_SHORT).show();
                        swipeLayout.setRefreshing(false);
                        hideDialog();
                    }
                });
                locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerBest);


    }

    private void hideDialog() {
        progressDialog.dismiss();
    }

    private void showDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Updating...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }


    public static boolean hasPermissions(Context context, String[] permissions )
    {
        for ( String permission : permissions )
        {
            if ( ContextCompat.checkSelfPermission( context, permission ) == PackageManager.PERMISSION_DENIED )
            {
                return false;
            }
        }
        return true;
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
                showMyLocation();
                break;
            default:
                super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));
        if(sessionManager.location()){
            Map<String, String> map = sessionManager.getLocation();
            getNearRestaurants(Float.parseFloat(map.get(sessionManager.LATITUDE)), Float.parseFloat(map.get(sessionManager.LONGITUDE+"")));
        }else{
            showMyLocation();
        }
        return view;
    }

    private void getNearRestaurants(Float lat, Float lo) {
        showDialog();
        RetrofitNetwork retrofitNetwork = new RetrofitNetwork();
        RequestCallback<List<Restaurant>> rc = new RequestCallback<List<Restaurant>>() {
            @Override
            public void onSuccess(final List<Restaurant> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        configureRecyclerView(response);
                    }
                });
            }
            @Override
            public void onFailed(NetworkException e) {
            }
        };
        retrofitNetwork.getRestaurants(lat, lo, rc);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeLayout.setRefreshing(false);
                hideDialog();
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRefresh() {
        showMyLocation();
        progressDialog.dismiss();

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void configureRecyclerView(List<Restaurant> restaurants){
        recyclerView = (RecyclerView) getActivity().findViewById( R.id.recyclerView );
        recyclerView.setHasFixedSize( true );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager( layoutManager );
        try {
            recyclerView.setAdapter(new RestaurantListAdapter(restaurants));
        }catch(Exception e){
            System.out.println(e);
        }
    }
}