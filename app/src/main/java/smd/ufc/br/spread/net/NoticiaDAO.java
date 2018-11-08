package smd.ufc.br.spread.net;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import smd.ufc.br.spread.R;
import smd.ufc.br.spread.model.Noticia;

public class NoticiaDAO {
    Context context;
    String url;
    String TAG = "NoticiaDAO";
    public NoticiaDAO(Context context){
        this.context = context;
        url = context.getString(R.string.server_url) + "/api/noticia";
    }

    /**
     * Pega as ultimas noticias do servidor
     * @return uma lista de Noticias
     *
     */
    public List<Noticia> getNoticias(){
        List<Noticia> resp = new ArrayList<>();
        JSONObject fromServer = null;
        NetworkConnect connect = new NetworkConnect(context, Request.Method.GET, url, null);
        try{
            fromServer = connect.connect().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e(TAG, "getNoticias: ", e);
            Toast.makeText(context, "Houve um erro crítico!", Toast.LENGTH_SHORT).show();
            return null;

        } catch (ExecutionException e) {
            Throwable t = e.getCause();
            if(t instanceof TimeoutError){
                Toast.makeText(context, "Você está offline", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        if(fromServer != null){
            try {
                JSONArray array = fromServer.getJSONArray("noticias");
                for(int i = 0; i < array.length(); i++){
                    JSONObject fromArray = (JSONObject) array.get(i);
                    resp.add(parseNoticia(fromArray));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return resp;

    }

    private Noticia parseNoticia(JSONObject fromArray) {

        Noticia r = new Noticia();
        try {
            r.setTitulo(fromArray.getString("titulo"));
            r.setCorpo(fromArray.getString("corpo"));
            r.setTopico(fromArray.getString("topico"));
            //r.setTimestamp(new Date(fromArray.getString("timestamp")));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return r;

    }
}
