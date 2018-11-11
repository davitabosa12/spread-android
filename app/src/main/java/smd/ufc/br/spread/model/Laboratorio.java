package smd.ufc.br.spread.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Laboratorio {
    private String nome;
    private String descricao;

    public Laboratorio(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Laboratorio() {
        this.nome = "";
        this.descricao = "";
    }

    public Laboratorio(JSONObject data) throws JSONException {
        this.nome = data.getString("nome");
        this.descricao = data.getString("descricao");
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

    public static List<Laboratorio> fromJSONArray(JSONArray data) throws JSONException {
        List<Laboratorio> resposta = new ArrayList<>();
        if(data == null)
            return resposta;
        for (int i = 0; i < data.length(); i++) {
            resposta.add(new Laboratorio(data.getJSONObject(i)));
        }
        return resposta;
    }
}
