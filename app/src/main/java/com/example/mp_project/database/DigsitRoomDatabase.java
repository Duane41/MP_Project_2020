package com.example.mp_project.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mp_project.database.objects.DigsitDao;
import com.example.mp_project.database.objects.DigsitItem;
import com.example.mp_project.database.objects.Funds;
import com.example.mp_project.database.objects.User;
import com.example.mp_project.database.objects.UserDAO;
import com.example.mp_project.database.objects.FundsDAO;
@Database(entities = {DigsitItem.class, User.class, Funds.class}, version = 8, exportSchema = false)
public abstract class DigsitRoomDatabase extends RoomDatabase {

    public abstract DigsitDao digsitDao();
    public abstract UserDAO userDAO();
    public abstract FundsDAO fundsDAO();
    private static DigsitRoomDatabase INSTANCE;

    static DigsitRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DigsitRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DigsitRoomDatabase.class, "digsit_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(AddDataCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback AddDataCallback = new RoomDatabase.Callback() {
                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final UserDAO users_dao;
        private final FundsDAO funds_dao;
        String[] users = {"Duane", "Chris", "Carl"};

        PopulateDbAsync(DigsitRoomDatabase db) {
            users_dao = db.userDAO();
            funds_dao = db.fundsDAO();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            if (users_dao.count() == 0) {
                for (int i = 0; i <= users.length - 1; i++) {
                    User word = new User(users[i]);
                    users_dao.insert(word);
                }

            }
                funds_dao.deleteAll();
                funds_dao.insert(new Funds(1,2,320.00,"Chris"));
                funds_dao.insert(new Funds(1, 3, -60.00, "Carl"));
                funds_dao.insert(new Funds(2, 1, -320.00));
                funds_dao.insert(new Funds(2, 3, 137));
                funds_dao.insert(new Funds(3, 1, 60));
                funds_dao.insert(new Funds(3, 2, -137));

            return null;
        }
    }
}