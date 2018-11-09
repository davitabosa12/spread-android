package smd.ufc.br.spread.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import smd.ufc.br.spread.utils.TokenUtil;
import smd.ufc.br.spread.workers.SendFCMTokenWorker;

public class SpreadFirebaseMessagingService extends FirebaseMessagingService {
    public SpreadFirebaseMessagingService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(String s) {
        //save token to device
        TokenUtil util = new TokenUtil(this);
        util.setFCMToken(s);
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SendFCMTokenWorker.class)
                .setConstraints(constraints)
                .build();
        WorkManager.getInstance().enqueue(workRequest);
    }
}
