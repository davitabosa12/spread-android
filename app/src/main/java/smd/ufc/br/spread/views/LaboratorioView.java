package smd.ufc.br.spread.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import net.danlew.android.joda.JodaTimeAndroid;

import smd.ufc.br.spread.R;

public class LaboratorioView extends ConstraintLayout {
    private final String TAG = "NoticiaView";
    private TextView txvLaboratorioNome;
    private String nome;
    boolean inflated = false;

    public LaboratorioView(Context context) {
        super(context);
        init(context);
    }

    public LaboratorioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LaboratorioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_laboratorio, this);
        inflated = true;
        onFinishInflate();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if(inflated)
            this.txvLaboratorioNome.setText(nome);
        this.nome = nome;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //bindar txvs
        txvLaboratorioNome = findViewById(R.id.txv_nome_laboratorio);
        //setar valores
        txvLaboratorioNome.setText(nome);
        inflated = true;
    }
}
