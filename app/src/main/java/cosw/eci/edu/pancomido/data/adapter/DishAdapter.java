package cosw.eci.edu.pancomido.data.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import cosw.eci.edu.pancomido.R;
import cosw.eci.edu.pancomido.data.listener.RestaurantsListener;
import cosw.eci.edu.pancomido.data.model.Dish;
import cosw.eci.edu.pancomido.misc.SessionManager;

/**
 * Created by Alejandra on 4/11/2017.
 */

public class DishAdapter
        extends RecyclerView.Adapter<DishAdapter.viewHolder>
{

    private final List<Dish> dishes;


    private final RestaurantsListener restaurantsListener;

    public DishAdapter( List<Dish> dishes, @NonNull RestaurantsListener restaurantsListener )
    {
        this.dishes = dishes;
        this.restaurantsListener = restaurantsListener;
    }


    @Override
    public viewHolder onCreateViewHolder( ViewGroup parent, int viewType )
    {
        View itemView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.dish_row, parent, false );
        return new DishAdapter.viewHolder( itemView );
    }

    @Override
    public void onBindViewHolder( final DishAdapter.viewHolder holder, final int position )
    {
        final Dish dish = dishes.get( position );
        Picasso.with( holder.itemView.getContext() ).load( dish.getImage() ).into( holder.image );

        holder.setName( dish.getName() );
        holder.setPrice( dish.getPrice() + "" );
        holder.addQuantity.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick( View v )
            {
                restaurantsListener.onAddDishToOrder( dish );
            }
        } );
        /*holder.delQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delProduct(position, holder);
            }
        });*/
    }

    private void delProduct( int position, DishAdapter.viewHolder holder )
    {
        String json = holder.manager.getDishes();
        if ( !json.isEmpty() )
        {
            Gson gson = new Gson();
            HashMap<String, String> map = gson.fromJson( json, HashMap.class );
            if ( map.containsKey( dishes.get( position ).getId_dish() + "," + dishes.get(
                    position ).getRestaurant().getId_restaurant() ) )
            {
                holder.manager.setQ( holder.manager.getQ() < 1 ? 0 : holder.manager.getQ() - 1 );
                holder.manager.setPrice(
                        holder.manager.getPrice() < 1 ? 0 : holder.manager.getPrice() - dishes.get( position ).getPrice() );
                int quanty = Integer.parseInt( map.get( dishes.get( position ).getId_dish() + "," + dishes.get(
                        position ).getRestaurant().getId_restaurant() ) );
                if ( quanty > 1 )
                {
                    quanty -= 1;
                    map.put( dishes.get( position ).getId_dish() + "," + dishes.get(
                            position ).getRestaurant().getId_restaurant() + "", quanty + "" );
                }
                else
                {
                    map.remove( dishes.get( position ).getId_dish() + "," + dishes.get(
                            position ).getRestaurant().getId_restaurant() );
                }
                holder.manager.setDishes( gson.toJson( map ) );
            }
        }
        restaurantsListener.showMessage();
    }

    private void addDish( int position, DishAdapter.viewHolder holder )
    {
        String json = holder.manager.getDishes();
        holder.manager.setQ( holder.manager.getQ() + 1 );
        holder.manager.setPrice( holder.manager.getPrice() + dishes.get( position ).getPrice() );
        if ( !json.isEmpty() )
        {
            Gson gson = new Gson();
            HashMap<String, String> map = gson.fromJson( json, HashMap.class );
            if ( map.containsKey( dishes.get( position ).getId_dish() + "," + dishes.get(
                    position ).getRestaurant().getId_restaurant() ) )
            {
                int quanty = Integer.parseInt( map.get( dishes.get( position ).getId_dish() + "," + dishes.get(
                        position ).getRestaurant().getId_restaurant() ) );
                quanty += 1;
                map.put( dishes.get( position ).getId_dish() + "," + dishes.get(
                        position ).getRestaurant().getId_restaurant(), quanty + "" );
            }
            else
            {
                map.put( dishes.get( position ).getId_dish() + "," + dishes.get(
                        position ).getRestaurant().getId_restaurant(), "1" );
            }
            holder.manager.setDishes( gson.toJson( map ) );
        }
        else
        {
            HashMap<String, String> map = new HashMap<>();
            map.put(
                    dishes.get( position ).getId_dish() + "," + dishes.get( position ).getRestaurant().getId_restaurant()
                            + "", "1" );
            Gson gson = new Gson();
            String json1 = gson.toJson( map );
            Log.d( "JSON", json1 );
            holder.manager.setDishes( json1 );
        }

        Dish dish = dishes.get( position );

        restaurantsListener.showMessage();
    }

    @Override
    public int getItemCount()
    {
        return dishes.size();
    }

    class viewHolder
            extends RecyclerView.ViewHolder
    {

        ImageView image;

        TextView name;

        TextView price;

        TextView quantity;

        TextView quanty;

        TextView total;

        Button addQuantity;

        SessionManager manager;


        public viewHolder( View itemView )
        {
            super( itemView );
            image = (ImageView) itemView.findViewById( R.id.dish_image );
            name = (TextView) itemView.findViewById( R.id.dish_name );
            price = (TextView) itemView.findViewById( R.id.dish_price );
            addQuantity = (Button) itemView.findViewById( R.id.add_product );
            manager = new SessionManager( itemView.getContext() );
        }


        public void setName( String name )
        {
            this.name.setText( name );
        }

        public void setPrice( String price )
        {
            this.price.setText( "$ " + price );
        }

        public SessionManager getManager()
        {
            return manager;
        }

    }
}
