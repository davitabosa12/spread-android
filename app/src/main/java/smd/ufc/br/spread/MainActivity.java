package smd.ufc.br.spread;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import smd.ufc.br.spread.fragments.AlteracoesFragment;
import smd.ufc.br.spread.fragments.CheckInProfFragment;
import smd.ufc.br.spread.fragments.LaboratorioFragment;
import smd.ufc.br.spread.fragments.NoticiasFragment;
import smd.ufc.br.spread.fragments.NotificacoesFragment;
import smd.ufc.br.spread.fragments.RequisicoesFragment;
import smd.ufc.br.spread.model.Topico;
import smd.ufc.br.spread.utils.TokenUtil;
import smd.ufc.br.spread.utils.TopicoPreferences;
import smd.ufc.br.spread.workers.TopicosGetterTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ResponseListener<List<Topico>> {
    private static final String TAG = "MainActivity";
    Button btnLab, btnNoticia, btnRequisicao, btnLogin;
    NavigationView navigationView;
    private static final int LOGIN_REQUEST_CODE = 401;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(userHasLogin()){
            changeUILogin();
        } else {

        }
        dataSync(); //todo: colocar em userHasLogin();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new NoticiasFragment())
                .commit();
        navigationView.setCheckedItem(R.id.nav_noticias);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == LOGIN_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                changeUILogin();
            }
        }
    }

    private void changeUILogin() {

        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.user_name);
        TextView userEmail = headerView.findViewById(R.id.user_email);
        TokenUtil tokenUtil = new TokenUtil(getApplicationContext());

        userName.setText(tokenUtil.getName());
        userEmail.setText(tokenUtil.getLogin());
    }

    private boolean userHasLogin(){
        TokenUtil util = new TokenUtil(this);
        String login = util.getLogin();
        return !(login == null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void dataSync(){
    Log.d(TAG, "dataSync: Init datasync");
    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
        @Override
        public void onSuccess(InstanceIdResult instanceIdResult) {
            String newToken = instanceIdResult.getToken();
            Log.d(TAG, "onSuccess: NEW TOKEN! ----->" + newToken);
            new TokenUtil(getApplicationContext()).setFCMToken(newToken);
        }
    });

    TopicosGetterTask task = new TopicosGetterTask(this,this);
            TopicoPreferences prefs = new TopicoPreferences(getApplicationContext());

    task.execute();
    ouvirTopicos();
}

    private void ouvirTopicos() {
        TopicoPreferences prefs = new TopicoPreferences(this);
        Set<String> disp = prefs.getTopicosDisponiveis();
        Set<String> interesse = prefs.getTopicosInteresse();
        if(disp == null){
            return;
        }
        if(interesse != null || true){
            //disp.removeAll(interesse);
            for(final String topico : disp) //TODO: MUDAR PARA OS INTERESSES DO USUARIO
                FirebaseMessaging.getInstance().subscribeToTopic(topico).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Topico " + topico + " inscrito com sucesso");
                        } else {
                            Log.e(TAG, "onComplete: Falha ao se inscrever no topico " + topico, new Exception("DeuBodeException"));
                        }
                    }
                });
        }


        for(String topico : disp);
        //FirebaseMessaging.getInstance().unsubscribeFromTopic(topico);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }*/



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.nav_notificacoes:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new NotificacoesFragment())
                        .commit();
                break;
            case R.id.nav_alteracoes:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new AlteracoesFragment())
                        .commit();
                break;
            case R.id.nav_requisicoes:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new RequisicoesFragment())
                        .commit();
                break;
            case R.id.nav_check_in_professor:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new CheckInProfFragment())
                        .commit();
                break;
            case R.id.nav_laboratorio:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new LaboratorioFragment())
                        .commit();
                break;
            case R.id.nav_noticias:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new NoticiasFragment())
                        .commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void doThis(List<Topico> response) {
        TopicoPreferences prefs = new TopicoPreferences(this);
        if(!response.isEmpty()){
            HashSet<String> set;
            set = new HashSet<String>();
            for(Topico t : response){
                set.add(t.getNome());
            }
            prefs.setTopicosDisponiveis(set);
        }

    }
}
