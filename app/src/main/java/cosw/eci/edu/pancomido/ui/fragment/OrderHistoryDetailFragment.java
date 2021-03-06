package cosw.eci.edu.pancomido.ui.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.adapter.ExpandableListAdapter;
import cosw.eci.edu.pancomido.data.adapter.HistoryListAdapter;
import cosw.eci.edu.pancomido.data.listener.RestaurantsListener;
import cosw.eci.edu.pancomido.data.model.Command;
import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.Order;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.network.RequestCallback;
import cosw.eci.edu.pancomido.data.network.RetrofitNetwork;
import cosw.eci.edu.pancomido.exception.NetworkException;
import cosw.eci.edu.pancomido.misc.loadImage;
import cosw.eci.edu.pancomido.ui.activity.RestaurantsActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderHistoryDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderHistoryDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryDetailFragment extends Fragment {

    private View view;
    private Button goPay;
    private ExpandableListView expandableListView;
    private HashMap<String, String> map;
    private HashMap<Integer, List<Dish>> dishesLists;
    private HashMap<Integer, List<Integer>> dishesQuanty;
    private HashSet<Restaurant> restaurants;
    private Integer q =0;
    private boolean is_ready;
    private static RestaurantsListener restaurantsListener;

    private int internal_cont;

    private ProgressDialog progressDialog;

    private Order order;

    private Bundle args;


    private OnFragmentInteractionListener mListener;
    private int size;

    public OrderHistoryDetailFragment() {

    }

    public static OrderHistoryDetailFragment newInstance() {
        OrderHistoryDetailFragment fragment = new OrderHistoryDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.order_row, container, false);
        internal_cont = 0;
        args = this.getArguments();
        restaurantsListener = (RestaurantsActivity) getActivity();
        /*goPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPay();
            }
        });*/
        restaurants = new HashSet<>();
        expandableListView = (ExpandableListView) view.findViewById(R.id.history_list_commands);
        dishesLists = new HashMap<>();
        //getDishes();
        getOrderDetail();
        return view;
    }

    private void getOrderDetail() {
        showDialog();
        final RetrofitNetwork r = new RetrofitNetwork();
        r.getOrderById(Integer.parseInt(args.get("id_order")+""), new RequestCallback<Order>() {
            @Override
            public void onSuccess(final Order o) {
                order = o;
                Log.e("ORDER", order.toString());

                r.getCommandsByOrder(order.getId_order(), new RequestCallback<List<Command>>() {
                    @Override
                    public void onSuccess(final List<Command> response) {
                        System.out.println("Commands: " + response);

                        size = response.size();


                                for (final Command command : response) {
                                    if (o.state==-1){
                                        o.state = command.getState();
                                    }else{
                                        if(o.state!=command.getState()){
                                            order.setReady(false);
                                        }
                                    }
                                    r.getDishesByCommand(command.getId_command(), new RequestCallback<List<Dish>>() {
                                        @Override
                                        public void onSuccess(List<Dish> dishes) {
                                            System.out.println("Dishes: " + dishes);
                                            if (dishes.size() > 0) {
                                                dishesLists.put(dishes.get(0).getRestaurant().getId_restaurant(), dishes);
                                            }
                                            restaurants.add(dishes.get(0).getRestaurant());

                                            internal_cont++;
                                        }

                                        @Override
                                        public void onFailed(NetworkException e) {
                                            System.out.println("Failed" + e.getMessage());
                                        }
                                    });
                                }

                        System.out.println("Size: " + dishesLists.size());

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addDish();
                            }
                        });
                    }



                    @Override
                    public void onFailed(NetworkException e) {
                        e.printStackTrace();
                    }
                });
                /*getActivity().runOnUiThread(new Runnable() {
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
                });*/
            }

            @Override
            public void onFailed(NetworkException e) {
                e.printStackTrace();
            }
        });
    }

    private void addDish() {
        System.out.println(internal_cont!=size);
        if (internal_cont!=size){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            addDish();
        }else {
            hideDialog();
            System.out.println("Dishes final: "+dishesLists);
            TextView date = (TextView) view.findViewById(R.id.order_date);
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                Date newDate = format.parse(order.getCreation_date());
                format = new SimpleDateFormat("MMM dd,yyyy");
                date.setText(format.format(newDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            TextView state = (TextView) view.findViewById(R.id.order_state);

            state.setText(order.isReady()?"":"");

            TextView order_number = (TextView) view.findViewById(R.id.order_number);

            order_number.setText("ORDER #"+order.getId_order());

            int total_order = 0;

            for (Map.Entry<Integer, List<Dish>> entry: dishesLists.entrySet()){
                for(Dish d : entry.getValue()){
                    total_order+=d.getPrice();
                }
            }

            TextView history_resume_total = (TextView) view.findViewById(R.id.history_resume_total);

            history_resume_total.setText("$"+total_order);

            TextView order_state = (TextView) view.findViewById(R.id.order_state);

            if (!order.isReady()){
                order_state.setText("Pending");
            }else{
                //Todo: Alejandra
                order_state.setText(order.state==1?"Pendiente":order.state==2?"Lista para ser entregada":"Entregada");
            }

            HistoryListAdapter listAdapter = new HistoryListAdapter(getActivity(), new ArrayList<Restaurant>(restaurants), dishesLists, order);
            expandableListView.setAdapter(listAdapter);
        }
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


}
