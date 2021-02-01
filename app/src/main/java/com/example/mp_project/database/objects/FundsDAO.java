package com.example.mp_project.database.objects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FundsDAO {

    @Insert
    void insert(Funds item);

    @Query("DELETE FROM funds_table")
    void deleteAll();

    @Query("SELECT Amount from funds_table where from_id = :id_1 and to_id = :id_2 ORDER BY id ASC")
    double AmountOwedToUser(int id_1, int id_2);

    @Query("SELECT SUM(Amount) from funds_table where from_id = :id")
    double IOwe(int id);

    @Query("SELECT sum(Amount) from funds_table where to_id = :id ORDER BY id ASC")
    double AmountOwedToId(int id);

    @Query("SELECT * from funds_table where from_id = :id ORDER BY id ASC")
    List<Funds> ListItems(int id);

    @Delete
    void delete(Funds item);

    @Query("update funds_table set  Amount = Amount + :amount where from_id = :id")
    void updateFundAdded(int id, double amount);

    @Query("SELECT count(*) from funds_table where from_id = :id ORDER BY id ASC")
    int fundsCount(int id);

    @Update
    void update(Funds... item);

    @Query("SELECT count(*) from funds_table ORDER BY id ASC")
    int count();
}
