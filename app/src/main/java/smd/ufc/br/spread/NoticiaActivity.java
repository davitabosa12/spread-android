package smd.ufc.br.spread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import smd.ufc.br.spread.model.Noticia;
import smd.ufc.br.spread.net.NetworkConnect;
import smd.ufc.br.spread.net.NoticiaDAO;

public class NoticiaActivity extends AppCompatActivity {
    List<Noticia> noticias;
    String TAG = "NoticiaActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia);
        NoticiaDAO dao = new NoticiaDAO(this);
        noticias = dao.getNoticias();
        Iterator<Noticia> not = noticias.iterator();
        while(not.hasNext()){
            Noticia n = not.next();
            Log.d(TAG, "titulo: " + n.getTitulo());
            Log.d(TAG, "corpo: " + n.getCorpo());
            Log.d(TAG, "topico: " + n.getTopico());
        }

    }
}
