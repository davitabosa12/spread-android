package smd.ufc.br.spread.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import smd.ufc.br.spread.ResponseListener;
import smd.ufc.br.spread.R;
import smd.ufc.br.spread.model.Noticia;
import smd.ufc.br.spread.net.NetworkConnect;

public class NoticiaGetterTask extends AsyncTask<Void, Void, JSONObject> {

    private static final String TAG = "NoticiaGetterTask";
    ResponseListener<List<Noticia>> callback;
    Context context;
    public NoticiaGetterTask(Context context, ResponseListener<List<Noticia>> callback){
        this.callback = callback;
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        String url = context.getString(R.string.server_url) + "/api/noticia";
        NetworkConnect connect = new NetworkConnect(context, Request.Method.GET, url, null);
        try {
            JSONObject response = connect.connectCached().get(5000, TimeUnit.MILLISECONDS);
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (TimeoutException e) {
            e.printStackTrace();
            //Toast.makeText(this, "Demorou demais!!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "doInBackground: Demorou demais!", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        if(response != null){
            JSONArray array = null;
            try {
                array = response.getJSONArray("noticias");
                List<Noticia> noticias = new ArrayList<>();
                for(int i = 0; i < array.length(); i++){
                    Noticia noticia = new Noticia(array.getJSONObject(i));
                    noticias.add(noticia);


                }
                callback.doThis(noticias);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
