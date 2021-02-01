package com.example.mp_project.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mp_project.database.objects.DigsitDao;
import com.example.mp_project.database.objects.DigsitItem;
import com.example.mp_project.database.objects.Funds;
import com.example.mp_project.database.objects.FundsDAO;
import com.example.mp_project.database.objects.User;
import com.example.mp_project.database.objects.UserDAO;

import java.util.List;

public class DigsitRepository {
    private DigsitDao local_digsitDao;
    private UserDAO local_userDao;
    private FundsDAO local_fundsDao;

    //Digsit items
    private LiveData<List<DigsitItem>> all_task_items;
    private LiveData<List<DigsitItem>> all_fund_items;
    private LiveData<List<DigsitItem>> all_grocery_items;

    private LiveData<List<DigsitItem>> all_uncomplete_tasks;
    private LiveData<List<DigsitItem>> all_complete_tasks;
    private LiveData<List<DigsitItem>> all_complete_groceries;
    private LiveData<List<DigsitItem>> all_uncomplete_groceries;

    private MutableLiveData<List<DigsitItem>> searchResults = new MutableLiveData<>();
    private MutableLiveData<Double> i_owe_value = new MutableLiveData<>();
    private MutableLiveData<String> user_name = new MutableLiveData<>();
    //User items
    private MutableLiveData<List<Funds>> funds_items = new MutableLiveData<>();
    DigsitRepository(Application application) {
        DigsitRoomDatabase db = DigsitRoomDatabase.getDatabase(application);
        local_digsitDao = db.digsitDao();
        local_userDao = db.userDAO();
        local_fundsDao = db.fundsDAO();

        //Digsit items
        all_task_items = local_digsitDao.getAllTasks();
        all_fund_items = local_digsitDao.getAllFunds();
        all_grocery_items = local_digsitDao.getAllGroceries();

        all_uncomplete_tasks = local_digsitDao.getAllTasksUncomplete();
        all_complete_tasks = local_digsitDao.getAllTasksComplete();
        all_complete_groceries = local_digsitDao.getAllGroceriesComplete();
        all_uncomplete_groceries = local_digsitDao.getAllGroceriesUncomplete();

    }
    //These will set the values of the respective variables once they are found
    private void asyncFinished(List<DigsitItem> results) {
        searchResults.setValue(results);
    }
    private void asyncFinishedCalcIOwe(double result) {
        i_owe_value.setValue(result);
    }
    private void asyncFundsList(List<Funds> result) {
        funds_items.setValue(result);
    }
    private void asyncUsername(String result) {
        user_name.setValue(result);
    }
    //Digist items
    LiveData<List<DigsitItem>> getAllCompleteGroceryItems() {
        return all_complete_groceries;
    }

    LiveData<List<DigsitItem>> getAllUnompleteGroceryItems() {
        return all_uncomplete_groceries;
    }
    LiveData<List<DigsitItem>> getAllTaskItems() {
        return all_task_items;
    }

    LiveData<List<DigsitItem>> getAllUncompleteTaskItems() {
        return all_uncomplete_tasks;
    }

    LiveData<List<DigsitItem>> getAllCompleteTaskItems() {
        return all_complete_tasks;
    }

    LiveData<List<DigsitItem>> getAllFundItems() {
        return all_fund_items;
    }

    LiveData<List<DigsitItem>> getAllGroceryItems() {
        return all_grocery_items;
    }

    public MutableLiveData<Double> getIOwe() {
        return i_owe_value;
    }
    public MutableLiveData<String> getUserName() {
        return user_name;
    }
    public void getUserName(int id) {
        GetNameAsyncTask task = new GetNameAsyncTask(local_userDao);
        task.delegate = this;
        task.execute(id);
    }
    //Gets list async
    private static class GetNameAsyncTask extends AsyncTask<Integer, Void, String> {

        private UserDAO user_dao;
        private DigsitRepository delegate = null;

        GetNameAsyncTask(UserDAO dao) {
            user_dao = dao;
        }

        @Override
        protected String doInBackground(Integer... params) {
            return user_dao.user_name(params[0].intValue());
        }

        @Override
        protected void onPostExecute(String result) {
            delegate.asyncUsername(result);
        }
    }

    public void getIOweValue(int id) {
        CalcIOweAsyncTask task = new CalcIOweAsyncTask(local_fundsDao);
        task.delegate = this;
        task.execute(id);
    }
    //Gets list async
    private static class CalcIOweAsyncTask extends AsyncTask<Integer, Void, Double> {

        private FundsDAO funds_dao;
        private DigsitRepository delegate = null;

        CalcIOweAsyncTask(FundsDAO dao) {
            funds_dao = dao;
        }

        @Override
        protected Double doInBackground(Integer... params) {
            return funds_dao.IOwe(params[0].intValue());
        }

        @Override
        protected void onPostExecute(Double result) {
            delegate.asyncFinishedCalcIOwe(result.doubleValue());
        }
    }


    public void getCalendarItems (String date) {
        ListAsyncTask task = new ListAsyncTask(local_digsitDao);
        task.delegate = this;
        task.execute(date);
    }
    //Gets list async
    private static class ListAsyncTask extends
            AsyncTask<String, Void, List<DigsitItem>> {

        private DigsitDao digsit_dao;
        private DigsitRepository delegate = null;

        ListAsyncTask(DigsitDao dao) {
            digsit_dao = dao;
        }

        @Override
        protected List<DigsitItem> doInBackground(final String... params) {
            return digsit_dao.getAllCalendar(params[0]);
        }

        @Override
        protected void onPostExecute(List<DigsitItem> result) {
            delegate.asyncFinished(result);
        }
    }

