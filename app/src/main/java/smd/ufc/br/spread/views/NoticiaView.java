package smd.ufc.br.spread.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;

import smd.ufc.br.spread.R;

public class NoticiaView extends ConstraintLayout {
    private TextView txvTopico, txvTitulo, txvCorpo, txvData;
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
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //bindar textviews
        txvTopico = findViewById(R.id.txv_topico);
        txvTitulo = findViewById(R.id.txv_titulo);
        txvCorpo = findViewById(R.id.txv_corpo_preview);
        txvData = findViewById(R.id.txv_data);
    }

    public void setTopico(String topico){
        txvTopico.setText(topico);
    }
    public void setTitulo(String titulo){
        txvTitulo.setText(titulo);
    }
    public void setCorpo(String corpo){
        String limit = corpo;
        //limita o texto mostrado em 30 caracteres
        if (corpo.length() > 30) {
            limit = limit.substring(0,26);
            limit += "...";
        }

        txvCorpo.setText(limit);
    }
    public void setData(String data){
        DateTime dataNoticia = new DateTime(data, DateTimeZone.forOffsetHours(-3));
        DateTime hoje = DateTime.now();
        DateTime ontem = hoje.minusDays(1);
        DateTime doisDiasAtras = hoje.minusDays(2);
        DateTime tresDiasAtras = hoje.minusDays(3);
        if(dataNoticia.isBefore(tresDiasAtras.toInstant())){
            txvData.setText(dataNoticia.getDayOfMonth() + "/" + dataNoticia.getMonthOfYear() + "/" + dataNoticia.getYear());

        } else if(dataNoticia.isBefore(doisDiasAtras.toInstant())){
            txvData.setText("três dias atrás");
        } else if(dataNoticia.isBefore(ontem.toInstant())){
            txvData.setText("dois dias atrás");
        } else if(dataNoticia.isBefore(hoje.toInstant())){
            txvData.setText("ontem");
        } else {
            txvData.setText("hoje");
        }
    }
}
