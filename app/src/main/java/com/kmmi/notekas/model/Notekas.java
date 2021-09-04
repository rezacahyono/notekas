package com.kmmi.notekas.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Notekas implements Parcelable {
    public Notekas(int id, int idUser, String typeInput, String title, String description, double amount, String date) {
        this.id = id;
        this.idUser = idUser;
        this.typeInput = typeInput;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }

    public Notekas() {

    }

    protected Notekas(Parcel in) {
        id = in.readInt();
        idUser = in.readInt();
        typeInput = in.readString();
        title = in.readString();
        description = in.readString();
        amount = in.readDouble();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idUser);
        dest.writeString(typeInput);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeDouble(amount);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notekas> CREATOR = new Creator<Notekas>() {
        @Override
        public Notekas createFromParcel(Parcel in) {
            return new Notekas(in);
        }

        @Override
        public Notekas[] newArray(int size) {
            return new Notekas[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getTypeInput() {
        return typeInput;
    }

    public void setTypeInput(String typeInput) {
        this.typeInput = typeInput;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private int id;
    private int idUser;
    private String typeInput;
    private String title;
    private String description;
    private double amount;
    private String date;
}
