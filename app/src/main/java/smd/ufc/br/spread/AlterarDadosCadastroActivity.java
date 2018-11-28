package smd.ufc.br.spread;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import smd.ufc.br.spread.fragments.alterardadoscadastro.AlterarDadosCadastroFragment;

public class AlterarDadosCadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alterar_dados_cadastro_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AlterarDadosCadastroFragment.newInstance())
                    .commitNow();
        }
    }
}
