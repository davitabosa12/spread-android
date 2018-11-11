package smd.ufc.br.spread.utils;

import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Set;

public class TopicRefresher {
    Context context;
    String TAG = "TopicRefresher";
    public TopicRefresher(Context context){
        this.context = context;
    }
    public void refresh(){
        Log.d(TAG, "refresh: Starting refresh");
        TopicoPreferences prefs = new TopicoPreferences(context);
        Set<String> disp = prefs.getTopicosDisponiveis();
        Set<String> interesse = prefs.getTopicosInteresse();
        if(disp == null){
            return;
        }

        disp.removeAll(interesse);
        for(String topico : disp){
            Log.d(TAG, "refresh: Unsubscribing from " + topico);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topico);
        }

        for(String topico : interesse){
            Log.d(TAG, "refresh: Subscribing from " + topico);
            FirebaseMessaging.getInstance().subscribeToTopic(topico);
        }

    }
}
