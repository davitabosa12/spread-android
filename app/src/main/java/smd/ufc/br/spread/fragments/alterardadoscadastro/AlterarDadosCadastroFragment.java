package smd.ufc.br.spread.fragments.alterardadoscadastro;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import smd.ufc.br.spread.R;

public class AlterarDadosCadastroFragment extends Fragment implements View.OnClickListener {

    Button btnAtualizarCadastro;
    EditText edtNome, edtEmail, edtTopico;
    ProgressBar loader;
    private String userType;
    Activity activity;

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
        btnAtualizarCadastro = getActivity().findViewById(R.id.btn_atualizar_cadastro);
        edtNome = activity.findViewById(R.id.edt_nome);
        edtEmail = activity.findViewById(R.id.edt_email);
        edtTopico = activity.findViewById(R.id.edt_topico);
        loader = activity.findViewById(R.id.progressBar);

        btnAtualizarCadastro.setOnClickListener(this);
        userType = getActivity().getIntent().getStringExtra("userType");
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
            loader.setVisibility(View.VISIBLE);
        } else {
            if(userType != null && userType.equals("professor")){
                edtTopico.setVisibility(View.VISIBLE);
            }
            edtNome.setVisibility(View.VISIBLE);
            edtEmail.setVisibility(View.VISIBLE);
            btnAtualizarCadastro.setVisibility(View.VISIBLE);
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
        toggleLoader(true);
        if(checarCampos()){
            tentarEnvioDados();
        }
    }

    private boolean checarCampos() {
        edtNome.setError(null);
        edtTopico.setError(null);
        edtEmail.setError(null);
        String nome, email, topico;
        if(userType != null && userType.equals("professor")){
            topico = edtTopico.getText().toString();
            if(!topico.matches("[a-zA-Z0-9-_.~%]+")){
                edtTopico.setError("Nome de tópico inválido");
                return false;
            }
        }
        nome = edtNome.getText().toString();
        email = edtNome.getText().toString();

        return true;
    }
}
