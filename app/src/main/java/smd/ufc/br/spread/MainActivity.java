package smd.ufc.br.spread;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatDelegate;
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

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.FenceClient;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceQueryRequest;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import smd.ufc.br.spread.actions.NoBlocoAction;
import smd.ufc.br.spread.actions.SaindoDoBlocoAction;
import smd.ufc.br.spread.fragments.AlteracoesFragment;
import smd.ufc.br.spread.fragments.CheckInProfFragment;
import smd.ufc.br.spread.fragments.LaboratorioFragment;
import smd.ufc.br.spread.fragments.NoticiasFragment;
import smd.ufc.br.spread.fragments.NotificacoesFragment;
import smd.ufc.br.spread.fragments.RequisicoesFragment;
import smd.ufc.br.spread.model.Topico;
import smd.ufc.br.spread.utils.ProfessorPreferences;
import smd.ufc.br.spread.utils.TokenUtil;
import smd.ufc.br.spread.utils.TopicoPreferences;
import smd.ufc.br.spread.workers.TopicosGetterTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ResponseListener<List<Topico>>, NotificacoesFragment.OnFragmentDismissListener, MenuItem.OnMenuItemClickListener {
    private static final String TAG = "MainActivity";
    NavigationView navigationView;
    private static final int LOGIN_REQUEST_CODE = 401;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main2);


        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu toolbarMenu = toolbar.getMenu();



        if (userHasLogin()) {
            changeUILogin();
        } else {
            changeUINotLoggedIn();
        }
        dataSync(); //todo: colocar em userHasLogin();

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

    /**
     * Remove os itens do menu que necessitam de login para funcionar.
     */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                changeUILogin();
            }
        }
    }

    /*******************************************************************/
    private void changeUINotLoggedIn() {
        Menu menu = navigationView.getMenu();
        MenuItem notificacoes, requisicoes, alteracoes, loginMenu;
        notificacoes = menu.findItem(R.id.nav_notificacoes);
        requisicoes = menu.findItem(R.id.nav_requisicoes);
        alteracoes = menu.findItem(R.id.nav_alteracoes);
        loginMenu = menu.findItem(R.id.nav_login);

        notificacoes.setVisible(false);
        requisicoes.setVisible(false);
        alteracoes.setVisible(false);
        loginMenu.setVisible(true);
    }

    /*******************************************************************/
    private void changeUILogin() {


        View headerView = navigationView.getHeaderView(0);
        updateCredentials();
        TextView userName = headerView.findViewById(R.id.user_name);
        TextView userEmail = headerView.findViewById(R.id.user_email);
        TokenUtil tokenUtil = new TokenUtil(getApplicationContext());



        userName.setText(tokenUtil.getName());
        userEmail.setText(tokenUtil.getLogin());

        String mUserType = tokenUtil.getUserType();

        //checar se é professor
        if(mUserType != null && mUserType.equals("professor")){
            //mostrar coisas do professor!
            //tambem setar Awareness
            ProfessorPreferences profprefs = new ProfessorPreferences(this);
            if(profprefs.isAwarenessEnabled()){
                activateAwareness();
            } else {
                deactivateAwareness();
            }
        }


        Menu menu = navigationView.getMenu();


        MenuItem notificacoes, requisicoes, alteracoes, loginMenu;
        notificacoes = menu.findItem(R.id.nav_notificacoes);
        requisicoes = menu.findItem(R.id.nav_requisicoes);
        alteracoes = menu.findItem(R.id.nav_alteracoes);
        loginMenu = menu.findItem(R.id.nav_login);

        notificacoes.setVisible(true);
        requisicoes.setVisible(true);
        alteracoes.setVisible(true);
        loginMenu.setVisible(false);

    }

    /*******************************************************************/
    private void updateCredentials() {

    }

    /*******************************************************************/
    private boolean userHasLogin() {
        Intent dadosInit = getIntent();
        return dadosInit.getBooleanExtra("hasLogin", false);

        /*TokenUtil util = new TokenUtil(this);
        String login = util.getLogin();
        return !(login == null);*/
    }

    /*******************************************************************/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*******************************************************************/
    private void dataSync() {
        Log.d(TAG, "dataSync: Init datasync");
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.d(TAG, "onSuccess: NEW TOKEN! ----->" + newToken);
                new TokenUtil(getApplicationContext()).setFCMToken(newToken);
            }
        });
        /*******************************************************************/
        TopicosGetterTask task = new TopicosGetterTask(this, this);
        TopicoPreferences prefs = new TopicoPreferences(getApplicationContext());

        task.execute();
        ouvirTopicos();
    }

    /*******************************************************************/
    private void ouvirTopicos() {
        TopicoPreferences prefs = new TopicoPreferences(this);
        Set<String> disp = prefs.getTopicosDisponiveis();
        Set<String> interesse = prefs.getTopicosInteresse();

        if (disp == null) {
            return;
        }
        Set<String> semInteresse = new HashSet<>(disp);
        if (interesse == null)
            interesse = new HashSet<>(disp);
        else
            semInteresse.removeAll(interesse);


        for (final String topico : interesse)
            FirebaseMessaging.getInstance().subscribeToTopic(topico).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete: Topico " + topico + " inscrito com sucesso");
                    } else {
                        Log.e(TAG, "onComplete: Falha ao se inscrever no topico " + topico, new Exception("DeuBodeException"));
                    }
                }
            });


        for (final String topico : semInteresse) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topico).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                        Log.d(TAG, "onComplete: Topico " + topico + " removido com sucesso");
                    else
                        Log.e(TAG, "onComplete: Falha ao se desinscrever do topico " + topico, new Exception("DeuBodeException"));
                }
            });
        }

    }

    /*******************************************************************/
    private void atualizarTopicos() {

    }

    /*******************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        MenuItem logout = menu.findItem(R.id.menu_logout);
        logout.setVisible(userHasLogin());
        return true;
    }

    /*******************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.menu_logout:
                Log.d(TAG, "onOptionsItemSelected: performLogout");
                performLogout();
                break;
            default:
                Log.d(TAG, "onOptionsItemSelected: " + item.getTitle() + " pressionado!");
                break;
        }

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }*/
        /*******************************************************************/


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
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
            case R.id.nav_login:
                startActivityForResult(new Intent(this, AlunoProfessorLogin.class), LOGIN_REQUEST_CODE);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void doThis(List<Topico> response) {
        TopicoPreferences prefs = new TopicoPreferences(this);
        if (!response.isEmpty()) {
            HashSet<String> set;
            set = new HashSet<String>();
            for (Topico t : response) {
                set.add(t.getNome());
            }
            prefs.setTopicosDisponiveis(set);
            ouvirTopicos();
        }

    }

    public void activateAwareness() {
        double latitudeSMD = -3.7486777;
        double longitudeSMD = -38.5796101;
        double raio = 300; //300m de distancia
        long tempo = 5000; //cinco segundos de espera
        FenceClient client = Awareness.getFenceClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 321654);
            }
            return;
        }
        AwarenessFence noBloco = LocationFence.in(latitudeSMD, longitudeSMD, raio, tempo);
        AwarenessFence saindoDoBloco = LocationFence.exiting(latitudeSMD,longitudeSMD, raio);


        PendingIntent saindoDoBlocoPi = PendingIntent.getBroadcast(this, 0,
                new Intent(this, SaindoDoBlocoAction.class), 0);

        PendingIntent noBlocoPi = PendingIntent.getBroadcast(this, 0,
                new Intent(this, NoBlocoAction.class), 0);



        client.updateFences(new FenceUpdateRequest.Builder()
                .addFence("noBloco", noBloco,noBlocoPi)
                .addFence("saindoDoBloco", saindoDoBloco, saindoDoBlocoPi)
                .build())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Fences registradas com sucesso!");
                        } else {
                            Log.e(TAG, "onComplete: Nao foi possivel registrar fence", new Exception("oooo"));
                        }
                    }
                });
    }

    public void deactivateAwareness() {
        FenceClient client = Awareness.getFenceClient(this);
        client.updateFences(new FenceUpdateRequest.Builder()
                .removeFence("noBloco")
                .removeFence("saindoDoBloco")
                .build())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Fences removidas!");
                        } else {
                            Log.e(TAG, "onComplete: Nao foi possivel remover fence", new Exception("deu bode"));
                        }
                    }
                });
    }

    @Override
    public void fragmentDismissed(String fragmentName) {
        if(fragmentName.equals(NotificacoesFragment.TAG)){
            ProfessorPreferences professorPreferences = new ProfessorPreferences(this);
            if(professorPreferences.isAwarenessEnabled())
                activateAwareness();
            else
                deactivateAwareness();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if(menuItem.getItemId() == R.id.menu_logout){
            Log.d(TAG, "onMenuItemClick: performing logout...");
            performLogout();
        }
        return true;
    }

    private void performLogout() {
        Log.d(TAG, "performLogout: clearing tokens..");
        TokenUtil util = new TokenUtil(this);
        util.clear();

        Log.d(TAG, "performLogout: disabling awareness...");
        ProfessorPreferences preferences = new ProfessorPreferences(this);
        preferences.setAwarenessEnabled(false);
        Log.d(TAG, "performLogout: logged out, going back to SplashActivity!");
        startActivity(new Intent(this, SplashActivity.class));
        Log.d(TAG, "performLogout: finishing MainActivity");
        finish();
    }
}
