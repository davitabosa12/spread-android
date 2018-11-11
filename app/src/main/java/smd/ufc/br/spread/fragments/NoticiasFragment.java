package smd.ufc.br.spread.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smd.ufc.br.spread.R;
import smd.ufc.br.spread.ResponseListener;
import smd.ufc.br.spread.model.Noticia;
import smd.ufc.br.spread.utils.NoticiaGetterTask;
import smd.ufc.br.spread.views.NoticiaView;

public class NoticiasFragment extends Fragment implements ResponseListener<List<Noticia>> {
    List<NoticiaView> noticiaViews;
    LinearLayout linearScroll;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noticiaViews = new ArrayList<>();
        return inflater.inflate(R.layout.fragment_noticias, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        NoticiaGetterTask task = new NoticiaGetterTask(getContext(), this);
        linearScroll = getActivity().findViewById(R.id.linearScroll);
        if(savedInstanceState == null){
            //pega as noticias, apenas se for a primeira vez executando
            task.execute();

        }
        for(NoticiaView view : noticiaViews){
            linearScroll.addView(view);
        }
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void doThis(List<Noticia> response) {
        for(Noticia n : response){
            NoticiaView view = new NoticiaView(getContext());
            view.setTitulo(n.getTitulo());
            view.setCorpo(n.getCorpo());
            view.setTopico(n.getTopico());
            view.setData(n.getTimestamp());
            noticiaViews.add(view);
        }
        refresh();
    }

    private void refresh() {
        for(NoticiaView view : noticiaViews){
            linearScroll.addView(view);
        }
    }
}
