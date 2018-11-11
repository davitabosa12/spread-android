package smd.ufc.br.spread;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class LaboratorioReservaActivity extends AppCompatActivity implements View.OnClickListener {

    String laboratorio;
    TextInputLayout tilTitulo, tilDescricao;
    EditText edtHorarioInicio, edtHorarioFim;
    Button btnReservar;
    TimePickerFragment timePicker;
    int hora, minuto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laboratorio_reserva);
        laboratorio = this.getIntent().getStringExtra("labNome");
        setTitle("Reservar " + laboratorio);

        tilTitulo = findViewById(R.id.til_titulo);
        tilDescricao = findViewById(R.id.til_descricao);

        edtHorarioFim = findViewById(R.id.edt_horario_fim);
        edtHorarioInicio = findViewById(R.id.edt_horario_inicio);

        btnReservar = findViewById(R.id.btn_reservar);
        btnReservar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_reservar){
            realizarReserva();
        }
    }

    private void realizarReserva() {
        String titulo = tilTitulo.getEditText().getText().toString();
        String descricao = tilDescricao.getEditText().getText().toString();
        String horarioInicio = edtHorarioInicio.getText().toString();
        String horarioFim = edtHorarioFim.getText().toString();
        if(verificarDados(titulo, horarioFim, horarioInicio)){
            Toast.makeText(this,
                    "Titulo: " + titulo + ", Descricao: " + descricao + ", horarioInicio: " + horarioInicio + ", horarioFim: " + horarioFim
                    , Toast.LENGTH_SHORT).show();
            Log.d("Reservas", "realizarReserva: " + "Titulo: " + titulo + ", Descricao: " + descricao + ", horarioInicio: " + horarioInicio + ", horarioFim: " + horarioFim);
        }
        setResult(RESULT_OK);
        finish();
    }

    private boolean verificarDados(String titulo, String horarioFim, String horarioInicio) {


        if(titulo.isEmpty() || horarioFim.isEmpty() || horarioInicio.isEmpty()){
            return false;
        }
        return true;
    }

    public void showPickerDialog(View view) {

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            Toast.makeText(getActivity(), hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();


        }
    }

}
