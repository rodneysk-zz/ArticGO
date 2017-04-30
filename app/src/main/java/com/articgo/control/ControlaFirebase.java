package com.articgo.control;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.articgo.DAO.FireBase;

/**
 * Created by root on 12/03/17.
 */

public class ControlaFirebase {

    private FireBase FB;

    public ControlaFirebase() {
        FB = new FireBase();
    }

    public void logarCliente(String email, String senha, final Context context, FirebaseAuth mAuth, final AutoCompleteTextView mEmailView, EditText mPasswordView) {
        FB.logarCliente(email,senha,context,mAuth,mEmailView,mPasswordView);
    }

    public void carregaDadosJogadorAoLogar(Context context, String idJogador){
        FB.carregaDadosJogadorAoLogar(context,idJogador);
    }

}
