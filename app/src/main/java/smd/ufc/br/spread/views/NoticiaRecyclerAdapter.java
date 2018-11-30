package smd.ufc.br.spread.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import smd.ufc.br.spread.R;
import smd.ufc.br.spread.model.Noticia;

public class NoticiaRecyclerAdapter extends RecyclerView.Adapter<NoticiaRecyclerAdapter.ViewHolder> {
    private final String TAG = "NoticiaRecyclerAdapter";
    private ArrayList<Noticia> mNoticias;

    public NoticiaRecyclerAdapter(ArrayList<Noticia> mNoticias) {
        this.mNoticias = mNoticias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NoticiaView mNoticiaView = new NoticiaView(parent.getContext());
        mNoticiaView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        Log.d(TAG, "onCreateViewHolder: created");
        return new ViewHolder(mNoticiaView);
    }




    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: bound");
        final Noticia n = mNoticias.get(position);
        holder.getCustomView().setTitulo(n.getTitulo());
        holder.getCustomView().setCorpo(n.getCorpo());
        holder.getCustomView().setTopico(n.getTopico());
        holder.getCustomView().setData(n.getTimestamp());


        holder.getCustomView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: " + n.getTitulo());
            }
        });


    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: item count requested: " + mNoticias.size());
        return mNoticias.size();
    }

    /**
     * Classe do ViewHolder
     * Tem que existir para o Adapter funcionar.
     */
    public class ViewHolder extends RecyclerView.ViewHolder{
        private NoticiaView mCustomView;
        public ViewHolder(final View itemView) {
            super(itemView);
            mCustomView = (NoticiaView) itemView;
        }

        public NoticiaView getCustomView() {
            return mCustomView;
        }
    }
}
