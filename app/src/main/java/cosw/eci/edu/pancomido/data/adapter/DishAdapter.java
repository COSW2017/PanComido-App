package cosw.eci.edu.pancomido.data.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
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

/**
 * Created by Alejandra on 4/11/2017.
 */

public class DishAdapter extends ArrayAdapter{

    private final Activity context;
    private final List<Dish> dishes;

    public DishAdapter(@NonNull Activity context, @LayoutRes int resource, List<Dish> dishes) {
        super(context, R.layout.dish_row);
        this.context = context;
        this.dishes = dishes;
    }

    public View getView(int posicion, View v, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.dish_row, null, true);

        ImageView dishImage = (ImageView)  rowView.findViewById(R.id.dish_image);
        TextView dishName = (TextView) rowView.findViewById(R.id.dish_name);
        TextView dishPrice = (TextView) rowView.findViewById(R.id.dish_price);

        dishName.setText(dishes.get(posicion).getName());
        dishPrice.setText(("$ "+dishes.get(posicion).getPrice()));

        return rowView;
    }
}
