package smd.ufc.br.spread.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Evento {
    private String nome;
    private String descricao;
    private String horarioInicio;
    private String horarioFim;
    private String dia;
    private String local;

    private Evento(){

    }

    public Evento(String nome, String descricao, String horarioInicio, String horarioFim, String dia, String local) {
        this.nome = nome;
        this.descricao = descricao;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.dia = dia;
        this.local = local;
    }

    public Evento(JSONObject obj) throws JSONException {
        this.nome = obj.getString("nome");
        this.descricao = obj.getString("descricao");
        this.horarioInicio = obj.getString("horarioInicio");
        this.horarioFim = obj.getString("horarioFim");
        this.dia = obj.getString("dia");
        this.local = obj.getString("local");
    }

    static List<Evento> fromJSONArray(JSONArray data) throws JSONException {
        List<Evento> resposta = new ArrayList<>();
        if(data == null)
            return resposta;
        for (int i = 0; i < data.length(); i++) {
            resposta.add(new Evento(data.getJSONObject(i)));
        }
        return resposta;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(String horarioFim) {
        this.horarioFim = horarioFim;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
