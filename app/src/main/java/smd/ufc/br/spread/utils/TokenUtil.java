package smd.ufc.br.spread.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import smd.ufc.br.spread.workers.SendFCMTokenWorker;

/**
 * Classe TokenUtil
 * Camada de acesso ao SharedPreferences para pegar o token de autenticacao
 */
public class TokenUtil {
    private Context context;
    public static final String TAG = "TokenUtil";
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

    public String getMatricula(){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String token = sharedPref.getString("matricula", null);
        return token;
    }
    public String getFCMToken(){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String token = sharedPref.getString("fcmToken", null);
        return token;
    }
    public String getUserType(){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String token = sharedPref.getString("userType", null);
        return token;
    }

    public void setMatricula(String matricula){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("matricula", matricula);
        editor.apply();
    }
    public void setFCMToken(String fcmToken){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("fcmToken", fcmToken);
        //startTokenWorker();
        editor.apply();
    }

    private void startTokenWorker() {
        Log.d(TAG, "startTokenWorker: starting token worker...");
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SendFCMTokenWorker.class)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance().enqueue(workRequest);
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

    public void setUserType(String tipo) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("userType", tipo);
        editor.apply();
    }

    public void clear(){
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        if(sharedPref == null) return;
        sharedPref.edit()
                .remove("userType")
                .remove("name")
                .remove("password")
                .remove("login")
                .remove("authToken")
                .remove("matricula")
        .apply();

    }
}
