package smd.ufc.br.spread.workers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.RequestFuture;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import androidx.work.Worker;
import smd.ufc.br.spread.R;
import smd.ufc.br.spread.net.NetworkConnect;
import smd.ufc.br.spread.utils.TokenUtil;

public class SendFCMTokenWorker extends Worker {
    private final String TAG = "SendFCMTokenWorker";
    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: Started Working");
        Context context = getApplicationContext();
        String auth_token = new TokenUtil(context).getAuthToken();
        String fcm_token = new TokenUtil(context).getFCMToken();
        Log.d(TAG, "doWork: fcm_token is " + fcm_token);
        String matricula = new TokenUtil(context).getMatricula();
        String url = context.getResources().getString(R.string.server_url) + "/api/aluno/"+ matricula + "/tokenUpdate";
        JSONObject response = null;
        JSONObject params = new JSONObject();
        try{
            params.put("auth_token", auth_token);
            params.put("fcm_token", fcm_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkConnect connect = new NetworkConnect(context, Request.Method.POST, url, params);
        RequestFuture<JSONObject> future = connect.connect();
        try {
            response = future.get();
        } catch (InterruptedException e) {
            //erro critico
            e.printStackTrace();
            Log.e(TAG, "doWork: Erro critico", e);
            return Result.RETRY;
        } catch (ExecutionException e) {
            //algum erro de autenticacao, etc..
            Throwable t = e.getCause();
            if( t instanceof AuthFailureError){
                Log.e(TAG, "doWork: Erro de autenticacao", e);
                //TODO: Atualizar token de autenticacao.
                return Result.FAILURE;
            } else if( t instanceof TimeoutError){
                //servidor offline ou url errada, tente de novo
                Log.e(TAG, "doWork: Servidor offline ou URL errada", e);
                return Result.RETRY;
            }
            return Result.RETRY;
        }
        Log.d(TAG, "doWork: Success!");
        Log.d(TAG, "doWork: response -->>" + response.toString());
        return Result.SUCCESS;


    }
}
