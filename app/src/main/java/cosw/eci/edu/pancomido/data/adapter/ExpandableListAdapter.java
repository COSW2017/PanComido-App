package cosw.eci.edu.pancomido.data.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.listener.RestaurantsListener;
import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.misc.SessionManager;
import cosw.eci.edu.pancomido.ui.fragment.OrderDetailFragment;

/**
 * Created by Alejandra on 11/11/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Restaurant> restaurants;
    private HashMap<Integer, List<Dish>> dishesLists;
    private final RestaurantsListener restaurantsListener;


    public ExpandableListAdapter(Context context, RestaurantsListener listener, HashMap<Integer, List<Dish>> dishesLists){
        this.context = context;
        restaurantsListener = listener;
        this.restaurants = restaurantsListener.getRestaurants();
        this.dishesLists = dishesLists;
    }

    @Override
    public int getGroupCount() {
        return restaurants.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Restaurant restaurant = this.restaurants.get(groupPosition);
        List<Dish> restaurantDishes = this.dishesLists.get(restaurant.getId_restaurant());
        System.out.print(restaurantDishes == null);
        return this.dishesLists.get(this.restaurants.get(groupPosition).getId_restaurant()).size();
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
        Restaurant idRestaurant= restaurants.get(groupPosition);
        Dish dish = dishesLists.get(idRestaurant.getId_restaurant()).get(childPosition);
        SessionManager sessionManager = new SessionManager(context);
        TextView name = (TextView) convertView.findViewById(R.id.dish_name_row);
        TextView quanty = (TextView) convertView.findViewById(R.id.dish_quanty_row);
        TextView price= (TextView) convertView.findViewById(R.id.dish_price_row);
        name.setText(dish.getName());
        int price1 = dish.getPrice()*restaurantsListener.getDishQuanty(dish.getId_dish());
        price.setText("$ "+price1);
        quanty.setText(restaurantsListener.getDishQuanty(dish.getId_dish())+"");

        setActionsButtons(convertView, dish);
        return convertView;
    }

    private void setActionsButtons(View convertView, final Dish dish) {
        Button addButton = (Button) convertView.findViewById(R.id.dish_add_row);
        Button delButton = (Button) convertView.findViewById(R.id.dish_del_row);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantsListener.onAddDishToOrder(dish);
                addProduct();
            }
        });

        /*delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delProduct(groupPosition, childPosition, sessionManager, quanty, price);
                deleteProduct(groupPosition, childPosition);
            }
        });*/

    }

    private void addProduct() {
        this.notifyDataSetChanged();
    }

    private void deleteProduct(int position, int childPosition){
        /*int quanty1 = dishesQuanty.get(position).get(childPosition);
        quanty1--;
        if(quanty1>0){
            dishesQuanty.get(position).set(childPosition, quanty1);
        }else{
            dishesQuanty.get(position).remove(childPosition);
            dishesLists.get(position).remove(childPosition);
            if(dishesLists.size()<1){
                restaurants.remove(position);
            }
        }*/
        this.notifyDataSetChanged();
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
                            +","+dish.getRestaurant().getId_restaurant()+"", quanty1+"");
                }else {
                    map.remove(dish.getId_dish()+","+
                            dish.getRestaurant().getId_restaurant());
                }
                sessionManager.setDishes(gson.toJson(map));
            }
        }
        OrderDetailFragment.refreshPrice();
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
