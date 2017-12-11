package cosw.eci.edu.pancomido.ui.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.adapter.OrderHistoryAdapter;
import cosw.eci.edu.pancomido.data.adapter.RestaurantListAdapter;
import cosw.eci.edu.pancomido.data.model.Order;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.network.RequestCallback;
import cosw.eci.edu.pancomido.data.network.RetrofitNetwork;
import cosw.eci.edu.pancomido.exception.NetworkException;
import cosw.eci.edu.pancomido.misc.SessionManager;

public class OrderHistoryFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;

    private SessionManager sessionManager;


    public OrderHistoryFragment() {
    }


    public static OrderHistoryFragment newInstance(String param1, String param2) {
        OrderHistoryFragment fragment = new OrderHistoryFragment();

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

    private void getOrders() {
        showDialog();
        RetrofitNetwork retrofitNetwork = new RetrofitNetwork();
        RequestCallback<List<Order>> rc = new RequestCallback<List<Order>>() {
            @Override
            public void onSuccess(final List<Order> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        configureRecyclerView(response);
                        hideDialog();
                    }
                });
            }
            @Override
            public void onFailed(NetworkException e) {
            }
        };
        retrofitNetwork.getOrdersByUser(Integer.parseInt(sessionManager.getUserId()), rc);
    }

    private void configureRecyclerView(List<Order> orders){
        Collections.reverse(orders);
        recyclerView = (RecyclerView) getActivity().findViewById( R.id.orders_list );
        recyclerView.setHasFixedSize( true );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager( layoutManager );
        try {
            recyclerView.setAdapter(new OrderHistoryAdapter(orders));
        }catch(Exception e){
            System.out.println(e);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        sessionManager = new SessionManager(getActivity());
        getOrders();
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
