package cosw.eci.edu.pancomido.misc;

import android.content.Context;
import android.content.SharedPreferences;

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

    private static final String EMAIL = "email";

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

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }
}
