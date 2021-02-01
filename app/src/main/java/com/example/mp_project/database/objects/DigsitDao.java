package com.example.mp_project.database.objects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DigsitDao {

    @Insert
    void insert(DigsitItem digsit_item);

    @Query("DELETE FROM digsit_table")
    void deleteAll();

    @Query("SELECT * from digsit_table where Task = 1 ORDER BY id ASC")
    LiveData<List<DigsitItem>> getAllTasks();

    @Query("SELECT * from digsit_table where Task = 1 and Complete = 1 ORDER BY id ASC")
    LiveData<List<DigsitItem>> getAllTasksComplete();

    @Query("SELECT * from digsit_table where Task = 1 and Complete = 0 ORDER BY id ASC")
    LiveData<List<DigsitItem>> getAllTasksUncomplete();

    @Query("SELECT * from digsit_table where Groc = 1 and Complete = 1 ORDER BY id ASC")
    LiveData<List<DigsitItem>> getAllGroceriesComplete();

    @Query("SELECT * from digsit_table where Groc = 1 and Complete = 0 ORDER BY id ASC")
    LiveData<List<DigsitItem>> getAllGroceriesUncomplete();


    @Query("SELECT * from digsit_table where ZAR = 1 ORDER BY id ASC")
    LiveData<List<DigsitItem>> getAllFunds();

    @Query("SELECT * from digsit_table where Groc = 1 ORDER BY id ASC")
    LiveData<List<DigsitItem>> getAllGroceries();

    @Query("SELECT * from digsit_table where DueDate = :due_date and Complete = 0 order BY id ASC")
    List<DigsitItem> getAllCalendar(String due_date);

    @Delete
    void deleteWord(DigsitItem word);

    @Update
    void update(DigsitItem... word);
}
