package com.example.mp_project.database.objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "funds_table")
public class Funds {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private int from_id;

    @NonNull
    private int to_id;

    @NonNull
    private double Amount;

    private String Name;

    public Funds(@NonNull int from_id, @NonNull int to_id, @NonNull double Amount) {
        this.from_id = from_id;
        this.to_id = to_id;
        this.Amount = Amount;
    }

    @Ignore
    public Funds(int id, @NonNull int from_id, @NonNull int to_id, @NonNull double Amount) {
        this.id = id;
        this.from_id = from_id;
        this.to_id = to_id;
        this.Amount = Amount;
    }
    @Ignore
    public Funds( @NonNull int from_id, @NonNull int to_id, @NonNull double Amount, String Name) {
        this.from_id = from_id;
        this.to_id = to_id;
        this.Amount = Amount;
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFrom_id() {
        return from_id;
    }

    public void setFrom_id(int from_id) {
        this.from_id = from_id;
    }

    public int getTo_id() {
        return to_id;
    }

    public void setTo_id(int to_id) {
        this.to_id = to_id;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }
}