package smd.ufc.br.spread.workers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.concurrent.ExecutionException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.work.Worker;

import smd.ufc.br.spread.R;
import smd.ufc.br.spread.net.NetworkConnect;
import smd.ufc.br.spread.utils.TokenUtil;

public class UpdateAlunoWorker extends Worker {
    private final String TAG = "UpdateAlunoWorker"; 
    
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: Started working...");
        
        TokenUtil util = new TokenUtil(getApplicationContext());
        String nome = util.getName();
        String email = util.getLogin();
        JSONObject requestParams = new JSONObject();
        try {
            requestParams.put("nome", nome);
            requestParams.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
            return Result.RETRY;
        }
        String url = getApplicationContext().getString(R.string.server_url) + "/api/aluno/" + util.getMatricula() + "/update";
        NetworkConnect connect = new NetworkConnect(getApplicationContext(), Request.Method.POST, url, requestParams);
        try {
            JSONObject response = connect.connect().get(60, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "doWork: interrupted", e);
            return Result.RETRY;
        } catch (TimeoutException e) {
            Log.e(TAG, "doWork: Timeout!", e);
            return Result.RETRY;
        }
        Log.d(TAG, "doWork: Success!!");
        return Result.SUCCESS;


    }
}
