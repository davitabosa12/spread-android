package smd.ufc.br.spread.actions;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Request;
import com.google.android.gms.awareness.fence.FenceState;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import smd.ufc.br.spread.R;
import smd.ufc.br.spread.ResponseListener;
import smd.ufc.br.spread.net.NetworkConnect;
import smd.ufc.br.spread.utils.TokenUtil;

public class SaindoDoBlocoAction extends BroadcastReceiver implements ResponseListener<JSONObject> {
    private String TAG = "SaindoDoBlocoAction";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Receiving action");

            FenceState state = FenceState.extract(intent);
            switch (state.getCurrentState()){
                case FenceState.TRUE:
                    Log.d(TAG, "onReceive: Fence is true");
                    mandarNotificacao(context);
                    break;
                case FenceState.FALSE:
                    Log.d(TAG, "onReceive: Fence is false");
                    break;
                case FenceState.UNKNOWN:
                    Log.d(TAG, "onReceive: Fence is UNKNOWN");
                    break;

            }
        }

    private void mandarNotificacao(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Awareness");
        builder.setContentTitle("Você saiu do bloco");
        builder.setContentText("Sua presença foi atualizada no Spread");
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        builder.setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(new NotificationChannel("Awareness","Notificações Inteligentes",NotificationManager.IMPORTANCE_DEFAULT));
        }
        nm.notify(323, notification);

        //TODO: enviar requisicao de entrada de bloco ao servidor
        CheckoutTask mTask = new CheckoutTask(context, this);
        mTask.execute();

    }

    @Override
    public void doThis(JSONObject response) {
        if(response == null){
            Log.e(TAG, "doThis: response is null", new NullPointerException());
        } else {
            Log.d(TAG, "doThis: Enviado ao servidor com sucesso");
        }
    }

    class CheckoutTask extends AsyncTask<Void, Void, JSONObject> {
        Context mContext;
        ResponseListener<JSONObject> mListener;
        public CheckoutTask(Context context, ResponseListener<JSONObject> listener){
            mContext = context;
            mListener = listener;
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            TokenUtil util = new TokenUtil(mContext);
            String siape = util.getMatricula();
            if(siape == null){
                return null;
            }
            String url = mContext.getString(R.string.server_url) + "/api/" + siape + "/checkout";
            NetworkConnect connect = new NetworkConnect(mContext, Request.Method.POST, url, null);
            Future<JSONObject> future = connect.connect();
            try {
                return future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            mListener.doThis(jsonObject);
        }
    }
}
