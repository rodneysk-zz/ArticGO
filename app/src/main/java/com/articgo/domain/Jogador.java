package com.articgo.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by root on 09/03/17.
 */

@IgnoreExtraProperties
public class Jogador implements Parcelable {

    private String id, nome, email;
    private int  qtdPontos;
    private List<Conquista> listaConquistas;

    public Jogador(String nome, String email, int qtdPontos, List<Conquista> listaConquistas) {
        this.nome = nome;
        this.email = email;
        this.qtdPontos = qtdPontos;
        this.listaConquistas = listaConquistas;
    }

    public Jogador(String id, String nome, String email, int qtdPontos, List<Conquista> listaConquistas) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.qtdPontos = qtdPontos;
        this.listaConquistas = listaConquistas;
    }

    public Jogador(String email, String id, List<Conquista> listaConquistas, String nome, int qtdPontos) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.qtdPontos = qtdPontos;
        this.listaConquistas = listaConquistas;
    }

    protected Jogador(Parcel in) {
        id = in.readString();
        nome = in.readString();
        email = in.readString();
        qtdPontos = in.readInt();
        listaConquistas = in.createTypedArrayList(Conquista.CREATOR);
    }

    public static final Creator<Jogador> CREATOR = new Creator<Jogador>() {
        @Override
        public Jogador createFromParcel(Parcel in) {
            return new Jogador(in);
        }

        @Override
        public Jogador[] newArray(int size) {
            return new Jogador[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getQtdPontos() {
        return qtdPontos;
    }

    public void setQtdPontos(int qtdPontos) {
        this.qtdPontos = qtdPontos;
    }

    public List<Conquista> getListaConquistas() {
        return listaConquistas;
    }

    public void setListaConquistas(List<Conquista> listaConquistas) {
        this.listaConquistas = listaConquistas;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nome);
        dest.writeString(email);
        dest.writeInt(qtdPontos);
        dest.writeTypedList(listaConquistas);
    }
}
