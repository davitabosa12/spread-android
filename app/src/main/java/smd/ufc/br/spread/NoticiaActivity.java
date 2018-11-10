package smd.ufc.br.spread;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import smd.ufc.br.spread.model.Noticia;
import smd.ufc.br.spread.net.NetworkConnect;
import smd.ufc.br.spread.net.NoticiaDAO;
import smd.ufc.br.spread.views.NoticiaView;

public class NoticiaActivity extends AppCompatActivity {
    List<Noticia> noticias;
    String TAG = "NoticiaActivity";
    NoticiaActivity.NoticiaGetterTask mTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);
        final LinearLayout rootView = findViewById(R.id.linearScroll);

        mTask = new NoticiaGetterTask(new JSONArrayListener() {
            @Override
            public void doThis(JSONArray response) {
                //inflate views
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject noticia = response.getJSONObject(i);
                        NoticiaView nv = new NoticiaView(rootView.getContext());
                        nv.setTitulo(noticia.getString("titulo"));
                        nv.setCorpo(noticia.getString("corpo"));
                        nv.setTopico(noticia.getString("topico"));
                        nv.setData(noticia.getString("timestamp"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }
        });
        mTask.execute((Void) null);





    }
    public class NoticiaGetterTask extends AsyncTask<Void, Void, JSONObject>{
        JSONArrayListener callback;
        public NoticiaGetterTask(){

        }

        public NoticiaGetterTask(JSONArrayListener callback){
            this.callback = callback;
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {
            String url = getString(R.string.server_url) + "/api/noticia";
            NetworkConnect connect = new NetworkConnect(getApplicationContext(), Request.Method.GET, url, null);
            try {
                JSONObject response = connect.connect().get(5000, TimeUnit.MILLISECONDS);
                return response;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            } catch (TimeoutException e) {
                e.printStackTrace();
                //Toast.makeText(this, "Demorou demais!!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "doInBackground: Demorou demais!", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            if(response != null){
                JSONArray array = null;
                try {
                    array = response.getJSONArray("noticias");
                    callback.doThis(array);
                    for(int i = 0; i < array.length(); i++){
                        JSONObject noticia = array.getJSONObject(i);

                        Log.d(TAG, "titulo: " + noticia.getString("titulo"));
                        Log.d(TAG, "corpo: " + noticia.getString("corpo"));
                        Log.d(TAG, "topico: " + noticia.getString("topico"));
                        Log.d(TAG, "timestamp: " + noticia.getString("timestamp"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
