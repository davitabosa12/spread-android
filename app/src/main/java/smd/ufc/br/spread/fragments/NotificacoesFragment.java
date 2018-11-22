package smd.ufc.br.spread.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import smd.ufc.br.spread.R;
import smd.ufc.br.spread.ResponseListener;
import smd.ufc.br.spread.model.Topico;
import smd.ufc.br.spread.utils.ProfessorPreferences;
import smd.ufc.br.spread.utils.TokenUtil;
import smd.ufc.br.spread.utils.TopicoGetterTask;
import smd.ufc.br.spread.utils.TopicoPreferences;

public class NotificacoesFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    HashMap<String, Switch> views;
    TokenUtil util;
    LinearLayout linearLayout;
    TopicoPreferences preferences;
    private String TAG = "NotificacoesFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        views = new HashMap<>();
        return inflater.inflate(R.layout.fragment_notificacoes, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        preferences = new TopicoPreferences(getContext());

        util = new TokenUtil(getContext());

        Switch swEntrarBloco = getActivity().findViewById(R.id.sw_entrar_bloco);
        swEntrarBloco.setOnCheckedChangeListener(this);

        Set<String> disponiveis = preferences.getTopicosDisponiveis();
        Set<String> escolhidos = preferences.getTopicosInteresse();
        linearLayout = getActivity().findViewById(R.id.linearScroll);
        for(String topico : disponiveis){
            Switch s = new Switch(getContext());
            s.setText(topico);
            s.setChecked(false);

            views.put(topico, s);
        }
        if(escolhidos != null)
            for(String topicoInteresse : escolhidos){
                Switch s = views.get(topicoInteresse);
                s.setChecked(true);
            }

        for(Switch s : views.values()){
            linearLayout.addView(s);
        }



    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: saving topicos..");
        for(Switch s : views.values()){
            String nomeTopico = s.getText().toString();
            if(s.isChecked()){

                Log.d(TAG, "onDestroy: Inscrevendo em " + nomeTopico);
                preferences.inscreverNoTopico(nomeTopico);
            } else {
                Log.d(TAG, "onDestroy: Desinscrevendo de " + nomeTopico);
                preferences.desinscreverNoTopico(nomeTopico);
            }
        }
        ouvirTopicos();
        Log.d(TAG, "onDestroy: BYE!");
        super.onDestroy();
    }

    private void ouvirTopicos() {
        TopicoPreferences prefs = new TopicoPreferences(getContext());
        Set<String> disp = prefs.getTopicosDisponiveis();
        Set<String> interesse = prefs.getTopicosInteresse();
        Set<String> semInteresse = new HashSet<>(disp);
        if(disp == null){
            return;
        }
        if(interesse == null)
            interesse = new HashSet<>(disp);
        else
            semInteresse.removeAll(interesse);


        for(final String topico : interesse)
            FirebaseMessaging.getInstance().subscribeToTopic(topico).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "onComplete: Topico " + topico + " inscrito com sucesso");
                    } else {
                        Log.e(TAG, "onComplete: Falha ao se inscrever no topico " + topico, new Exception("DeuBodeException"));
                    }
                }
            });



        for(final String topico : semInteresse){
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topico).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                        Log.d(TAG, "onComplete: Topico " + topico + " removido com sucesso");
                    else
                        Log.e(TAG, "onComplete: Falha ao se desinscrever do topico " + topico, new Exception("DeuBodeException"));
                }
            });
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();

        switch (id){
            case R.id.sw_entrar_bloco:
                new ProfessorPreferences(getContext()).setAwarenessEnabled(b);
                break;
        }
    }
}
