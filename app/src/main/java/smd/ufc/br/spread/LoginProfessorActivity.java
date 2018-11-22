package smd.ufc.br.spread;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import smd.ufc.br.spread.net.NetworkConnect;
import smd.ufc.br.spread.utils.TokenUtil;
import smd.ufc.br.spread.utils.TopicRefresher;
import smd.ufc.br.spread.utils.TopicoPreferences;
import smd.ufc.br.spread.workers.SendFCMTokenWorker;

public class LoginProfessorActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "LoginAlunoActivity";

    protected static final int LOGIN_SENHA_INCORRETA = -1;
    protected static final int LOGIN_SEM_INTERNET = -2;
    protected static final int LOGIN_USUARIO_NAO_EXISTE = -3;
    protected static final int LOGIN_OK = 0;

    Button btnLogin, btnCadastro, btnEsqueciSenha;
    EditText edtEmail, edtSenha;
    LoginProfessorActivity.UserLoginTask mAuthTask;
    View mLoginFormView, mProgressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_aluno);

        btnLogin = findViewById(R.id.btn_login);
        btnCadastro = findViewById(R.id.btn_cadastro);
        btnEsqueciSenha = findViewById(R.id.btn_esqueci_senha);

        TextInputLayout tilEmail, tilSenha;

        tilEmail = findViewById(R.id.edt_email);
        tilSenha = findViewById(R.id.edt_senha);
        edtEmail = tilEmail.getEditText();
        edtSenha = tilSenha.getEditText();

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        //click listeners

        btnLogin.setOnClickListener(this);
        btnCadastro.setOnClickListener(this);
        btnEsqueciSenha.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                pegarDadosETentarLogin();
                break;
            case R.id.btn_cadastro:
                break;
            case R.id.btn_esqueci_senha:
                break;
        }
    }

    private void pegarDadosETentarLogin() {
        String email, senha;
        email = edtEmail.getText().toString();
        senha = edtSenha.getText().toString();

        if(dadosValidos(email, senha)){
            tentarLogin();
        } else {
            Toast.makeText(this, "Insira os dados corretamente", Toast.LENGTH_SHORT).show();
        }
    }

    private void tentarLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        edtEmail.setError(null);
        edtSenha.setError(null);

        // Store values at the time of the login attempt.
        String email = edtEmail.getText().toString();
        String password = edtSenha.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            edtSenha.setError(getString(R.string.error_invalid_password));
            focusView = edtSenha;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError(getString(R.string.error_field_required));
            focusView = edtEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }

    }

    private boolean dadosValidos(String email, String senha) {
        if(email == null || email.equals(""))
            return false;
        else if(senha == null || senha.equals(""))
            return false;
        return true;
    }


    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

        //private final String mEmail;
        private final String mLogin;
        private final String mPassword;
        private int statusCode;

        UserLoginTask(String login, String password) {
            mLogin = login;
            mPassword = password;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String url = getResources().getString(R.string.server_url) + "/api/login/professor";
            JSONObject loginParams = new JSONObject();
            try {
                loginParams.put("login", mLogin);
                loginParams.put("senha", mPassword);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            NetworkConnect connect = new NetworkConnect(getApplicationContext(), Request.Method.POST, url, loginParams);
            RequestFuture<JSONObject> future = connect.connect();
            try {
                JSONObject response = future.get();
                //salvar token no sharedPref
                TokenUtil util = new TokenUtil(getApplicationContext());
                TopicoPreferences prefs = new TopicoPreferences(getApplicationContext());
                try{
                    Iterator<String> i = response.keys();
                    while(i.hasNext()){
                        String key = i.next();
                        Log.d(TAG, "doInBackground: " + key + ":" + response.getString(key));
                    }
                    String token = response.getString("token");
                    //String login = response.getString("login");
                    String name = response.getString("nome");
                    //String matricula = response.getString("siape");
                    String email = response.getString("email");


                    Log.d(TAG, "doInBackground received: " + " " + name + " "
                            + " " + email);


                    util.clear();
                    util.setAuthToken(token);
                    util.setLogin(mLogin);
                    util.setPassword(mPassword);

                    util.setUserType("professor");
                    util.setName(name);


                    Constraints constraints = new Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build();
                    OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SendFCMTokenWorker.class)
                            .setConstraints(constraints)
                            .build();
                    WorkManager.getInstance().enqueue(workRequest);
                    return LOGIN_OK;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return LOGIN_SEM_INTERNET;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return LOGIN_SEM_INTERNET;
            } catch (ExecutionException e) {

                Throwable error = e.getCause();

                if(error instanceof AuthFailureError){
                    //erro de autenticacao
                    Log.d(TAG, "onResponse: Senha incorreta");
                    //Toast.makeText(LoginActivity.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                    return LOGIN_SENHA_INCORRETA;
                } else if(error instanceof TimeoutError){
                    //erro de rede
                    Log.d(TAG, "onResponse: Não foi possível se conectar ao servidor.");
                    return LOGIN_SEM_INTERNET;
                    //Toast.makeText(LoginActivity.this, "Não foi possível se conectar ao servidor.", Toast.LENGTH_SHORT).show();
                }
                Log.e(TAG, "onErrorResponse: ", error);
                return LOGIN_SEM_INTERNET;
            }
        }

        @Override
        protected void onPostExecute(final Integer result) {
            mAuthTask = null;
            showProgress(false);

            if (!(result < 0)) {
                setResult(RESULT_OK);
                finish();
            } else {
                switch (result){
                    case LOGIN_SENHA_INCORRETA:
                        edtSenha.setError(getString(R.string.error_incorrect_password));
                        edtSenha.requestFocus();
                        break;
                    case LOGIN_SEM_INTERNET:
                        edtSenha.setError(getString(R.string.error_sem_internet));
                        edtSenha.requestFocus();
                        break;
                    case LOGIN_USUARIO_NAO_EXISTE:
                        edtSenha.setError(getString(R.string.error_nao_existe));
                        edtSenha.requestFocus();
                        break;
                }

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }


    }
}
