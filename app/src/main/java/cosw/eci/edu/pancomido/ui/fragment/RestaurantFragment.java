package cosw.eci.edu.pancomido.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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
import cosw.eci.edu.pancomido.data.adapter.CommentsAdapter;
import cosw.eci.edu.pancomido.data.adapter.DishAdapter;
import cosw.eci.edu.pancomido.data.model.Comment;
import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.network.RequestCallback;
import cosw.eci.edu.pancomido.data.network.RetrofitNetwork;
import cosw.eci.edu.pancomido.exception.NetworkException;
import cosw.eci.edu.pancomido.misc.SessionManager;
import cosw.eci.edu.pancomido.misc.loadImage;
import cosw.eci.edu.pancomido.ui.activity.LoginActivity;
import cosw.eci.edu.pancomido.ui.activity.RestaurantsActivity;


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

    private static View view;
    private ImageView restaurantImage;
    private TextView restaurantName;
    private Button menu, comments;
    private RecyclerView listView;
    private LinearLayout progressBar, name, restaurant_options;
    private Bundle args;
    private Restaurant restaurant;

    private OnFragmentInteractionListener mListener;

    public RestaurantFragment() {
    }

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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.restaurant_menu:
                showMenu(); break;
            case R.id.restaurant_comments:
                showComments(); break;
        }
    }

    private void showComments() {
        hideElements();
        menu.setTypeface(null, Typeface.NORMAL);
        comments.setTypeface(null, Typeface.BOLD);
        final RetrofitNetwork r = new RetrofitNetwork();
        Log.d("RESSSSSSSSSS", ""+restaurant.getId_restaurant());
        r.getRestaurantComments(restaurant.getId_restaurant(), new RequestCallback<List<Comment>>() {
            @Override
            public void onSuccess(final List<Comment> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        configureRecyclerViewComments(response);
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

    private void showMenu() {
        comments.setTypeface(null, Typeface.NORMAL);
        menu.setTypeface(null, Typeface.BOLD);
        final RetrofitNetwork r = new RetrofitNetwork();
        r.getRestaurantInformation(args.get("name")+"", new RequestCallback<Restaurant>() {
            @Override
            public void onSuccess(final Restaurant response) {
                restaurant = response;
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
                                configureRecyclerViewDishes(response);
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

    private void hideElements() {
        progressBar.setVisibility(View.VISIBLE);
        restaurantImage.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        restaurant_options.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);
    }

    private void configureRecyclerViewComments(List<Comment> comments){
        listView = (RecyclerView) view.findViewById(R.id.restaurant_list);
        listView.setHasFixedSize( true );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager( layoutManager );
        try {
            CommentsAdapter commentsAdapter = new CommentsAdapter(comments);
            listView.setAdapter(commentsAdapter);

        }catch(Exception e){
            System.out.println(e);
        }

    }

    private void configureRecyclerViewDishes(List<Dish> dishes){
        listView = (RecyclerView) view.findViewById(R.id.restaurant_list);
        listView.setHasFixedSize( true );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager( layoutManager );
        try {
            DishAdapter dishAdapter = new DishAdapter(dishes, (RestaurantsActivity) getActivity());
            listView.setAdapter(dishAdapter);

        }catch(Exception e){
            System.out.println(e);
        }

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
