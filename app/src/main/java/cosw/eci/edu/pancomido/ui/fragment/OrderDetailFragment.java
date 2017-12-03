package cosw.eci.edu.pancomido.ui.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.adapter.ExpandableListAdapter;
import cosw.eci.edu.pancomido.data.listener.RestaurantsListener;
import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.network.RequestCallback;
import cosw.eci.edu.pancomido.data.network.RetrofitNetwork;
import cosw.eci.edu.pancomido.exception.NetworkException;
import cosw.eci.edu.pancomido.misc.SessionManager;
import cosw.eci.edu.pancomido.ui.activity.RestaurantsActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderDetailFragment extends Fragment {

    private View view;
    private Button goPay;
    private ExpandableListView expandableListView;
    private HashMap<String, String> map;
    private List<Restaurant> restaurants;
    private HashMap<Integer, List<Dish>> dishesLists;
    private HashMap<Integer, List<Integer>> dishesQuanty;
    private Integer q =0;
    private static TextView totalOrder;
    private static RestaurantsListener restaurantsListener;


    private OnFragmentInteractionListener mListener;

    public OrderDetailFragment() {

    }

    public static OrderDetailFragment newInstance() {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        restaurantsListener = (RestaurantsActivity) getActivity();
        goPay = (Button) view.findViewById(R.id.goToPay);
        goPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPay();
            }
        });
        expandableListView = (ExpandableListView) view.findViewById(R.id.list_commands);
        totalOrder = (TextView) view.findViewById(R.id.resume_total);
        totalOrder.setText("Total: $"+restaurantsListener.onGetTotalOrder()+"");
        dishesLists = new HashMap<>();
        getDishes();
        return view;
    }

    private void getDishes() {
       List<Restaurant> restaurants = restaurantsListener.getRestaurants();
       for(Restaurant r : restaurants){
           List<Dish> dishes = restaurantsListener.getDishesByRestaurant(r.getId_restaurant());
           dishesLists.put(r.getId_restaurant(), dishes);
       }
        ExpandableListAdapter listAdapter = new ExpandableListAdapter(getActivity(), restaurantsListener, dishesLists);
        expandableListView.setAdapter(listAdapter);
    }

    private void goToPay() {
        PaymentFragment paymentFragment = new PaymentFragment();
        FragmentManager manager = getActivity().getFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container, paymentFragment).addToBackStack("detail").commit();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static void refreshPrice(){
        totalOrder.setText("Total: $"+restaurantsListener.onGetTotalOrder()+"");
    }
}
