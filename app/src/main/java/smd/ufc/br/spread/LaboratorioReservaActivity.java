package smd.ufc.br.spread;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
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

public class LaboratorioReservaActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {

    String laboratorio;
    TextInputLayout tilTitulo, tilDescricao;
    EditText edtHorarioInicio, edtHorarioFim;
    Button btnReservar;
    TimePickerFragment timePicker;
    int idEdtSelecionado;
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
        edtHorarioFim.setOnFocusChangeListener(this);
        edtHorarioFim.setOnClickListener(this);
        edtHorarioInicio.setOnFocusChangeListener(this);
        edtHorarioInicio.setOnClickListener(this);

        btnReservar = findViewById(R.id.btn_reservar);
        btnReservar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.btn_reservar:
                realizarReserva();
                break;
            case R.id.edt_horario_inicio:
                idEdtSelecionado = id;
                showPickerDialog(view);
                break;
            case R.id.edt_horario_fim:
                idEdtSelecionado = id;
                showPickerDialog(view);
                break;
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

    @Override
    public void onFocusChange(View view, boolean b) {
        int id = view.getId();
        if(b){
            switch (id){
                case R.id.edt_horario_inicio:
                    idEdtSelecionado = id;
                    showPickerDialog(view);
                    break;
                case R.id.edt_horario_fim:
                    idEdtSelecionado = id;
                    showPickerDialog(view);
                    break;
            }
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        String hora = "", minuto = "";
        if(i < 10){
            hora = "0";
        }
        if( i1 < 10){
            minuto = "0";
        }
        hora = hora + i + "";
        minuto = minuto + i1 + "";
        switch (idEdtSelecionado){
            case R.id.edt_horario_inicio:
                edtHorarioInicio.setText(hora + ":" + minuto);
                break;
            case R.id.edt_horario_fim:
                edtHorarioFim.setText(hora + ":" + minuto);
                break;
        }
    }

    public static class TimePickerFragment extends DialogFragment {

        Activity mActivity;
        TimePickerDialog.OnTimeSetListener mListener;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            if(context instanceof Activity){
                mActivity = getActivity();
                if(mActivity instanceof TimePickerDialog.OnTimeSetListener)
                    mListener = (TimePickerDialog.OnTimeSetListener) mActivity;
                else
                    throw new ClassCastException("You must implement OnTimeSetListener!");
            } else{
                throw new ClassCastException("Dialog context must be an activity");
            }
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), mListener, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }
    }

}
