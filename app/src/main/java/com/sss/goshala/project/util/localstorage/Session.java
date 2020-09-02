package com.sss.goshala.project.util.localstorage;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    public Session(Context ctx){
        prefs = ctx.getSharedPreferences("Goshala", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.apply();
    }

    public void setLoggedin(boolean logggedin){
        editor.putBoolean("loggedInmode",logggedin);
        editor.commit();
    }

    public boolean loggedin(){ return prefs.getBoolean("loggedInmode", false); }

}
