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

    public void setTopicosDisponiveis(Set<String> topicos){

        editor.putStringSet("topicos_disponiveis", topicos);
        editor.commit();

        //atualizar topicos de interesse, para nao dar conflito
        Set<String> interesse = getTopicosInteresse();
        if(interesse == null) return;

        interesse.retainAll(topicos);
        setTopicoInteresse(interesse);
    }

    public void setTopicoInteresse(Set<String> topicos){
        editor.putStringSet("topicos_interesse", topicos);
        editor.commit();
    }

    public Set<String> getTopicosDisponiveis(){
        return sharedPref.getStringSet("topicos_disponiveis", null);
    }
    public Set<String> getTopicosInteresse(){
        return sharedPref.getStringSet("topicos_interesse", null);
    }

    public void inscreverNoTopico(String topico){
        Set<String> interesse = sharedPref.getStringSet("topicos_interesse", null);
        if(interesse == null)
            interesse = new HashSet<>();

        if(interesse.contains(topico))
            return;
        else interesse.add(topico);
        setTopicoInteresse(interesse);
    }

    public void desinscreverNoTopico(String topico){
        Set<String> interesse = sharedPref.getStringSet("topicos_interesse", null);
        if(interesse == null) return;

        if(interesse.contains(topico))
            interesse.remove(topico);
        else return;
        setTopicoInteresse(interesse);
    }
}
