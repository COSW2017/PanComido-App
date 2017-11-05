package cosw.eci.edu.pancomido.ui.fragment;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.data.model.User;
import cosw.eci.edu.pancomido.data.network.RequestCallback;
import cosw.eci.edu.pancomido.data.network.RetrofitNetwork;
import cosw.eci.edu.pancomido.exception.NetworkException;
import cosw.eci.edu.pancomido.misc.SessionManager;
import cosw.eci.edu.pancomido.ui.activity.MainActivity;

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
    private Button cancel;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> dishes;
    private List<Dish> dishes_info;
    private int cant2;
    private int cant_friends;
    private ArrayList<boolean[]> booleans_final;
    private List<User> friends_list;
    private int total_dishes;

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

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, new PaymentSuccessFragment()).addToBackStack(null).commit();
            }
        });

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

        cancel = (Button) view.findViewById(R.id.cancel_button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().popBackStack();
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

        Gson gson = new Gson();
        SessionManager sessionManager = new SessionManager(getActivity());
        HashMap<String, String> map = gson.fromJson(sessionManager.getDishes(), HashMap.class);
        System.out.println(map);

        dishes = new ArrayList<>();
        dishes_info = new ArrayList<>();

        int size = map.size();

        cant2 = 0;

        total_dishes = 0;

        for(Map.Entry<String, String> entry : map.entrySet()){
            String[] key = entry.getKey().split(",");
            final Integer cant = Integer.parseInt(entry.getValue());
            Integer id_rest = Integer.parseInt(key[1]);
            Integer id_dish = Integer.parseInt(key[0]);

            final RetrofitNetwork r = new RetrofitNetwork();

            r.getDishById(id_rest, id_dish, new RequestCallback<Dish>() {
                @Override
                public void onSuccess(final Dish response) {
                    dishResponse(response, cant);
                }
                @Override
                public void onFailed(NetworkException e) {
                    e.printStackTrace();
                }
            });



        }

        while(cant2!=size){
            System.out.println("algo"); //NO BORRARRRRRR
        }

        final boolean[] booleans = new boolean[dishes.size()];

        final CharSequence[] dishes_final = new CharSequence[dishes.size()];

        booleans_final = new ArrayList<>();

        for(int i = 0 ; i < dishes.size(); i++){
            dishes_final[i] = dishes.get(i);

        }

        final HashMap<String, List<String>> mSelectedItems = new HashMap<>();


        friends.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                String item = (String)parent.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


                // Set the dialog title
                builder.setTitle("Select dish to pay")
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected

                        .setMultiChoiceItems(dishes_final, booleans_final.get(position), new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                String dish = dishes_info.get(which).getId_dish()+","+which;
                                String friend = friends_list.get(position).getUser_id() + "";
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items

                                    List<String> users_added = mSelectedItems.get(dish);
                                    if(users_added == null){
                                        users_added = new ArrayList<>();
                                    }
                                    if (!users_added.contains(friend)){
                                        users_added.add(friend);
                                    }
                                    mSelectedItems.put(dish, users_added);
                                    System.out.println(mSelectedItems);
                                } else if (mSelectedItems.get(dish)!=null) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.get(dish).remove(friend);
                                }
                                System.out.println(mSelectedItems);
                            }
                        })
                        // Set the action buttons
                        .setPositiveButton("OK", null);

                builder.show();
            }
        });
        return view;
    }

    private void dishResponse(Dish response, int cant) {

        for (int i = 0 ; i < cant ; i++){
            dishes.add(response.getName());
            dishes_info.add(response);
            total_dishes++;
        }
        cant2+=1;
    }

    private void configureAdapter(List<User> response) {
        friends_list = response;
        ArrayList<String> arrayList = new ArrayList<>();
        cant_friends = response.size();

        for(int i = 0 ; i < cant_friends; i++){
            booleans_final.add(new boolean[total_dishes]);
        }

        System.out.println("Booleans "+booleans_final);
        for (boolean[] b : booleans_final){
            System.out.println(Arrays.toString(b));
        }

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
