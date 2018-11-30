package smd.ufc.br.spread.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smd.ufc.br.spread.R;
import smd.ufc.br.spread.ResponseListener;
import smd.ufc.br.spread.model.Noticia;
import smd.ufc.br.spread.utils.NoticiaGetterTask;
import smd.ufc.br.spread.views.NoticiaRecyclerAdapter;
import smd.ufc.br.spread.views.NoticiaView;

public class NoticiasFragment extends Fragment implements ResponseListener<List<Noticia>>, View.OnClickListener{
    private final String TAG = "NoticiasFragment";
    List<View> noticiaViews;
    LinearLayout linearScroll;
    ScrollView scrollView;
    private boolean isSet = false;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private NoticiaRecyclerAdapter mAdapter;
    private ArrayList<Noticia> mNoticias = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        noticiaViews = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_noticias, container, false);

        return  v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NoticiaGetterTask task = new NoticiaGetterTask(getContext(), this);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Setar o Adapter
        mAdapter = new NoticiaRecyclerAdapter(mNoticias);
        mRecyclerView.setAdapter(mAdapter);

        /*linearScroll = getActivity().findViewById(R.id.linearScroll);
        scrollView = getActivity().findViewById(R.id.scrollView);
        scrollView.setOnClickListener(this);
        linearScroll.setOnClickListener(this);*/
        if (savedInstanceState == null) {
            //pega as noticias, apenas se for a primeira vez executando
            task.execute();

        }
        /*for (View view : noticiaViews) {
            linearScroll.addView(view);
        }*/


    }

    @Override
    public void doThis(List<Noticia> response) {
        mNoticias.clear();
        mNoticias.addAll(response);
        Log.d(TAG, "doThis: " + mNoticias.toString());
        mAdapter.notifyDataSetChanged();
        /*for(Noticia n : response){
            NoticiaView view = new NoticiaView(getContext());
            view.setTitulo(n.getTitulo());
            view.setCorpo(n.getCorpo());
            view.setTopico(n.getTopico());
            view.setData(n.getTimestamp());
            view.setOnTouchListener(this);
            noticiaViews.add(view);
            Button b = new Button(getContext());
            b.setText(n.getTitulo());
            b.setOnClickListener(this);
            noticiaViews.add(b);
        }
        refresh();*/
    }

    private void refresh() {
        linearScroll.removeAllViews();
        for (View view : noticiaViews) {
            linearScroll.addView(view);
        }
    }

    @Override
    public void onClick(View view) {

        Toast.makeText(getContext(), " " + view.getId(), Toast.LENGTH_SHORT).show();
    }

    public class NoticiaClickListener implements View.OnClickListener {
        NoticiaView noticiaView;

        public NoticiaClickListener(NoticiaView laboratorioView) {
            this.noticiaView = laboratorioView;
        }

        @Override
        public void onClick(View view) {

            Log.d(TAG, "onClick: click!");
            Toast.makeText(getContext(), noticiaView.getTitulo(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public String toString() {
            return super.toString() + "  FROM FRAGMENT!!";
        }
    }

    private void configuraRecycler() {
            //iniciar o RecyclerView
            mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);

            //iniciar o LayoutManager (No caso, LinearLayoutManager)
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            //Setar o Adapter
            mAdapter = new NoticiaRecyclerAdapter(mNoticias);
            mRecyclerView.setAdapter(mAdapter);

    }
}
