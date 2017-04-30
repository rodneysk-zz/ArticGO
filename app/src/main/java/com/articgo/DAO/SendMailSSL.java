package com.articgo.DAO;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.articgo.control.ControlaEventos;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailSSL extends AsyncTask<String, Integer, Void> {

    private String emailDestino, titulo, mensagem;
    private Context context;
    private ProgressDialog progressDialog;
    private ControlaEventos controlaEventos;

    public SendMailSSL(String emailDestino, String titulo, String mensagem, Context context, ProgressDialog progressDialog) {
        this.emailDestino = emailDestino;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.context = context;
        this.progressDialog = progressDialog;

        controlaEventos = new ControlaEventos();
    }

    @Override
    protected Void doInBackground(String... params) {

        //GMAIL
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "*");
        props.put("mail.smtp.host", "74.125.198.108");//"smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        try{

            Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("articifes@gmail.com","NASA123456");
                    }
                });

            session.setProtocolForAddress("rfc822", "smtp");

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("articifes@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestino));
            message.setSubject(titulo);
            message.setContent(message, "text/plain");
            message.setText(mensagem);

            //Enviar mensagem
            Transport.send(message);

        }catch(Exception exc) {
            Toast.makeText(context, "Ocorreu um erro ao enviar seu e-mail. Confira se os dados estão corretos!", Toast.LENGTH_LONG).show();
        }

        controlaEventos.fechaProgressDialog(progressDialog);

        return null;
    }
}