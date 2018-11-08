package smd.ufc.br.spread.services;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import smd.ufc.br.spread.utils.TokenUtil;
import smd.ufc.br.spread.workers.SendFCMTokenWorker;


public class TokenRefresherService extends FirebaseInstanceIdService {
    private final String TAG = "TokenRefresherService";
    public TokenRefresherService() {
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);

        super.onTokenRefresh();
    }

    private void sendRegistrationToServer(String refreshedToken) {
        TokenUtil util = new TokenUtil(this);
        String auth = util.getAuthToken();
        if(auth == null){
            return;
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "192.168.25.9:3000/users/tokenUpdate";
        JSONObject params = new JSONObject();
        try {
            params.put("auth_token", auth);
            params.put("fcm_token", refreshedToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String status = null;
                try {
                    status = response.getString("status");
                    if (status.equals("ok")) {
                        Log.d(TAG, "onResponse: Sent to Server");
                    } else {
                        Log.e(TAG, "onResponse: Failed!");
                        Log.e(TAG, response.getString("cause"));
                        Log.d(TAG, "onResponse: Enqueuing SendFCMTokenWorker");
                        //Worker que tenta esta operacao de novo
                        Constraints constraints = new Constraints.Builder()
                                .setRequiredNetworkType(NetworkType.CONNECTED)
                                .build();
                        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SendFCMTokenWorker.class)
                                .setConstraints(constraints)
                                .build();
                        WorkManager.getInstance().enqueue(workRequest);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ERROR", error);
            }
        });
        queue.add(request);
    }
}
