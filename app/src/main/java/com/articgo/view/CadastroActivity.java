package com.articgo.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.articgo.domain.Conquista;
import com.google.firebase.auth.FirebaseAuth;
import com.articgo.R;
import com.articgo.control.ControlaEventos;
import com.articgo.domain.Jogador;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CadastroActivity extends AppCompatActivity {

    private Button btnCadastrar;
    private FirebaseAuth mAuth;
    private AutoCompleteTextView txtEmail;
    private EditText txtNome,  txtSenha, txtConfirmaSenha;
    private ControlaEventos controlaEventos;
    private ProgressDialog progressDialog;
    private String mensagem, codigo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Campos do Formulário
        txtNome = (EditText) findViewById(R.id.txtNome);
        txtEmail = (AutoCompleteTextView) findViewById(R.id.txtEmail);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        txtConfirmaSenha = (EditText) findViewById(R.id.txtConfirmaSenha);

        controlaEventos = new ControlaEventos(txtEmail, txtSenha, this);

        //Botao Cadastrar
        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });
    }

    //==================== EFETUAR CADASTRO DE USUÁRIO =============================================
    private void cadastrar(){

        boolean cancel = false;
        View focusView = null;

        //Validar Nome
        if(txtNome.getText().toString().isEmpty()){
            txtNome.setError("digite seu nome");
            focusView = txtNome;
            cancel = true;
        }
        //Validar E-mail
        else if(txtEmail.getText().toString().isEmpty()){
            txtEmail.setError("digite seu E-mail");
            focusView = txtEmail;
            cancel = true;
        }
        else if(!txtEmail.getText().toString().contains("@")){
            txtEmail.setError("E-mail inválido");
            focusView = txtEmail;
            cancel = true;
        }
        //Validar Senha
        else if(txtSenha.getText().toString().isEmpty()){
            txtSenha.setError("digite sua Senha");
            focusView = txtSenha;
            cancel = true;
        }
        else if(txtSenha.getText().toString().length() < 6){
            txtSenha.setError("Senha muito curta (mínimo 6 dígitos)");
            focusView = txtSenha;
            cancel = true;
        }
        //Validar Confirmar Senha
        else if(txtConfirmaSenha.getText().toString().isEmpty()){
            txtConfirmaSenha.setError("Confirme sua Senha");
            focusView = txtConfirmaSenha;
            cancel = true;
        }
        else if(!txtConfirmaSenha.getText().toString().equals(txtSenha.getText().toString())){
            txtConfirmaSenha.setError("Senhas Diferentes");
            focusView = txtConfirmaSenha;
            cancel = true;
        }
        if(cancel){
            focusView.requestFocus();
        }
        //Cadastrar Novo Usuário
        else {

            //Mensagem
            codigo = gerarCodigo();
            mensagem = "Olá " + txtNome.getText().toString() +"!\n\n" +
                    codigo + " é o seu código de registro no aplicativo Ártic GO. " +
                    "Digite este código no app para confirmar seu cadastro." +
                    "\n\nGratos, equipe Ártic GO.";


            //Enviar E-mail com código de confirmação
            progressDialog = progressDialog.show(this, "Enviando E-mail...", "Um código de confirmação de cadastro está sendo enviado!");
            controlaEventos.enviarEmail(txtEmail.getText().toString(), "Ártic GO - Confirmação de Cadastro", mensagem, this, progressDialog);

            //Abrir caixa de diálogo para digitar o código de confirmação e cadastrar jogador
            List<Conquista> l = new ArrayList<>();
            l.add(new Conquista(0,0, "Descrição - Hello Word", "Titulo - Hello Word"));
            Jogador c = new Jogador(txtNome.getText().toString(), txtEmail.getText().toString(), 0, l);
            controlaEventos.confirmarCodigo(codigo, this, txtSenha.getText().toString(), c, controlaEventos);

        }
    }

    //==================== GERAR CÓDIGO ============================================================
    private String gerarCodigo(){

        String codigo = "";
        Random gerador = new Random();

        for (int i = 0; i < 6; i++) {
            codigo += gerador.nextInt(10);
        }

        return codigo;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
