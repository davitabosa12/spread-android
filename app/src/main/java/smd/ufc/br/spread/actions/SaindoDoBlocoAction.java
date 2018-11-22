package smd.ufc.br.spread.actions;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;

import smd.ufc.br.spread.R;

public class SaindoDoBlocoAction extends BroadcastReceiver {
    private String TAG = "SaindoDoBlocoAction";

    @Override
    public void onReceive(Context context, Intent intent) {

            FenceState state = FenceState.extract(intent);
            switch (state.getCurrentState()){
                case FenceState.TRUE:
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
        builder.setContentTitle("Você entrou no bloco");
        builder.setContentText("Uma mensagem foi enviada para os alunos interessados.");
        builder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        builder.setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(323, notification);

        //TODO: enviar requisicao de entrada de bloco ao servidor
    }
}
