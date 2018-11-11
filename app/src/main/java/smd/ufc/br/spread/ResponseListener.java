package smd.ufc.br.spread;

import org.json.JSONArray;
import org.json.JSONException;

public interface ResponseListener<T> {
    public abstract void doThis(T response);
}
