package com.articgo.control;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.articgo.DAO.FireBase;
import com.articgo.DAO.SendMailSSL;
import com.articgo.R;
import com.articgo.domain.Jogador;
import com.articgo.view.CadastroActivity;

/**
 * Created by root on 12/03/17.
 */

public class ControlaEventos {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Context context;

    public ControlaEventos() {
    }

    public ControlaEventos(AutoCompleteTextView mEmailView, EditText mPasswordView, Context context) {
        this.mEmailView = mEmailView;
        this.mPasswordView = mPasswordView;
        this.context = context;
    }

    //======================== FECHA A BARRA DE PROGRESSO ==========================================
    public void fechaProgressDialog(ProgressDialog progressDialog){

        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    //======================== EXIBE EXCESSAO ======================================================
    public void excecaoFirebase(int codExcecao){

        if(codExcecao == -1446840658) {
            mEmailView.setError("Não há registro desse usuário");
            mEmailView.requestFocus();
        }
        else if(codExcecao == 787766007) {
            mEmailView.setError("O endereço de e-mail está mal formatado");
            mEmailView.requestFocus();
        }
        else if(codExcecao == -1710700802) {
            mPasswordView.setError("Senha inválida");
            mPasswordView.requestFocus();
        }
        else if(codExcecao == 1980419389) {
            Toast.makeText(context, "Bloqueamos todas as solicitações deste dispositivo devido a uma atividade incomum. Aguarde 10 segundos", Toast.LENGTH_LONG).show();
        }
        else if(codExcecao == -1817786574) {
            mEmailView.setError("O endereço de e-mail já está em uso por outra conta");
            mEmailView.requestFocus();
        }
    }

    //======================== ENVIA E-MAIL ========================================================
    public void enviarEmail(String emailDestino, String titulo, String mensagem, Context context, ProgressDialog progressDialog) {
        SendMailSSL s = new SendMailSSL(emailDestino, titulo, mensagem, context, progressDialog);
        s.execute("");
    }

    //======================= CONFIRMAR CÓDIGO DE CADASTRO =========================================
    public void confirmarCodigo(final String  codigo, final Context context, final String senha, final Jogador cliente, final ControlaEventos controlaEventos){


        LayoutInflater li = ((CadastroActivity) context).getLayoutInflater();
        final View vW = li.inflate(R.layout.dialog_confirmar_cadastro, null);

        //AlertDialog de Recuperação de senha
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(vW);
        final AlertDialog dialog = builder.create();
        dialog.show();

        //--------------------------- EVENTO DO BOTAO CONFIRMAR ------------------------------------
        vW.findViewById(R.id.btnConfirmar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txtCodigo = ((EditText) vW.findViewById(R.id.txtCodigo)).getText().toString();

                if(codigo.equals(txtCodigo)) {

                    //Cadastra novo cliente no FireBase
                    FireBase fB = new FireBase();
                    fB.cadastrarCliente(context, cliente, senha, dialog, controlaEventos);

                }
                else {
                    ((EditText) vW.findViewById(R.id.txtCodigo)).setError("Código Inválido");
                }
            }
        });

    }

}
