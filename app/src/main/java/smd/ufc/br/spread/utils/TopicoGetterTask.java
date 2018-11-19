package smd.ufc.br.spread.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import smd.ufc.br.spread.R;
import smd.ufc.br.spread.ResponseListener;
import smd.ufc.br.spread.model.Laboratorio;
import smd.ufc.br.spread.model.Topico;
import smd.ufc.br.spread.net.NetworkConnect;

public class TopicoGetterTask extends AsyncTask<Void, Void, JSONObject> {

    private ResponseListener<List<Topico>> callback;
    private Context context;

    public TopicoGetterTask(Context context, ResponseListener<List<Topico>> callback) {
        this.callback = callback;
        this.context = context;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        String url = context.getString(R.string.server_url) + "/api/topico";
        NetworkConnect connect = new NetworkConnect(context, Request.Method.GET, url, null);

        JSONObject resp = null;
        try {
            resp = connect.connectCached().get(10, TimeUnit.SECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return resp;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        List<Topico> topicos = new ArrayList<>();
        try {
            topicos = Topico.fromJSONArray(jsonObject.getJSONArray("topicos"));

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        callback.doThis(topicos);
    }
}
