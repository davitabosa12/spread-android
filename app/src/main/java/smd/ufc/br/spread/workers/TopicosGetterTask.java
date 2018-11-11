package smd.ufc.br.spread.workers;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import smd.ufc.br.spread.JSONArrayListener;
import smd.ufc.br.spread.R;
import smd.ufc.br.spread.net.NetworkConnect;

public class TopicosGetterTask extends AsyncTask<Void, Void, JSONArray> {

    Context ctx;
    JSONArrayListener callback;
    public TopicosGetterTask(Context context, JSONArrayListener callback){
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
        callback.doThis(jsonArray);
    }
}
