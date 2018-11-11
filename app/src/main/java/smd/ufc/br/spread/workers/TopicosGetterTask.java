package smd.ufc.br.spread.workers;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import smd.ufc.br.spread.R;
import smd.ufc.br.spread.ResponseListener;
import smd.ufc.br.spread.model.Topico;
import smd.ufc.br.spread.net.NetworkConnect;

public class TopicosGetterTask extends AsyncTask<Void, Void, JSONArray> {

    Context ctx;
    ResponseListener<List<Topico>> callback;
    public TopicosGetterTask(Context context, ResponseListener<List<Topico>> callback){
        this.ctx = context;
        this.callback = callback;
    }
    @Override
    protected JSONArray doInBackground(Void... voids) {
        String url = ctx.getString(R.string.server_url) + "/api/topico";
        NetworkConnect connect = new NetworkConnect(ctx, Request.Method.GET, url, null );
        try {
            JSONObject future = connect.connect().get();
            return future.getJSONArray("topicos");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        super.onPostExecute(jsonArray);

        try {
            callback.doThis(Topico.fromJSONArray(jsonArray));
        } catch (JSONException e) {
            callback.doThis(null);
        }
    }
}
