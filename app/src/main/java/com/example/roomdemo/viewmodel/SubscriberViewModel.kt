package com.example.roomdemo.viewmodel

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdemo.db.Subscriber
import com.example.roomdemo.repository.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel(), Observable {

    val subscribers = repository.subscribers

    private var isUpdateOrDelete= false
    private lateinit var subscriberToUpdateOrDelete:Subscriber

    @Bindable
    val inputName = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>();

    val message : LiveData<Event<String>>
        get()= statusMessage

    init{
        saveOrUpdateButtonText.value="Save"
        clearAllOrDeleteButtonText.value="Clear All"
    }

    fun saveOrUpdate(){
        if(isUpdateOrDelete) {
            subscriberToUpdateOrDelete.name= inputName.value!!
            subscriberToUpdateOrDelete.email= inputEmail.value!!
            update(subscriberToUpdateOrDelete)
            //clear inputs after saving
            inputName.value = null
            inputEmail.value = null

        }
        else{
            val name = inputName.value!!
            val email = inputEmail.value!!
            insert(Subscriber(0, name, email))

            //clear inputs after saving
            inputName.value = null
            inputEmail.value = null
        }

    }

    fun clearAllOrDelete(){
        if(isUpdateOrDelete){
            delete(subscriberToUpdateOrDelete)
        }
        else{
        clearAll()
        }
    }

    fun insert(subscriber: Subscriber):Job=
       viewModelScope.launch {
           val Id= repository.insert(subscriber)//if the insertion was not successful the return value will be -1
           statusMessage.value= Event("Subscriber Inserted Successfully With id"+ Id)
        }

    fun update(subscriber: Subscriber):Job =
        viewModelScope.launch {
           val numberOfUpdatedRow= repository.update(subscriber)
            inputEmail.value= ""
            inputName.value = ""
            isUpdateOrDelete= false
            subscriberToUpdateOrDelete=subscriber
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value= "Clear All"
            statusMessage.value= Event("Subscriber Updated Successfully"+ numberOfUpdatedRow)
        }
    fun delete(subscriber: Subscriber):Job=
        viewModelScope.launch {
            repository.delete(subscriber)
            inputEmail.value= ""
            inputName.value = ""
            isUpdateOrDelete= false
            subscriberToUpdateOrDelete=subscriber
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value= "Clear All"
            statusMessage.value= Event("Subscriber Deleted Successfully")
        }

    fun initUpdateAndDelete(subscriber: Subscriber){
        inputEmail.value= subscriber.email
        inputName.value = subscriber.name
        isUpdateOrDelete=true
        subscriberToUpdateOrDelete=subscriber
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value= "Delete"

    }

    fun clearAll():Job= viewModelScope.launch {
            repository.deleteAll()
        }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }


}