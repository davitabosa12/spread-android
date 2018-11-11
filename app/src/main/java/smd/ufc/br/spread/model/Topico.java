package smd.ufc.br.spread.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Topico {
    private String nome;
    private String descricao;

    public Topico() {
    }

    public Topico(JSONObject object) throws JSONException {
        this.nome = object.getString("nome");
        this.descricao = object.getString("descricao");
    }

    public Topico(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
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


    public static List<Topico> fromJSONArray(JSONArray data) throws JSONException {
        List<Topico> resposta = new ArrayList<>();
        if(data == null)
            return resposta;
        for (int i = 0; i < data.length(); i++) {
            resposta.add(new Topico(data.getJSONObject(i)));
        }
        return resposta;
    }
}
