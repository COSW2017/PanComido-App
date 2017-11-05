package cosw.eci.edu.pancomido.data.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.misc.loadImage;

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
        Restaurant r = restaurants.get(position);
        holder.setName(r.getName());
        holder.setRestaurantImage(r.getImage());
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        ImageView restaurantImage;
        TextView name;

        public viewHolder(View itemView) {
            super(itemView);
            restaurantImage = (ImageView) itemView.findViewById(R.id.restaurantImage);
            name = (TextView) itemView.findViewById(R.id.name);
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
    }
}
