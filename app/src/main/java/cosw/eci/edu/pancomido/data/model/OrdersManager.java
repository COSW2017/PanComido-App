package cosw.eci.edu.pancomido.data.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Santiago Carrillo
 */

public class OrdersManager
{

    private List<Dish> orderDishes = new ArrayList<>();


    public void addDish( Dish dish ){
        orderDishes.add( dish );
    }

    public void delDish( int idDish){
        boolean found = false;
        Dish delDish = null;
        for (int i =0; i<orderDishes.size()&&!found; i++){
            if(orderDishes.get(i).getId_dish()==idDish){
                delDish=orderDishes.get(i);
                found=true;
            }
        }
        orderDishes.remove(delDish);
    }

    public int getDishesCount(){
        return orderDishes.size();
    }

    public int getDishCount(int dishId){
        int total = 0;
        for(Dish d : orderDishes){
            if(d.getId_dish()==dishId){
                total++;
            }
        }
        return total;
    }

    public int getOrderTotalPrice(){
        int total =0;
        for(Dish d : orderDishes){
            total+=d.getPrice();
        }
        return total;
    }

    public List<Restaurant> getRestaurants(){
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        for(Dish d : orderDishes){
            if(!hasRestaurant(d.getRestaurant().getId_restaurant(), restaurants)){
                restaurants.add(d.getRestaurant());
            }
        }
        return restaurants;
    }

    private boolean hasRestaurant(int idRestaurant, ArrayList<Restaurant> restaurants){
        boolean found =false;
        for(int i =0; i<restaurants.size() && !found; i++){
            found=idRestaurant==restaurants.get(i).getId_restaurant();
        }
        return found;
    }

    private boolean hasDish(int idDish, ArrayList<Dish> dishes){
        boolean found =false;
        for(int i =0; i<dishes.size() && !found; i++){
            found=idDish==dishes.get(i).getId_dish();
        }
        return found;
    }

    public List<Dish> getDishesByRestaurant(int restaurantId )
    {
        ArrayList<Dish> restauranDishes = new ArrayList<>();
        for(Dish d : orderDishes){
            if(!hasDish(d.getId_dish(), restauranDishes
            )){
                restauranDishes.add(d);
            }
        }
        return restauranDishes;
    }
}
