package com.articgo.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by root on 09/03/17.
 */
@IgnoreExtraProperties
public class Conquista implements Parcelable {

    private int id;
    private double latitude, longitude;
    private String descricao, titulo;

    public Conquista() {
    }

    public Conquista(int id, String titulo, String descricao, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.descricao = descricao;
        this.titulo = titulo;
        this.id = id;
    }

    public Conquista(double latitude, double longitude, String descricao, String titulo) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.descricao = descricao;
        this.titulo = titulo;
    }

    protected Conquista(Parcel in) {
        id = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        descricao = in.readString();
        titulo = in.readString();
    }

    public static final Creator<Conquista> CREATOR = new Creator<Conquista>() {
        @Override
        public Conquista createFromParcel(Parcel in) {
            return new Conquista(in);
        }

        @Override
        public Conquista[] newArray(int size) {
            return new Conquista[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(descricao);
        dest.writeString(titulo);
    }
}
