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
import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.network.RequestCallback;
import cosw.eci.edu.pancomido.data.network.RetrofitNetwork;
import cosw.eci.edu.pancomido.exception.NetworkException;
import cosw.eci.edu.pancomido.misc.SessionManager;


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
    private static SessionManager sessionManager;


    private OnFragmentInteractionListener mListener;

    public OrderDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderDetailFragment newInstance(String param1, String param2) {
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
        sessionManager = new SessionManager(getActivity());
        goPay = (Button) view.findViewById(R.id.goToPay);
        goPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPay();
            }
        });
        expandableListView = (ExpandableListView) view.findViewById(R.id.list_commands);
        totalOrder = (TextView) view.findViewById(R.id.resume_total);
        totalOrder.setText("Total: $"+sessionManager.getPrice()+"");
        restaurants = new ArrayList<>();
        dishesLists = new HashMap<>();
        dishesQuanty = new HashMap<>();
        getDishes();
        return view;
    }

    private void getDishes() {
        Gson gson = new Gson();
        map = gson.fromJson(sessionManager.getDishes(), HashMap.class);
        q = map.size();
        for(Map.Entry<String, String> entry : map.entrySet()){
            String[] key = entry.getKey().split(",");
            final Integer cant = Integer.parseInt(entry.getValue());
            Integer id_rest = Integer.parseInt(key[1]);
            Integer id_dish = Integer.parseInt(key[0]);
            final RetrofitNetwork r = new RetrofitNetwork();
            r.getDishById(id_rest, id_dish, new RequestCallback<Dish>() {
                @Override
                public void onSuccess(final Dish response) {
                    Boolean hasKey =dishesLists.containsKey(response.getRestaurant().getId_restaurant());
                    if(!hasKey){
                        restaurants.add(response.getRestaurant());
                        dishesLists.put(response.getRestaurant().getId_restaurant(), new ArrayList<Dish>());
                        dishesQuanty.put(response.getRestaurant().getId_restaurant(), new ArrayList<Integer>());
                    }
                    dishesLists.get(response.getRestaurant().getId_restaurant()).add(response);
                    dishesQuanty.get(response.getRestaurant().getId_restaurant()).add(cant);
                    synchronized (q){
                        q-=1;
                        if(q==0){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ExpandableListAdapter listAdapter = new ExpandableListAdapter(getActivity(), restaurants, dishesLists, dishesQuanty);
                                    expandableListView.setAdapter(listAdapter);
                                }
                            });

                        }
                    }
                }
                @Override
                public void onFailed(NetworkException e) {
                    e.printStackTrace();
                }
            });
        }
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

    public static void refreshPrice(){
        totalOrder.setText("Total: $"+sessionManager.getPrice()+"");
    }
}