    public MutableLiveData<List<Funds>> getFundsItemsResult() {
        return funds_items;
    }

    public void getFundsItems (int id) {
        ListFundsAsyncTask task = new ListFundsAsyncTask(local_fundsDao);
        task.delegate = this;
        task.execute(id);
    }
    //Gets list async
    private static class ListFundsAsyncTask extends AsyncTask<Integer, Void, List<Funds>> {

        private FundsDAO funds_DAO;
        private DigsitRepository delegate = null;

        ListFundsAsyncTask(FundsDAO dao) {
            funds_DAO = dao;
        }

        @Override
        protected List<Funds> doInBackground(final Integer... params) {
            return funds_DAO.ListItems(params[0].intValue());
        }

        @Override
        protected void onPostExecute(List<Funds> result) {
            delegate.asyncFundsList(result);
        }
    }

    public MutableLiveData<List<DigsitItem>> getSearchResults() {
        return searchResults;
    }

    //Code for deleting a digsit item
    public void deleteDigsitItem(DigsitItem digsit_item)  {
        new deleteDigsitItemAsyncTask(local_digsitDao).execute(digsit_item);
    }

    private static class deleteDigsitItemAsyncTask extends AsyncTask<DigsitItem, Void, Void> {
        private DigsitDao mAsyncTaskDao;

        deleteDigsitItemAsyncTask(DigsitDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DigsitItem... params) {
            mAsyncTaskDao.deleteWord(params[0]);
            return null;
        }
    }

    //Code for deleting a user item
    public void deleteUserItem(User item)  {
        new deleteUserItemAsyncTask(local_userDao).execute(item);
    }

    private static class deleteUserItemAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO mAsyncDao;

        deleteUserItemAsyncTask(UserDAO dao) {
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncDao.delete(params[0]);
            return null;
        }
    }

    //Delete fund item
    public void deleteFundItem(Funds item)  {
        new deleteFundsItemAsyncTask(local_fundsDao).execute(item);
    }

    private static class deleteFundsItemAsyncTask extends AsyncTask<Funds, Void, Void> {
        private FundsDAO mAsyncDao;

        deleteFundsItemAsyncTask(FundsDAO dao) {
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final Funds... params) {
            mAsyncDao.delete(params[0]);
            return null;
        }
    }

    //Code for inseting to the DB
    public void insertDigsitItem (DigsitItem digsit_item) {
        new insertDigsitItemAsyncTask(local_digsitDao).execute(digsit_item);
    }

    private static class insertDigsitItemAsyncTask extends AsyncTask<DigsitItem, Void, Void> {

        private DigsitDao mAsyncTaskDao;

        insertDigsitItemAsyncTask(DigsitDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DigsitItem... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    //Code for inserting to the DB
    public void insertFundItem (Funds item) {
        new insertFundsAsyncTask(local_fundsDao).execute(item);
    }

    private static class insertFundsAsyncTask extends AsyncTask<Funds, Void, Void> {

        private FundsDAO mAsyncTaskDao;

        insertFundsAsyncTask(FundsDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Funds... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    //Code for inserting to the DB
    public void insertUserItem (User item) {
        new insertUserAsyncTask(local_userDao).execute(item);
    }

    private static class insertUserAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDAO mAsyncTaskDao;

        insertUserAsyncTask(UserDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    //Code for updating an entry
    public void updateDigsitItem (DigsitItem digsit_item) {
        new updateAsyncTaskDigsit(local_digsitDao).execute(digsit_item);
    }

    private static class updateAsyncTaskDigsit extends AsyncTask<DigsitItem, Void, Void> {

        private DigsitDao mAsyncTaskDao;

        updateAsyncTaskDigsit(DigsitDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DigsitItem... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    //Code for updating an entry
    public void updateUserItem (User item) {
        new updateAsyncUser(local_userDao).execute(item);
    }

    private static class updateAsyncUser extends AsyncTask<User, Void, Void> {

        private UserDAO mAsyncDao;

        updateAsyncUser(UserDAO dao) {
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncDao.update(params[0]);
            return null;
        }
    }

    //Code for updating an entry
    public void updateFundsItem (int from_id, double amount) {
        new updateAsyncFunds(local_fundsDao, amount).execute(from_id);
    }

    private static class updateAsyncFunds extends AsyncTask<Integer, Void, Void> {

        private FundsDAO mAsyncDao;
        private Double Amount;
        updateAsyncFunds(FundsDAO dao, double amount) {
            mAsyncDao = dao;
            Amount = amount;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncDao.updateFundAdded(params[0].intValue(), Amount * 1/(mAsyncDao.fundsCount(params[0].intValue()) + 1));
            return null;
        }
    }


    //Code for updating an entry
    public void updateFundItem (Funds fund) {
        new updateAsyncFund(local_fundsDao).execute(fund);
    }

    private static class updateAsyncFund extends AsyncTask<Funds, Void, Void> {

        private FundsDAO mAsyncDao;

        updateAsyncFund(FundsDAO dao) {
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(final Funds... params) {
            mAsyncDao.update(params[0]);
            return null;
        }
    }
}
