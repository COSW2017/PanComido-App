package cosw.eci.edu.pancomido.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.network.RequestCallback;
import cosw.eci.edu.pancomido.data.network.RetrofitNetwork;
import cosw.eci.edu.pancomido.exception.NetworkException;
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
    private View view;
    private ImageView restaurantImage;
    private TextView restaurantName;
    private Button menu, comments;
    private ListView listView;

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
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        menu = (Button) view.findViewById(R.id.restaurant_menu);
        comments = (Button) view.findViewById(R.id.restaurant_comments);
        restaurantName = (TextView) view.findViewById(R.id.restaurant_name);
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
        r.getRestaurantInformation("Tierra Colombiana", new RequestCallback<Restaurant>() {
            @Override
            public void onSuccess(final Restaurant response) {
                Log.d("Restaurant", response.getId_restaurant()+"");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        restaurantName.setText(response.getName());
                    }
                });
                r.getRestaurantDishes(response.getId_restaurant(), new RequestCallback<List<Dish>>() {
                    @Override
                    public void onSuccess(List<Dish> response) {
                        for(Dish dish : response){
                            Log.d("Dish", dish.getName());
                        }
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
