package cosw.eci.edu.pancomido.data.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.misc.SessionManager;

/**
 * Created by Alejandra on 11/11/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Restaurant> restaurants;
    private HashMap<Integer, List<Dish>> dishesLists;
    private HashMap<Integer, List<Integer>> dishesQuanty;


    public ExpandableListAdapter(Context context, List<Restaurant> restaurants, HashMap<Integer, List<Dish>> dishesLists,
                                 HashMap<Integer, List<Integer>>  dishesQuanty){
        this.context = context;
        this.restaurants = restaurants;
        this.dishesLists = dishesLists;
        this.dishesQuanty = dishesQuanty;
    }

    @Override
    public int getGroupCount() {
        return restaurants.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.dishesLists.get(restaurants.get(groupPosition).getId_restaurant()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.restaurants.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.dishesLists.get(restaurants.get(groupPosition).getId_restaurant()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = restaurants.get(groupPosition).getName();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.restaurant_row_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.restaurant_header);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.restaurant_row_item, null);
        }

        SessionManager sessionManager = new SessionManager(context);

        TextView name = (TextView) convertView.findViewById(R.id.dish_name_row);
        TextView quanty = (TextView) convertView.findViewById(R.id.dish_quanty_row);
        TextView price= (TextView) convertView.findViewById(R.id.dish_price_row);
        int idRestaurant= restaurants.get(groupPosition).getId_restaurant();
        name.setText(dishesLists.get(idRestaurant).get(childPosition).getName());
        int price1 = (dishesLists.get(idRestaurant).get(childPosition).
                getPrice()*dishesQuanty.get(idRestaurant).get(childPosition));
        price.setText("$ "+price1);
        quanty.setText(dishesQuanty.get(idRestaurant).get(childPosition)+"");

        setActionsButtons(convertView, sessionManager, restaurants.get(groupPosition).getId_restaurant(),
                childPosition, quanty, price);
        return convertView;
    }

    private void setActionsButtons(View convertView, final SessionManager sessionManager,
                                   final int groupPosition,
                                   final int childPosition, final TextView quanty, final TextView price) {
        Button addButton = (Button) convertView.findViewById(R.id.dish_add_row);
        Button delButton = (Button) convertView.findViewById(R.id.dish_del_row);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct(groupPosition, childPosition, sessionManager, quanty, price);
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delProduct(groupPosition, childPosition, sessionManager, quanty, price);
            }
        });

    }

    private void addProduct(int position, int childPosition, SessionManager sessionManager, TextView quanty, TextView price) {
        String json = sessionManager.getDishes();
        sessionManager.setQ(sessionManager.getQ()+1);
        Dish dish = dishesLists.get(position).get(childPosition);
        sessionManager.setPrice(sessionManager.getPrice()+dish.getPrice());
        if(!json.isEmpty()){
            Gson gson = new Gson();
            HashMap<String, String> map = gson.fromJson(json, HashMap.class);
            if(map.containsKey(dish.getId_dish()+
                    ","+dish.getRestaurant().getId_restaurant())){
                int quanty1 = Integer.parseInt(map.get(dish.getId_dish()+
                        ","+dish.getRestaurant().getId_restaurant()));
                quanty1+=1;
                map.put(dish.getId_dish()
                        +","+dish.getRestaurant().getId_restaurant(), quanty1+"");
            }else{
                map.put(dish.getId_dish()
                        +","+dish.getRestaurant().getId_restaurant(), "1");
            }
            sessionManager.setDishes(gson.toJson(map));
        }else {
            HashMap<String, String> map = new HashMap<>();
            map.put(dish.getId_dish()+
                    ","+dish.getRestaurant().getId_restaurant()+"", "1");
            Gson gson = new Gson();
            String json1 = gson.toJson(map);
            Log.d("JSON", json1);
            sessionManager.setDishes(json1);
        }
        quanty.setText((Integer.parseInt(quanty.getText().toString())+1)+"");
        price.setText((Integer.parseInt(quanty.getText().toString())*dish.getPrice())+"");
    }


    private void delProduct(int position, int childPosition, SessionManager sessionManager,  TextView quanty, TextView price) {
        String json = sessionManager.getDishes();
        Dish dish = dishesLists.get(position).get(childPosition);
        if(!json.isEmpty()){
            Gson gson = new Gson();
            HashMap<String, String> map = gson.fromJson(json, HashMap.class);
            if(map.containsKey(dish.getId_dish()+
                    ","+dish.getRestaurant().getId_restaurant())){
                sessionManager.setQ(sessionManager.getQ()<1 ? 0 : sessionManager.getQ()-1);
                sessionManager.setPrice(sessionManager.getPrice()<1 ? 0 : sessionManager.getPrice()-
                        dish.getPrice());
                int quanty1 = Integer.parseInt(map.get(dish.getId_dish()+
                        ","+dish.getRestaurant().getId_restaurant()));
                if(quanty1 > 1){
                    quanty1-=1;
                    map.put(dish.getId_dish()
                            +","+dish.getRestaurant().getId_restaurant()+"", quanty+"");
                }else {
                    map.remove(dish.getId_dish()+","+
                            dish.getRestaurant().getId_restaurant());
                }
                sessionManager.setDishes(gson.toJson(map));
            }
        }
        quanty.setText((Integer.parseInt(quanty.getText().toString())-1)+"");
        price.setText((Integer.parseInt(quanty.getText().toString())*dish.getPrice())+"");
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
