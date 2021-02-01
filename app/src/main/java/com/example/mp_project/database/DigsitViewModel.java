package com.example.mp_project.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.mp_project.database.objects.DigsitItem;
import com.example.mp_project.database.objects.Funds;

import java.util.List;

public class DigsitViewModel extends AndroidViewModel {
    private DigsitRepository mRepository;
    private LiveData<List<DigsitItem>> all_task_items;
    private LiveData<List<DigsitItem>> all_funds_items;
    private LiveData<List<DigsitItem>> all_grocery_items;
    private LiveData<List<DigsitItem>> all_uncomplete_tasks;
    private LiveData<List<DigsitItem>> all_complete_tasks;
    private LiveData<List<DigsitItem>> all_uncomplete_groceries;
    private LiveData<List<DigsitItem>> all_complete_groceries;
    private MutableLiveData<List<DigsitItem>> searchResults;
    private MutableLiveData<Double> i_owe_result;
    private MutableLiveData<String> username_result;
    private MutableLiveData<List<Funds>> funds_list;
    public DigsitViewModel(Application application) {
        super(application);
        mRepository = new DigsitRepository(application);
        all_task_items = mRepository.getAllTaskItems();
        all_funds_items = mRepository.getAllFundItems();
        all_grocery_items = mRepository.getAllGroceryItems();
        all_uncomplete_tasks = mRepository.getAllUncompleteTaskItems();
        all_complete_tasks = mRepository.getAllCompleteTaskItems();
        all_uncomplete_groceries = mRepository.getAllUnompleteGroceryItems();
        all_complete_groceries = mRepository.getAllCompleteGroceryItems();
        searchResults = mRepository.getSearchResults();
        i_owe_result = mRepository.getIOwe();
        funds_list = mRepository.getFundsItemsResult();
        username_result = mRepository.getUserName();
    }

    public void findUsername(int id) {
        mRepository.getUserName(id);
    }


    public void findCalendarItems(String due_date) {
        mRepository.getCalendarItems(due_date);
    }

    public void getIOwe(int id) {
        mRepository.getIOweValue(id);
    }

    public void getFunds(int id) {
        mRepository.getFundsItems(id);
    }

    //Returns all of the tasks
    public LiveData<List<DigsitItem>> getAllTaskItems() {
        return all_task_items;
    }
    //Returns all of the tasks
    public LiveData<List<DigsitItem>> getAllUncompleteTaskItems() {
        return all_uncomplete_tasks;
    }
    //Returns all of the tasks
    public LiveData<List<DigsitItem>> getAllCompleteTaskItems() {
        return all_complete_tasks;
    }
    //Returns all of the tasks
    public LiveData<List<DigsitItem>> getAllCompleteGroceryItems() {
        return all_complete_groceries;
    }
    //Returns all of the tasks
    public LiveData<List<DigsitItem>> getAllUnompleteGroceryItems() {
        return all_uncomplete_groceries;
    }
    //Returns all of the grocery items
    public LiveData<List<DigsitItem>> getAllGroceryItems() {
        return all_grocery_items;
    }

    //Returns all of the funds items
    public LiveData<List<DigsitItem>> getAllFundsItems() {
        return all_funds_items;
    }

    public void insertDigsitItem(DigsitItem digsitItem) {
        mRepository.insertDigsitItem(digsitItem);
    }

    public void deleteDigsitItem(DigsitItem digsitItem) {
        mRepository.deleteDigsitItem(digsitItem);
    }

    public void updateDigsitItem(DigsitItem digsitItem) {
        mRepository.updateDigsitItem(digsitItem);
    }

    public void updateFundsItem(int from_id, double amount) {
        mRepository.updateFundsItem(from_id, amount);
    }
    public void updateFund(Funds fund) {
        mRepository.updateFundItem(fund);
    }
    public MutableLiveData<List<DigsitItem>> getCalendarItems() {
        return searchResults;
    }
    public MutableLiveData<List<Funds>> getFundsList() {
        return funds_list;
    }
    public MutableLiveData<Double> getIOwe() {
        return i_owe_result;
    }
    public MutableLiveData<String> getName() {
        return username_result;
    }
}
