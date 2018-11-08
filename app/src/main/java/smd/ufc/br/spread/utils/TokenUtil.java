package smd.ufc.br.spread.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Classe TokenUtil
 * Camada de acesso ao SharedPreferences para pegar o token de autenticacao
 */
public class TokenUtil {
    private Context context;
    public static String PREF_FILE_NAME = "smd.ufc.br.spread";


    public TokenUtil(Context context){
        this.context = context;
    }
    public String getAuthToken(){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String token = sharedPref.getString("authToken", null);
        return token;
    }
    public String getPassword() {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String token = sharedPref.getString("password", null);
        return token;
    }

    public String getLogin() {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String token = sharedPref.getString("login", null);
        return token;
    }

    public String getName(){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String token = sharedPref.getString("name", null);
        return token;
    }

    public void setAuthToken(String authToken){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("authToken", authToken);
        editor.apply();
    }
    public void setLogin(String login){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("login", login);
        editor.apply();
    }
    public void setPassword(String password){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("password", password);
        editor.apply();
    }

    public void setName(String name){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", name);
        editor.apply();
    }

}
