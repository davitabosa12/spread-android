package smd.ufc.br.spread.net;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class NetworkConnect {
    private RequestQueue queue;
    private JsonObjectRequest request;
    private String url;
    private JSONObject params;
    private int method;
    private RequestFuture<JSONObject> future;

    /**
     * Utilidade que realiza uma conexao HTTP
     * @param ctx Contexto da aplicacao
     * @param method Metodo HTTP. Pode ser Request.Method.POST ou Request.Method.GET
     * @param url Endereço do servidor
     * @param params Parametros JSON
     */
    public NetworkConnect(Context ctx, int method, String url, JSONObject params){
        this.queue = Volley.newRequestQueue(ctx);
        this.method = method;
        this.params = params;
        this.url = url;

        this.future = RequestFuture.newFuture();
    }

    /**
     * Realiza a conexao com o servidor
     * @return Um RequestFuture representando o estado da operacao
     */
    public RequestFuture<JSONObject> connect(){
        request = new JsonObjectRequest(method, url, params, future, future);
        request.setShouldCache(false);
        queue.add(request);
        return future;
    }

}
