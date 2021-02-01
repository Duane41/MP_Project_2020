package com.example.mp_project.database.objects;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert
    void insert(User item);

    @Query("DELETE FROM users_table")
    void deleteAll();

    @Query("SELECT * from users_table ORDER BY id ASC")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT count(*) from users_table ORDER BY id ASC")
    int count();

    @Delete
    void delete(User item);

    @Update
    void update(User... item);

    @Query("SELECT UserName from users_table where id = :user_id ORDER BY id ASC")
    String user_name(int user_id);
}
