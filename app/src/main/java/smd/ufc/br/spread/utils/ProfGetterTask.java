package smd.ufc.br.spread.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import smd.ufc.br.spread.R;
import smd.ufc.br.spread.ResponseListener;
import smd.ufc.br.spread.net.NetworkConnect;

public class ProfGetterTask extends AsyncTask<HashMap<String, String>, Void, JSONObject> {

    private String mMatricula;
    private String mCPF;
    private Context mContext;
    private ResponseListener<JSONObject> mListener;

    public ProfGetterTask(Context mContext, ResponseListener<JSONObject> mListener) {
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @Override
    protected JSONObject doInBackground(HashMap<String, String>... hashMaps) {
        HashMap<String, String> params = hashMaps[0];
        mMatricula = params.get("matricula");
        mCPF = params.get("cpf");
        String url = mContext.getString(R.string.server_url) + "/api/professor/" + mMatricula;

        NetworkConnect connect = new NetworkConnect(mContext, Request.Method.GET, url, null);
        Future<JSONObject> future = connect.connect();
        try {
            return future.get(30, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
    }
}
