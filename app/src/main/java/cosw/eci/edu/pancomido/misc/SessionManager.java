package cosw.eci.edu.pancomido.misc;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cosw.eci.edu.pancomido.data.model.Command;
import cosw.eci.edu.pancomido.data.model.Command_Dish;

/**
 * Created by Alejandra on 31/10/2017.
 */

public class SessionManager {

    private static final String USER_ID = "user_id";
    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private Context _context;

    private int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AndroidHivePref";

    private static final String TOKEN = "token";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "long";
    private static final String ORDER = "order";
    private static final String COMMANDS = "command";
    private static final String DISHES = "dishes";
    private static final String Q = "quan";
    private static final String PRICE = "price";

    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String PHONE = "phone";

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String setToken(String token){
        editor.putString(TOKEN, token);
        editor.commit();
        return token;
    }

    public String setEmail(String email){
        editor.putString(EMAIL, email);
        editor.commit();
        return email;
    }

    public String setUserId(String user_id){
        editor.putString(USER_ID, user_id);
        editor.commit();
        return user_id;
    }

    public Boolean isLoggedIn(){
        String token = pref.getString(TOKEN, "");
        return !token.isEmpty();
    }

    public String getToken(){
        return pref.getString(TOKEN, "");
    }

    public String getEmail(){
        return pref.getString(EMAIL, "");
    }

    public String getUserId(){
        return pref.getString(USER_ID, "");
    }


    public void logoutUser(){
        editor.clear();
        editor.commit();
    }

    public void setLocation(String longitude, String latitude){
        editor.putString(LONGITUDE, longitude);
        editor.putString(LATITUDE, latitude);
        editor.commit();
    }

    public Map<String, String> getLocation(){
        Map<String, String> location = new HashMap<>();
        location.put(LONGITUDE, pref.getString(LONGITUDE, ""));
        location.put(LATITUDE, pref.getString(LATITUDE, ""));
        return location;
    }

    public Boolean location(){
        return !pref.getString(LONGITUDE, "").isEmpty();
    }

    public void initOrder(String order){
        editor.putString(ORDER, order);
        editor.commit();
    }

    public Boolean orderCreated(){
        return !pref.getString(ORDER, "").isEmpty();
    }

   public void setCommands(String commands){
       editor.putString(COMMANDS, commands);
       editor.commit();
   }

   public String getCommands(){
       return pref.getString(COMMANDS, "");
   }

    public void setDishes(String commands){
        editor.putString(DISHES, commands);
        Log.d("coooooom", commands);
        editor.commit();
    }

    public String getDishes(){
        return pref.getString(DISHES, "");
    }

    public void setQ(Integer q){
        editor.putInt(Q, q);
        editor.commit();
    }

    public int getQ(){
        return pref.getInt(Q, -1);
    }

    public void setPrice(Integer q){
        editor.putInt(PRICE, q);
        editor.commit();
    }

    public int getPrice(){
        return pref.getInt(PRICE, 0);
    }

    public String getPassword(){
        return pref.getString(PASSWORD, "");
    }

    public String setPassword(String password){
        editor.putString(PASSWORD, password);
        editor.commit();
        return password;
    }

    public String getPhone(){
        return pref.getString(PHONE, "");
    }

    public String setPhone(String phone){
        editor.putString(PHONE, phone);
        editor.commit();
        return phone;
    }

}
