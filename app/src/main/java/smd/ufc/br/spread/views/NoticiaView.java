package smd.ufc.br.spread.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;

import smd.ufc.br.spread.R;

public class NoticiaView extends ConstraintLayout {
    private final String TAG = "NoticiaView";
    private TextView txvTopico, txvTitulo, txvCorpo, txvData;
    private String topico, titulo, corpo, data;
    boolean inflated = false;

    public String getTopico() {
        return topico;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCorpo() {
        return corpo;
    }

    public String getData() {
        return data;
    }

    public NoticiaView(Context context) {
        super(context);
        init(context);
    }

    public NoticiaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NoticiaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.noticia_view, this);
        JodaTimeAndroid.init(context);
        onFinishInflate();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //bindar textviews
        txvTopico = findViewById(R.id.txv_topico);
        txvTitulo = findViewById(R.id.txv_titulo);
        txvCorpo = findViewById(R.id.txv_corpo_preview);
        txvData = findViewById(R.id.txv_data);



        //setar valores
        txvTopico.setText(topico);
        txvTitulo.setText(titulo);
        txvCorpo.setText(corpo);
        txvData.setText(data);

        txvTopico.setLetterSpacing(0.08f);

        inflated = true;
        Log.d(TAG, "onFinishInflate: inflado com " + txvTopico.getText() + ", " +
                txvTitulo.getText() + ", " + txvCorpo.getText() + ", " + txvData.getText());

    }


    public void setTopico(String topico){
        if(inflated)
            txvTopico.setText(topico);
        this.topico = topico;
    }
    public void setTitulo(String titulo){
        if(inflated)
            txvTitulo.setText(titulo);
        this.titulo = titulo;
    }
    public void setCorpo(String corpo){
        String limit = corpo;
        //limita o texto mostrado em 60 caracteres
        if (corpo.length() > 60) {
            limit = limit.substring(0,56);
            limit += "...";
        }

        if(inflated)
            txvCorpo.setText(limit);
        this.corpo = corpo;
    }
    public void setData(String data){
        DateTime dataNoticia = new DateTime(data, DateTimeZone.forOffsetHours(-3));
        DateTime hoje = DateTime.now();
        hoje = hoje.minusHours(hoje.hourOfDay().get());
        hoje = hoje.minusMinutes(hoje.minuteOfHour().get());
        DateTime ontem = hoje.minusDays(1);
        DateTime doisDiasAtras = hoje.minusDays(2);
        DateTime tresDiasAtras = hoje.minusDays(3);
        Period horasDia = new Period().minusHours(24);
        String dia;

        if(dataNoticia.isBefore(tresDiasAtras.toInstant())){
            dia = dataNoticia.getDayOfMonth() + "/" + dataNoticia.getMonthOfYear() + "/" + dataNoticia.getYear();


        } else if(dataNoticia.isBefore(doisDiasAtras.toInstant())){
            dia = "três dias atrás";
        } else if(dataNoticia.isBefore(ontem.toInstant())){
            dia = "dois dias atrás";
        } else if(dataNoticia.isBefore(hoje)){
            dia = "ontem";
        } else {
            dia = "hoje";
        }

        if(inflated){
            txvData.setText(dia);
        }
        this.data = dia;
    }

}
