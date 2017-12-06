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

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.model.Order;
import cosw.eci.edu.pancomido.data.model.Restaurant;
import cosw.eci.edu.pancomido.ui.fragment.OrderHistoryDetailFragment;
import cosw.eci.edu.pancomido.ui.fragment.RestaurantFragment;

/**
 * Created by estudiante on 3/9/17.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.viewHolder> {

    private List<Order> orders;

    public OrderHistoryAdapter(List<Order> t){
        orders = t;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_list, parent, false);

        return new viewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        final Order r = orders.get(position);
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date newDate = format.parse(r.getCreation_date());

            format = new SimpleDateFormat("MMM dd,yyyy");
            String date = format.format(newDate);;
            holder.setName(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new OrderHistoryDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id_order",r.getId_order());
                fragment.setArguments(bundle);
                FragmentManager manager = ((Activity) v.getContext()).getFragmentManager();
                manager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("lista").commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView row;

        public viewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            row = (ImageView) itemView.findViewById(R.id.row);
            row.setImageResource(R.drawable.ic_keyboard_arrow_right);
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
