// Vị trí: com.example.fastfood.api/SessionManager.java
package com.example.fastfood.data.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.fastfood.activity.LoginActivity;

public class SessionManager {
    private static final String PREF_NAME = "FastFoodAppPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_TOKEN = "userToken";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context _context;

    public SessionManager(Context context) {
        this._context = context;
        prefs = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }


    public void createLoginSession(String token) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_TOKEN, token);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }
}