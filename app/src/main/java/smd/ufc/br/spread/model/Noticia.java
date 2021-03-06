package smd.ufc.br.spread.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Noticia {
    private String titulo;
    private String corpo;
    private String topico;
    private String timestamp;
    private String base64Image;

    public Noticia() {
        this.titulo = "";
        this.corpo = "";
        this.topico = "";
        this.timestamp = "";
        this.base64Image = "";
    }

    public Noticia(JSONObject jsonObject) throws JSONException {
        this.titulo = jsonObject.getString("titulo");
        this.corpo = jsonObject.getString("corpo");
        this.topico = jsonObject.getString("topico");
        this.timestamp = jsonObject.getString("timestamp");
    }

    static List<Noticia> fromJSONArray (JSONArray data) throws JSONException {
        List<Noticia> resposta = new ArrayList<>();
        if(data == null)
            return resposta;
        for (int i = 0; i < data.length(); i++) {
            resposta.add(new Noticia(data.getJSONObject(i)));
        }
        return resposta;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCorpo() {
        return corpo;
    }

    public void setCorpo(String corpo) {
        this.corpo = corpo;
    }

    public String getTopico() {
        return topico;
    }

    public void setTopico(String topico) {
        this.topico = topico;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
