package smd.ufc.br.spread;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.InputMismatchException;

import smd.ufc.br.spread.utils.AlunoGetterTask;
import smd.ufc.br.spread.utils.ProfGetterTask;

public class VerificarDadosProfessorActivity extends AppCompatActivity implements View.OnClickListener, ResponseListener<JSONObject> {

    Button btnContinuar;
    EditText edtMatricula, edtCpf;
    ProgressBar loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_dados_professor);

        btnContinuar = findViewById(R.id.btn_continuar);
        edtMatricula = ((TextInputLayout)findViewById(R.id.edt_siape)).getEditText();
        edtCpf = ((TextInputLayout)findViewById(R.id.edt_cpf)).getEditText();
        loader = findViewById(R.id.progressBar);

        btnContinuar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_continuar){
            enviarDados();
        }
    }

    private void enviarDados() {
        visibilidadeLoader(true);
        String matricula, cpf;
        matricula = edtMatricula.getText().toString();
        cpf = edtCpf.getText().toString();
        if(checarCampos()){
            tentarEnvio(matricula, cpf);
        } else {
            visibilidadeLoader(false);
        }

    }

    private void tentarEnvio(String matricula, String cpf) {
        ProfGetterTask task = new ProfGetterTask(this, this);
        HashMap<String, String> params = new HashMap<>();
        params.put("siape", matricula);
        params.put("cpf", cpf);
        task.execute(params);
    }

    private boolean checarCampos() {
        //reset nos erros
        edtCpf.setError(null);
        edtMatricula.setError(null);

        //ler os campos
        String matricula, cpf;
        matricula = edtMatricula.getText().toString();
        cpf = edtCpf.getText().toString();
        if(matricula.isEmpty()){
            edtMatricula.setError("Campo obrigatório");
            return false;
        }
        if(cpf.isEmpty()){
            edtCpf.setError("Campo obrigatório");
            return false;
        }
        if(!checarCPF(cpf)){
            edtCpf.setError("CPF inválido");
            return false;
        }
        return true;

    }

    @Override
    public void doThis(JSONObject response) {
        if(response == null){
            Toast.makeText(this, "Matricula não cadastrada no sistema", Toast.LENGTH_SHORT).show();
            visibilidadeLoader(false);
        } else {
            String serverMatricula = null;
            String serverCPF = null;
            try {
                serverMatricula = response.getString("siape");

                serverCPF = response.getString("cpf");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String matricula = edtMatricula.getText().toString();
            String cpf = edtCpf.getText().toString();

            if(matricula.equals(serverMatricula) && cpf.equals(serverCPF)){
                //os dados conferem

                Intent i = new Intent(this, AlterarDadosCadastroActivity.class);
                i.putExtra("userType", "professor");
                i.putExtra("matricula", matricula);
                startActivity(i);
                finish();

            } else {
                //os dados informados nao conferem
                visibilidadeLoader(false);
                Toast.makeText(this, "Os dados informados não conferem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void visibilidadeLoader(boolean visivel){
        if(visivel){
            btnContinuar.setVisibility(View.GONE);
            edtMatricula.setVisibility(View.GONE);
            edtCpf.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
        } else {
            btnContinuar.setVisibility(View.VISIBLE);
            edtMatricula.setVisibility(View.VISIBLE);
            edtCpf.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        }

    }

    /**
     * https://www.devmedia.com.br/validando-o-cpf-em-uma-aplicacao-java/22097
     * @param CPF
     * @return
     */
    public boolean checarCPF(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
                CPF.equals("11111111111") ||
                CPF.equals("22222222222") || CPF.equals("33333333333") ||
                CPF.equals("44444444444") || CPF.equals("55555555555") ||
                CPF.equals("66666666666") || CPF.equals("77777777777") ||
                CPF.equals("88888888888") || CPF.equals("99999999999") ||
                (CPF.length() != 11))
            return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
                // converte o i-esimo caractere do CPF em um numero:
                // por exemplo, transforma o caractere '0' no inteiro 0
                // (48 eh a posicao de '0' na tabela ASCII)
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
                num = (int)(CPF.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig11 = '0';
            else dig11 = (char)(r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                return(true);
            else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
    }
}
