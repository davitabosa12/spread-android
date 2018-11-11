package smd.ufc.br.spread;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import smd.ufc.br.spread.model.Laboratorio;
import smd.ufc.br.spread.utils.LaboratorioGetterTask;
import smd.ufc.br.spread.views.LaboratorioView;

public class EscolherLaboratorioReservaActivity extends AppCompatActivity implements ResponseListener<List<Laboratorio>> {
    private static final int RESERVA_REQUEST_CODE = 2314;
    LaboratorioGetterTask task;
    LinearLayout linearScroll;
    List<Laboratorio> laboratorios;
    List<LaboratorioView> laboratorioViews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar_laboratorio);
        task = new LaboratorioGetterTask(this, this);
        task.execute();
        linearScroll = findViewById(R.id.linearScroll);
        laboratorioViews = new ArrayList<>();
    }

    @Override
    public void doThis(List<Laboratorio> response) {
        laboratorios = response;
        for(Laboratorio l : laboratorios){
            LaboratorioView lv = new LaboratorioView(this);
            lv.setNome(l.getNome());
            lv.setOnClickListener(new LabClickListener(lv));
            laboratorioViews.add(lv);
        }
        refresh();
    }

    private void refresh() {
        for(LaboratorioView laboratorioView: laboratorioViews){
            linearScroll.addView(laboratorioView);
        }
    }

    public class LabClickListener implements View.OnClickListener{
        LaboratorioView laboratorioView;

        public LabClickListener(LaboratorioView laboratorioView) {
            this.laboratorioView = laboratorioView;
        }

        @Override
        public void onClick(View view) {


            Intent i = new Intent(getApplicationContext(), LaboratorioReservaActivity.class);
            i.putExtra("labNome", laboratorioView.getNome());

            startActivityForResult(i, RESERVA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESERVA_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Você será notificado quando a reserva for concluída", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
