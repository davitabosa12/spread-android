package smd.ufc.br.spread.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

public class TopicoPreferences {
    private Context context;
    public static final String TAG = "TokenUtil";
    public static String PREF_FILE_NAME = "smd.ufc.br.spread";
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public TopicoPreferences(Context context){
        this.context = context;
        sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void setTopicosDisponiveis(HashSet<String> topicos){

        editor.putStringSet("topicos_disponiveis", topicos);
        editor.commit();
    }

    public void setTopicoInteresse(HashSet<String> topicos){
        editor.putStringSet("topicos_interesse", topicos);
        editor.commit();
    }

    public Set<String> getTopicosDisponiveis(){
        return sharedPref.getStringSet("topicos_disponiveis", null);
    }
    public Set<String> getTopicosInteresse(){
        return sharedPref.getStringSet("topicos_interesse", null);
    }
}
