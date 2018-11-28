package smd.ufc.br.spread.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.annotation.Nullable;
import smd.ufc.br.spread.R;
import smd.ufc.br.spread.ResponseListener;
import smd.ufc.br.spread.net.NetworkConnect;

public class AlunoGetterTask extends AsyncTask<HashMap<String, String>, Void, JSONObject> {

    private String mMatricula;
    private String mCPF;
    private Context mContext;
    private ResponseListener<JSONObject> mListener;

    public AlunoGetterTask(Context context, ResponseListener<JSONObject> listener){
        mContext = context;
        mListener = listener;
    }
    @Override
    protected JSONObject doInBackground(HashMap<String, String>... hashMaps) {

        HashMap<String, String> params = hashMaps[0];
        mMatricula = params.get("matricula");
        mCPF = params.get("cpf");
        String url = mContext.getString(R.string.server_url) + "/api/aluno/" + mMatricula;

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
        mListener.doThis(jsonObject);
    }
}
