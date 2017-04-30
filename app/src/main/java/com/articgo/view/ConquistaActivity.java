package com.articgo.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.articgo.R;
import com.articgo.control.ControlaFirebase;
import com.articgo.domain.Jogador;

import java.util.ArrayList;
import java.util.List;

public class ConquistaActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack{


    private ControlaFirebase controlaFirebase;
    private Jogador jogador;
    private RecyclerView minhaLista;
    private ConquistaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conquista);

        if(savedInstanceState != null){
            jogador = savedInstanceState.getParcelable("Jogador");
        }
        else {
            if (getIntent() != null && getIntent().getExtras() != null) {
                jogador = getIntent().getExtras().getParcelable("Jogador");
            }
        }

        controlaFirebase = new ControlaFirebase();
        minhaLista = (RecyclerView) findViewById(R.id.listaconquista);

        GridLayoutManager llm = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        minhaLista.setLayoutManager(llm);

        //ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Ranking");
        setSupportActionBar(toolbar);

        //Adapter
        adapter = new ConquistaAdapter(ConquistaActivity.this, jogador.getListaConquistas());
        adapter.setRecyclerViewOnClickListenerHack(ConquistaActivity.this);
        minhaLista.setAdapter(adapter);
    }

    @Override
    public void onClickListener(View view, int position) {
        Toast.makeText(this, jogador.getNome(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLongPressClickListener(View view, int position) {

    }
}
