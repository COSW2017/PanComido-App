package cosw.eci.edu.pancomido.data.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.misc.loadImage;
import cosw.eci.edu.pancomido.ui.activity.MainActivity;
import cosw.eci.edu.pancomido.ui.fragment.RestaurantFragment;

/**
 * Created by estudiante on 3/9/17.
 */

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.viewHolder> {

    private List<Restaurant> restaurants;

    public RestaurantListAdapter(List<Restaurant> t){
        restaurants = t;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_list, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final Restaurant r = restaurants.get(position);
        holder.setName(r.getName());
        holder.setRestaurantImage(r.getImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RestaurantFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name",r.getName());
                fragment.setArguments(bundle);
                FragmentManager manager = ((Activity) v.getContext()).getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("lista").commit();
                Toast.makeText(v.getContext(), "", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        ImageView restaurantImage;
        TextView name;
        ImageView row;

        public viewHolder(View itemView) {
            super(itemView);
            restaurantImage = (ImageView) itemView.findViewById(R.id.restaurantImage);
            name = (TextView) itemView.findViewById(R.id.name);
            row = (ImageView) itemView.findViewById(R.id.row);
            row.setImageResource(R.drawable.ic_keyboard_arrow_right);
        }

        public ImageView getRestaurantImage() {
            return restaurantImage;
        }

        public void setRestaurantImage(String url) {
            new loadImage(restaurantImage).execute(url);

        }

        public TextView getName() {
            return name;
        }

        public void setName(String name) {
            this.name.setText(name);
        }

        public ImageView getRow() {
            return row;
        }

        public void setRow(ImageView row) {
            this.row = row;
        }
    }
}
