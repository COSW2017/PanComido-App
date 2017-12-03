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
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.restaurant_row_item, null);
        }
        Restaurant idRestaurant= restaurants.get(groupPosition);
        Dish dish = dishesLists.get(idRestaurant.getId_restaurant()).get(childPosition);
        TextView name = (TextView) convertView.findViewById(R.id.dish_name_row);
        TextView quanty = (TextView) convertView.findViewById(R.id.dish_quanty_row);
        TextView price= (TextView) convertView.findViewById(R.id.dish_price_row);
        name.setText(dish.getName());
        int price1 = dish.getPrice()*restaurantsListener.getDishQuanty(dish.getId_dish());
        price.setText("$ "+price1);
        quanty.setText(restaurantsListener.getDishQuanty(dish.getId_dish())+"");
        setActionsButtons(convertView, dish, childPosition, groupPosition);
        return convertView;
    }

    private void setActionsButtons(View convertView, final Dish dish, final int childPosition, final int groupPosition) {
        Button addButton = (Button) convertView.findViewById(R.id.dish_add_row);
        Button delButton = (Button) convertView.findViewById(R.id.dish_del_row);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantsListener.onAddDishToOrder(dish);
                addProduct();
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               deleteProduct(dish, childPosition, groupPosition);
            }
        });

    }

    private void addProduct() {
        this.notifyDataSetChanged();
        OrderDetailFragment.refreshPrice();
    }

    private void deleteProduct(Dish dish, int childPosition, int groupPosition){
        restaurantsListener.deleteDishFromOrder(dish.getId_dish());
        if(restaurantsListener.getDishQuanty(dish.getId_dish())<1){
            dishesLists.get(dish.getRestaurant().getId_restaurant()).remove(childPosition);
        }
        if(dishesLists.get(dish.getRestaurant().getId_restaurant()).size()<1){
            dishesLists.remove(dish.getRestaurant().getId_restaurant());
            restaurants.remove(groupPosition);
        }
        this.notifyDataSetChanged();
        OrderDetailFragment.refreshPrice();
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
