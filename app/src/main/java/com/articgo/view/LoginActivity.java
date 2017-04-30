package com.articgo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.articgo.R;
import com.articgo.control.ControlaEventos;
import com.articgo.control.ControlaFirebase;

public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView txtEsqueceuSenha;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton, btnCadastrar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ControlaFirebase controlaFireBase;
    private ControlaEventos controlaEventos;
    private ProgressDialog progressDialog;
    private String email;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //------------------ Monitora o Estado do usuário no FireBase ------------------------------
        controlaFireBase = new ControlaFirebase();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                //Se o usuário estiver logado no dispositivo, entao abrir o app
                if (user != null) {

                    //Iniciar app
                    controlaFireBase.carregaDadosJogadorAoLogar(LoginActivity.this, user.getUid());
                }
                else {
                    setContentView(R.layout.activity_login);

                    //Dados do Usuário
                    mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
                    mPasswordView = (EditText) findViewById(R.id.password);

                    //Botão Entrar
                    mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
                    mEmailSignInButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Entrar();
                        }
                    });

                    //Botão Esqueceu sua senha
                    txtEsqueceuSenha = (TextView) findViewById(R.id.txtEsqueceuSenha);
                    txtEsqueceuSenha.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            recuperarSenha();
                        }
                    });

                    if (savedInstanceState != null) {
                        email = (String) savedInstanceState.get("email");
                        mEmailView.setText(email);

                    }
                    else if (getIntent() != null && getIntent().getExtras() != null) {
                        email = (String) getIntent().getExtras().get("email");
                        mEmailView.setText(email);
                    }

                    controlaEventos = new ControlaEventos();

                    //Botão Cadastrar
                    btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
                    btnCadastrar.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    mLoginFormView = findViewById(R.id.login_form);
                    mProgressView = findViewById(R.id.login_progress);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //================================== RECUPERAR SENHA ===========================================
    private void recuperarSenha(){

        LayoutInflater li = getLayoutInflater();

        final View vW = li.inflate(R.layout.dialog_recuperar_senha, null);

        //AlertDialog de Recuperação de senha
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setView(vW);
        final AlertDialog dialog = builder.create();
        dialog.show();

        //--------------------------- EVENTO DO BOTAO CONFIRMAR ------------------------------------
        vW.findViewById(R.id.btnConfirmar).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText txtEmail = (EditText) vW.findViewById(R.id.textoEmail);
                String emailAddress = txtEmail.getText().toString();

                //Validar E-mail
                if (!emailValido(emailAddress)) {
                    txtEmail.setError(getString(R.string.error_invalid_email));
                    txtEmail.requestFocus();
                }
                else {

                    progressDialog = progressDialog.show(vW.getContext(), "Olá", "Aguarde enquanto seus dados são validados");

                    mAuth = FirebaseAuth.getInstance();
                    mAuth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    controlaEventos.fechaProgressDialog(progressDialog);

                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Sucesso! Abra sua caixa de e-mail para efetivar a redefinição de senha", Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    } else
                                        Toast.makeText(getApplicationContext(), "Erro! Não há registro desse usuário", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        // -------------------------- EVENTO DO BOTAO CANCELAR -------------------------------------
        vW.findViewById(R.id.btnCancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //================================== LOGAR USUÁRIO =============================================
    private void Entrar() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Verifique se há uma senha válida, se o usuário digitar uma.
        if (password.isEmpty() || !senhaCorreta(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        //Verifique se há um endereço de e-mail válido.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        else if (!emailValido(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) focusView.requestFocus();
        else controlaFireBase.logarCliente(email, password, this, mAuth, mEmailView, mPasswordView);
    }

    //================================== VALIDAR USUÁRIO ===========================================
    private boolean emailValido(String email) {

        return email.contains("@");
    }

    private boolean senhaCorreta(String senha) {
        
        return senha.length() > 5;
    }

    //================================== BARRA DE PROGRESSO ========================================
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgressBar(final boolean show) {

        // No Honeycomb MR2 temos as APIs ViewPropertyAnimator, que permitem
        // para animações muito fáceis. Se disponível, use estas APIs para desaparecer
        // o spinner de progresso.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}