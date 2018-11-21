package smd.ufc.br.spread;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import smd.ufc.br.spread.fragments.CheckInProfFragment;
import smd.ufc.br.spread.fragments.LaboratorioFragment;
import smd.ufc.br.spread.fragments.NoticiasFragment;

public class AnonymousActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

        private static final String TAG = "AnonymousActivity";
        NavigationView navigationView;
        private static final int LOGIN_REQUEST_CODE = 401;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anonymous);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        @Override
        public void onBackPressed() {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                //do nothing
            }
        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main2, menu);

            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            switch(id){
                case R.id.nav_login:
                    startActivity(new Intent(this, AlunoProfessorLogin.class));
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
}
