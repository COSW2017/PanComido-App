package cosw.eci.edu.pancomido.data.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.misc.loadImage;

/**
 * Created by Alejandra on 4/11/2017.
 */

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.viewHolder>{

    private final List<Dish> dishes;

    public DishAdapter(List<Dish> dishes){
        this.dishes = dishes;
    }


    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dish_row, parent, false);

        return  new DishAdapter.viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DishAdapter.viewHolder holder, int position) {
        Dish dish = dishes.get(position);
        holder.setDishImage(dish.getImage());
        holder.setName(dish.getName());
        holder.setPrice(dish.getPrice()+"");
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView price;

        public viewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.dish_image);
            name = (TextView) itemView.findViewById(R.id.dish_name);
            price = (TextView) itemView.findViewById(R.id.dish_price);
        }

        public void setDishImage(String url) {
            new loadImage(image).execute(url);

        }

        public void setName(String name){
            this.name.setText(name);
        }

        public void setPrice(String price){
            this.price.setText("$ "+price);
        }


    }
}
