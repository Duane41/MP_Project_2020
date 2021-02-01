package com.example.mp_project.database.objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "digsit_table") //desctibes the table it associates to
public class DigsitItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String Description;

    private String DueDate;

    private double Amount;

    private boolean Task;

    private boolean Groc;

    private boolean ZAR;

    private boolean Complete;

    //This allows for an audit trail
    private boolean Deleted;

    //Constructor for task
    @Ignore
    public DigsitItem(@NonNull String Description, String DueDate, boolean Task) {
        this.Description = Description;
        this.DueDate = DueDate;
        this.Task = Task;
    }
    @Ignore
    public DigsitItem(@NonNull String Description, String DueDate, boolean Task, boolean Groc) {
        this.Description = Description;
        this.DueDate = DueDate;
        this.Task = Task;
        this.Groc = Groc;
    }
    @Ignore
    public DigsitItem(@NonNull String Description, String DueDate, double Amount, boolean Task, boolean Groc, boolean ZAR) {
        this.Description = Description;
        this.DueDate = DueDate;
        this.Amount = Amount;
        this.Task = Task;
        this.Groc = Groc;
        this.ZAR = ZAR;
    }
    @Ignore
    public DigsitItem(@NonNull String Description, String DueDate, boolean Task, boolean Groc, boolean ZAR) {
        this.Description = Description;
        this.DueDate = DueDate;
        this.Task = Task;
        this.Groc = Groc;
        this.ZAR = ZAR;
        if (ZAR && Groc) {
            this.Complete = true;
        }
    }
    @Ignore
    public DigsitItem(int id, @NonNull String Description, String DueDate, double Amount, boolean Task, boolean Groc, boolean ZAR, boolean Complete, boolean Deleted) {
        this.id = id;
        this.Description = Description;
        this.DueDate = DueDate;
        this.Amount = Amount;
        this.Task = Task;
        this.Groc = Groc;
        this.ZAR = ZAR;
        this.Complete = Complete;
        this.Deleted = Deleted;
    }

    public DigsitItem(@NonNull String Description, String DueDate, double Amount, boolean Task, boolean Groc, boolean ZAR, boolean Complete, boolean Deleted) {
        this.Description = Description;
        this.DueDate = DueDate;
        this.Amount = Amount;
        this.Task = Task;
        this.Groc = Groc;
        this.ZAR = ZAR;
        this.Complete = Complete;
        this.Deleted = Deleted;
    }

    //Getter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setZAR(boolean ZAR) {
        this.ZAR = ZAR;
    }

    public boolean isComplete() {
        return Complete;
    }

    public void setComplete(boolean complete) {
        Complete = complete;
    }

    @NonNull
    public String getDescription() {
        return Description;
    }

    public void setDescription(@NonNull String description) {
        Description = description;
    }

    public String getDueDate() {
        return this.DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public double getAmount() {
        return this.Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public void setTask(boolean task) {
        this.Task = task;
    }

    public void setGroc(boolean groc) {
        this.Groc = groc;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }

    public boolean isTask() {
        return this.Task;
    }

    public boolean isGroc() {
        return this.Groc;
    }

    public boolean isZAR() {
        return this.ZAR;
    }
}