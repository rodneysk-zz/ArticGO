package com.articgo.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.articgo.R;
import com.articgo.domain.Conquista;
import com.articgo.domain.Jogador;

import java.util.ArrayList;
import java.util.List;

public class ConquistaAdapter extends RecyclerView.Adapter<ConquistaAdapter.MyViewHolder> {

    private Conquista conquista;
    private Context mContext;
    private List<Conquista> listaConquistas;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    //=============================== CRIANDO ADAPTER DO DOMAIN ====================================
    public ConquistaAdapter(Context c, List<Conquista> lConquistas){
        this(c, lConquistas,  true, true);
    }

    public ConquistaAdapter(Context c, List<Conquista> lConquistas, boolean wa, boolean wcl){
        mContext = c;
        listaConquistas = lConquistas;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //=============================== CRIANDO VIEW HOLDER ==========================================
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RelativeLayout relativeLayout;
        public TextView txtTitulo;
        public RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public MyViewHolder(View itemView) {
            super(itemView);

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativo);
            relativeLayout.setOnClickListener(this);

            txtTitulo = (TextView) itemView.findViewById(R.id.titulo);

        }

        @Override
        public void onClick(View v) {
            if(mRecyclerViewOnClickListenerHack != null){
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        v = mLayoutInflater.inflate(R.layout.celula_conquista, parent, false);


        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    //========================= INSERE OS ITENS NA ACTIVITY ========================================
    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, int position) {

        conquista = listaConquistas.get(position);

        myViewHolder.txtTitulo.setText(conquista.getTitulo());

    }

    @Override
    public int getItemCount() {
        return listaConquistas.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r){
        mRecyclerViewOnClickListenerHack = r;
    }
}
