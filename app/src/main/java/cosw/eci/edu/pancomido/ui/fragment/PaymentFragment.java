package cosw.eci.edu.pancomido.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.model.User;
import cosw.eci.edu.pancomido.data.network.RequestCallback;
import cosw.eci.edu.pancomido.data.network.RetrofitNetwork;
import cosw.eci.edu.pancomido.exception.NetworkException;
import cosw.eci.edu.pancomido.misc.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private OnFragmentInteractionListener mListener;
    private RadioButton iPay;
    private RadioButton friendsPay;
    private SearchView findFriends;
    private ListView friends;
    private Button pay;
    private ArrayAdapter<String> arrayAdapter;

    public PaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentFragment newInstance(String param1, String param2) {
        PaymentFragment fragment = new PaymentFragment();

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
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        iPay = (RadioButton) view.findViewById(R.id.i_pay);
        friendsPay = (RadioButton) view.findViewById(R.id.friend_pay);
        findFriends = (SearchView) view.findViewById(R.id.find_friends);
        friends = (ListView) view.findViewById(R.id.friend_list);
        pay = (Button) view.findViewById(R.id.pay_button);
        friendsPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    findFriends.setVisibility(View.VISIBLE);
                    friends.setVisibility(View.VISIBLE);
                    iPay.setChecked(false);
                }
            }
        });
        iPay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    findFriends.setVisibility(View.GONE);
                    friends.setVisibility(View.GONE);
                    friendsPay.setChecked(false);
                }
            }
        });

        RetrofitNetwork retrofitNetwork = new RetrofitNetwork();
        RequestCallback<List<User>> rc = new RequestCallback<List<User>>() {
            @Override
            public void onSuccess(final List<User> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        configureAdapter(response);
                    }
                });
            }

            @Override
            public void onFailed(NetworkException e) {

            }
        };
        retrofitNetwork.getFriends(new SessionManager(getActivity()).getEmail(), rc);


        findFriends.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Filter filter = arrayAdapter.getFilter();
                if (TextUtils.isEmpty(newText)) {
                    filter.filter("");
                } else {
                    filter.filter(newText);
                }
                return true;
            }
        });
        return view;
    }

    private void configureAdapter(List<User> response) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (User u : response){
            arrayList.add(u.getFirstname()+" "+u.getLastname());
        }

        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayList);
        friends.setAdapter(arrayAdapter);
        //friends.setTextFilterEnabled(true);

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
}
