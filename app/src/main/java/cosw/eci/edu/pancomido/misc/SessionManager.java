package cosw.eci.edu.pancomido.misc;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alejandra on 31/10/2017.
 */

public class SessionManager {

    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "AndroidHivePref";

    private static final String TOKEN = "token";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "long";

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

    public Boolean isLoggedIn(){
        String token = pref.getString(TOKEN, "");
        return !token.isEmpty();
    }

    public String getToken(){
        return pref.getString(TOKEN, "");
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
}
