package smd.ufc.br.spread.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ProfessorPreferences {
    private Context context;
    public static final String TAG = "TokenUtil";
    public static String PREF_FILE_NAME = "smd.ufc.br.spread.professor";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public ProfessorPreferences(Context context){
        this.context = context;
        sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void setAwarenessEnabled(boolean b){
        editor.putBoolean("awarenessEnabled", b);
        editor.apply();
    }
    public boolean isAwarenessEnabled(){
        return sharedPref.getBoolean("awarenessEnabled", false);
    }

}
