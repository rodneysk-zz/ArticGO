package com.articgo.DAO;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.articgo.domain.Conquista;
import com.articgo.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.articgo.control.ControlaEventos;
import com.articgo.domain.Jogador;
import com.articgo.view.CadastroActivity;
import com.articgo.view.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by root on 09/03/17.
 */

public class FireBase {

    private ControlaEventos controlaEventos;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    //====================== LOGAR CLIENTE =========================================================
    public void logarCliente(String email, String senha, final Context context, FirebaseAuth mAuth, final AutoCompleteTextView mEmailView, final EditText mPasswordView) {

        //ProgressDialog
        progressDialog = progressDialog.show(context, "Efetuando Login...", "Aguarde enquanto seus dados são carregados!");

        //Validando Usuário
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener((LoginActivity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        controlaEventos = new ControlaEventos(mEmailView,mPasswordView, context);
                        controlaEventos.fechaProgressDialog(progressDialog);

                        if (!task.isSuccessful()) {

                            Log.i("Erro", task.getException().getMessage() + " ---codigo: " + task.getException().getMessage().hashCode());
                            controlaEventos.excecaoFirebase(task.getException().getMessage().hashCode());
                        }
                        else{
                            Toast.makeText(context, "Seja Bem Vindo!", Toast.LENGTH_LONG).show();
                            //carregaDadosJogadorAoLogar(context,task.getResult().getUser().getUid(), 1);

                            //Abrir a Activity principal
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            ((LoginActivity) context).finish();

                        }
                    }
                });
    }

    //====================== CADASTRAR CLIENTE =====================================================
    public void cadastrarCliente(final Context context, final Jogador jogador, String senha, final AlertDialog alertDialog, final ControlaEventos c){

        //ProgressDialog
        progressDialog = progressDialog.show(context, "Efetuando Cadastro...", "Aguarde enquanto seus dados são carregados!");

        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(jogador.getEmail(), senha)
                .addOnCompleteListener((CadastroActivity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        c.fechaProgressDialog(progressDialog);
                        alertDialog.dismiss();

                        if (!task.isSuccessful()) {
                            Log.i("Erro", task.getException().getMessage() + " ---codigo: " + task.getException().getMessage().hashCode());
                            c.excecaoFirebase(task.getException().getMessage().hashCode());
                        }
                        else{

                            //--------------- Inserir o usuario no Database ------------------------
                            jogador.setId(mAuth.getCurrentUser().getUid());

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Jogador");
                            mDatabase.child(jogador.getId()).setValue(jogador);


                            //-------------- Abrir próxima tela ------------------------------------
                            Toast.makeText(context, jogador.getNome() + " seu cadastro foi concluído!", Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();
                            Intent intent = new Intent((CadastroActivity) context, MainActivity.class);
                            intent.putExtra("Jogador", jogador);
                            context.startActivity(intent);
                            ((CadastroActivity) context).finish();
                        }

                    }
                });
    }

    //============================ CARREGA DADOS DO JOGADOR  =======================================
    public void carregaDadosJogadorAoLogar(final Context context, String idJogador){

        //------------------- ACOMPANHANDO O PROCESSO DE UPLOAD ----------------------------
        progressDialog = progressDialog.show(context, "Olá", "Aguarde enquanto carregamos suas informações!");

        if(verificaConexao(context)) {

            mDatabase = FirebaseDatabase.getInstance().getReference().child("Jogador").child(idJogador);
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        Iterable it = dataSnapshot.getChildren();
                        Iterator iterator = it.iterator();
                        Jogador j = null;

                        if (iterator.hasNext()) {
                            j = new Jogador(
                                    ((DataSnapshot) iterator.next()).getValue(String.class),
                                    ((DataSnapshot) iterator.next()).getValue(String.class),
                                    carregaConquistas(((DataSnapshot) iterator.next()).getChildren().iterator()),
                                    ((DataSnapshot) iterator.next()).getValue(String.class),
                                    ((DataSnapshot) iterator.next()).getValue(int.class));


                            //----------------- Abrir a próxima tela -------------------------------
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("Jogador", j);

                            progressDialog.dismiss();
                            ((LoginActivity) context).finish();
                            context.startActivity(intent);

                        }
                        if (j == null) {
                            progressDialog.dismiss();
                            Toast.makeText(context.getApplicationContext(), "Não existem dados para este jogador!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context.getApplicationContext(), "Jogador não encontrado", Toast.LENGTH_SHORT).show();
                    //desconectarJogador();
                }
            });
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(context.getApplicationContext(), "Sua conexão com a internet não esta funcionando. Conecte-se para fazer o login!", Toast.LENGTH_LONG).show();
        }
    }

    //=========================== CARREGA MISSOES DO JOGADOR =======================================
    public List<Conquista> carregaConquistas(Iterator it){
        List<Conquista> listaConquistas = new ArrayList<>();
        Conquista conquista, aux;

        while(it.hasNext()){

            Iterable u = ((DataSnapshot) it.next()).getChildren();
            Iterator k = u.iterator();

            conquista = new Conquista();
                   /* ((DataSnapshot) k.next()).getValue(String.class),
                    ((DataSnapshot) k.next()).getValue(double.class),
                    ((DataSnapshot) k.next()).getValue(double.class));*/

            listaConquistas.add(conquista);

        }

        return listaConquistas;
    }

    //===================== VERIFICA CONEXÃO COM INTERNET ==========================================
    public boolean verificaConexao(Context context) {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }
}
