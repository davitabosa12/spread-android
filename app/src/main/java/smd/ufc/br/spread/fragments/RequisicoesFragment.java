package smd.ufc.br.spread.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import smd.ufc.br.spread.EscolherLaboratorioReservaActivity;
import smd.ufc.br.spread.R;

public class RequisicoesFragment extends Fragment implements CalendarView.OnDateChangeListener, View.OnClickListener {
    private static final String TAG = "RequisicoesFragment";
    int ano, mes, dia;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requisicoes, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        JodaTimeAndroid.init(getContext());


        DateTime hoje = DateTime.now();
        CalendarView calendarioReserva = getActivity().findViewById(R.id.calendar_dia_reserva);
        Button escolherLab = getActivity().findViewById(R.id.btn_escolher_lab);
        escolherLab.setOnClickListener(this);
        calendarioReserva.setMinDate(hoje.getMillis());
        calendarioReserva.setMaxDate(hoje.plusWeeks(15).getMillis());
        calendarioReserva.setOnDateChangeListener(this);


        ano = hoje.getYear();
        mes = hoje.getMonthOfYear();
        dia = hoje.getDayOfMonth();

        Log.d(TAG, "onActivityCreated: " + dia + " / " + mes + "/" + ano);

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
        //Toast.makeText(getActivity(), i + "/" + i1 + "/" + i2, Toast.LENGTH_SHORT).show();
        ano = i;
        mes = i1 + 1;
        dia = i2;
        Log.d(TAG, "onActivityCreated: " + dia + " / " + mes + "/" + ano);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_escolher_lab){
            Intent i = new Intent(getContext(), EscolherLaboratorioReservaActivity.class);
            i.putExtra("ano" ,ano);
            i.putExtra("mes" ,mes);
            i.putExtra("dia" ,dia);
            startActivity(i);
        }
    }
}
