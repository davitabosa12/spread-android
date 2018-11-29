package smd.ufc.br.spread.fragments.alterardadoscadastro;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.impl.WorkManagerImpl;
import smd.ufc.br.spread.MainActivity;
import smd.ufc.br.spread.R;
import smd.ufc.br.spread.ResponseListener;
import smd.ufc.br.spread.SplashActivity;
import smd.ufc.br.spread.utils.AlunoGetterTask;
import smd.ufc.br.spread.utils.ProfGetterTask;
import smd.ufc.br.spread.utils.TokenUtil;
import smd.ufc.br.spread.workers.UpdateAlunoWorker;
import smd.ufc.br.spread.workers.UpdateProfessorWorker;

public class AlterarDadosCadastroFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "AlterarDadosCadastroFrg";
    Button btnAtualizarCadastro;
    EditText edtNome, edtEmail, edtTopico, edtSenha, edtRepitaSenha;
    ProgressBar loader;
    private String userType;
    Activity activity;
    String matricula;


    public static AlterarDadosCadastroFragment newInstance() {
        return new AlterarDadosCadastroFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alterar_dados_cadastro_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = getActivity();
        matricula = activity.getIntent().getStringExtra("matricula");
        btnAtualizarCadastro = getActivity().findViewById(R.id.btn_atualizar_cadastro);
        edtNome = ((TextInputLayout)activity.findViewById(R.id.edt_nome)).getEditText();
        edtEmail = ((TextInputLayout)activity.findViewById(R.id.edt_email)).getEditText();
        edtTopico = ((TextInputLayout)activity.findViewById(R.id.edt_topico)).getEditText();
        edtSenha = ((TextInputLayout)activity.findViewById(R.id.edt_senha)).getEditText();
        edtRepitaSenha = ((TextInputLayout)activity.findViewById(R.id.edt_senha_confirma)).getEditText();
        loader = activity.findViewById(R.id.progressBar);

        btnAtualizarCadastro.setOnClickListener(this);
        userType = getActivity().getIntent().getStringExtra("userType");
        Log.d(TAG, "onActivityCreated: usertype: " + userType);
        if(userType != null && userType.equals("professor")){
            edtTopico.setVisibility(View.VISIBLE);
        }

    }

    private void toggleLoader(boolean visibility){
        if(visibility){
            edtTopico.setVisibility(View.GONE);
            edtNome.setVisibility(View.GONE);
            edtEmail.setVisibility(View.GONE);
            btnAtualizarCadastro.setVisibility(View.GONE);
            edtSenha.setVisibility(View.GONE);
            edtRepitaSenha.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
        } else {
            if(userType != null && userType.equals("professor")){
                edtTopico.setVisibility(View.VISIBLE);
            }
            edtNome.setVisibility(View.VISIBLE);
            edtEmail.setVisibility(View.VISIBLE);
            btnAtualizarCadastro.setVisibility(View.VISIBLE);
            edtSenha.setVisibility(View.VISIBLE);
            edtRepitaSenha.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_atualizar_cadastro){
            tentarAtualizacao();
        }
    }

    private void tentarAtualizacao() {
        Log.d(TAG, "tentarAtualizacao: ");
        toggleLoader(true);
        if(checarCampos()){
            tentarEnvioDados();
        } else{
            Log.d(TAG, "tentarAtualizacao: dados invalidos");
            toggleLoader(false);
        }
    }

    private void tentarEnvioDados() {
        Log.d(TAG, "tentarEnvioDados: ");
        if(userType != null && userType.equals("professor")) {

            tentarEnvioDadosProfessor();
        } else {
            tentarEnvioDadosAluno();
        }

        Log.d(TAG, "tentarEnvioDados: workers iniciados, fazer logout");
        startActivity(new Intent(getContext(), SplashActivity.class));
        activity.finish();

    }

    private void tentarEnvioDadosAluno() {
        Log.d(TAG, "tentarEnvioDadosAluno: aluno");
        TokenUtil util = new TokenUtil(getContext());
        util.setUserType("aluno");
        util.setMatricula(matricula);
        util.setLogin(edtEmail.getText().toString());
        util.setPassword(edtSenha.getText().toString());
        util.setName(edtNome.getText().toString());


        Constraints workConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UpdateAlunoWorker.class)
                .setConstraints(workConstraints)
                .build();
        WorkManager.getInstance().enqueue(workRequest);
    }

    private void tentarEnvioDadosProfessor() {
        Log.d(TAG, "tentarEnvioDadosProfessor: professor");
        TokenUtil util = new TokenUtil(getContext());
        util.setUserType("professor");
        util.setMatricula(matricula);
        util.setLogin(edtEmail.getText().toString());
        util.setPassword(edtSenha.getText().toString());
        util.setName(edtNome.getText().toString());
        util.setTopico(edtTopico.getText().toString());


        Constraints workConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(UpdateProfessorWorker.class)
                .setConstraints(workConstraints)
                .build();
        WorkManager.getInstance().enqueue(workRequest);
    }

    private boolean checarCampos() {
        Log.d(TAG, "checarCampos: checando..");
        edtNome.setError(null);
        edtTopico.setError(null);
        edtEmail.setError(null);
        edtRepitaSenha.setError(null);
        String senha1, senha2, topico;
        if(userType != null && userType.equals("professor")){
            Log.d(TAG, "checarCampos: é professor");
            topico = edtTopico.getText().toString();
            if(!topico.matches("[a-zA-Z0-9-_.~%]+")){
                Log.d(TAG, "checarCampos: topico invalido");
                edtTopico.setError("Nome de tópico inválido");
                return false;
            }
        }
        senha1 = edtSenha.getText().toString();
        senha2 = edtRepitaSenha.getText().toString();

        if(!senha1.equals(senha2)){
            Log.d(TAG, "checarCampos: senhas diferem");
            edtRepitaSenha.setError("A senha fornecida aqui não corresponde");
            return false;
        }
        Log.d(TAG, "checarCampos: tudo ok");
        return true;
    }
}
