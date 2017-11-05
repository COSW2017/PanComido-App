package cosw.eci.edu.pancomido.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.adapter.DishAdapter;
import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.network.RequestCallback;
import cosw.eci.edu.pancomido.data.network.RetrofitNetwork;
import cosw.eci.edu.pancomido.exception.NetworkException;
import cosw.eci.edu.pancomido.misc.SessionManager;
import cosw.eci.edu.pancomido.misc.loadImage;
import cosw.eci.edu.pancomido.ui.activity.LoginActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RestaurantFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RestaurantFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static View view;
    private ImageView restaurantImage;
    private TextView restaurantName;
    private Button menu, comments;
    private RecyclerView listView;
    private LinearLayout progressBar, name, restaurant_options;
    private static SessionManager sessionManager;
    private Bundle args;

    private OnFragmentInteractionListener mListener;

    public RestaurantFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestaurantFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantFragment newInstance(String param1, String param2) {
        RestaurantFragment fragment = new RestaurantFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        args = this.getArguments();
        sessionManager = new SessionManager(getActivity());
        progressBar = (LinearLayout) view.findViewById(R.id.linearProgressBar);
        name = (LinearLayout) view.findViewById(R.id.name);
        restaurantImage = (ImageView) view.findViewById(R.id.restaurant_image);
        menu = (Button) view.findViewById(R.id.restaurant_menu);
        comments = (Button) view.findViewById(R.id.restaurant_comments);
        restaurantName = (TextView) view.findViewById(R.id.restaurant_name);
        restaurant_options = (LinearLayout) view.findViewById(R.id.restaurant_options);
        menu.setOnClickListener(this);
        comments.setOnClickListener(this);

        showMenu();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public static void showMessage(){
        Snackbar snackbar = Snackbar
                .make(view, "See order ("+sessionManager.getQ()+") Total: "+sessionManager.getPrice(), Snackbar.LENGTH_LONG)
                .setAction("Go", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToOrderDetail();
                Log.d("TODOOOOOOOOO", "TOPDOOOOOOOOO");
            }
        });

        snackbar.show();
    }

    private static void goToOrderDetail() {
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.restaurant_menu:
                showMenu(); break;
            case R.id.restaurant_comments:
                showComments(); break;
        }
    }

    private void showComments() {
        menu.setTypeface(null, Typeface.NORMAL);
        comments.setTypeface(null, Typeface.BOLD);

    }

    private void showMenu() {
        comments.setTypeface(null, Typeface.NORMAL);
        menu.setTypeface(null, Typeface.BOLD);
        final RetrofitNetwork r = new RetrofitNetwork();
        r.getRestaurantInformation(args.get("name")+"", new RequestCallback<Restaurant>() {
            @Override
            public void onSuccess(final Restaurant response) {
                Log.d("Restaurant", response.getId_restaurant()+"");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        restaurantName.setText(response.getName());
                        new loadImage(restaurantImage).execute(response.getImage());
                    }
                });
                r.getRestaurantDishes(response.getId_restaurant(), new RequestCallback<List<Dish>>() {
                    @Override
                    public void onSuccess(final List<Dish> response) {
                        for(Dish dish : response){
                            Log.d("Dish", dish.getName());
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                configureRecyclerView(response);
                                showElements();
                            }
                        });
                    }
                    @Override
                    public void onFailed(NetworkException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailed(NetworkException e) {
                e.printStackTrace();
            }
        });
    }

    private void showElements() {
        progressBar.setVisibility(View.GONE);
        restaurantImage.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        restaurant_options.setVisibility(View.VISIBLE);
        listView.setVisibility(View.VISIBLE);
    }

    private void configureRecyclerView(List<Dish> dishes){
        listView = (RecyclerView) view.findViewById(R.id.restaurant_list);
        listView.setHasFixedSize( true );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager( layoutManager );
        try {
            DishAdapter dishAdapter = new DishAdapter(dishes);
            listView.setAdapter(dishAdapter);

        }catch(Exception e){
            System.out.println(e);
        }

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


}
