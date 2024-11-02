package com.satyam.roomdemo.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satyam.roomdemo.helper.Event
import com.satyam.roomdemo.room.Subscriber
import com.satyam.roomdemo.room.SubscriberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.Pattern

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel() {

    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete : Subscriber
    val subscribers = repository.subscribers
    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()
    val saveOrUpdateButtonText = MutableLiveData<String>()
    val clearAllOrDeleteButtonText = MutableLiveData<String>()
    private val statusMessage = MutableLiveData<Event<String>>()
    val message : LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun saveOrUpdate(){
        if (inputName.value==null){
            statusMessage.value = Event("Please Enter Subscriber's Name!")
        }
        else if (inputEmail.value==null){
            statusMessage.value = Event("Please Enter Subscriber's Email!")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
            statusMessage.value = Event("Please Enter Correct Email Address!")
        }else{
            if(isUpdateOrDelete){
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value!!
                update(subscriberToUpdateOrDelete)
            }
            else{
                val name = inputName.value!!
                val email = inputEmail.value!!
                insert(Subscriber(0,name,email))
                inputName.value=""
                inputEmail.value=""
            }
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

    fun insert(subscriber: Subscriber)= viewModelScope.launch(Dispatchers.IO) {
            val newRowId = repository.insert(subscriber)
        withContext(Dispatchers.Main){
            if(newRowId > -1){
                statusMessage.value=Event("Subscriber Inserted Successfully!")
            }
            else{
                statusMessage.value=Event("Error Occurred!")
            }
        }
        }


    fun update(subscriber: Subscriber)= viewModelScope.launch(Dispatchers.IO) {
        val numberOfRows = repository.update(subscriber)
        withContext(Dispatchers.Main){
            if (numberOfRows>0){
                inputName.value = ""
                inputEmail.value = ""
                isUpdateOrDelete =false
                saveOrUpdateButtonText.value = "Save"
                clearAllOrDeleteButtonText.value="ClearAll"
                statusMessage.value = Event("Subscriber Updated Successfully")
            }
            else{
                statusMessage.value = Event("Error Occurred !")
            }

        }
    }


    fun delete(subscriber: Subscriber)= viewModelScope.launch(Dispatchers.IO) {
        val numberOfRows = repository.delete(subscriber)
        withContext(Dispatchers.Main){
            if(numberOfRows>0){
                inputName.value = ""
                inputEmail.value = ""
                isUpdateOrDelete =false
                saveOrUpdateButtonText.value = "Save"
                clearAllOrDeleteButtonText.value="ClearAll"
                statusMessage.value = Event("Subscriber Deleted Successfully")
            }
            else{
                statusMessage.value = Event("Error Occurred!")
            }

        }
    }

    fun clearAll()= viewModelScope.launch(Dispatchers.IO) {
        val numberOfRows = repository.deleteAll()
        withContext(Dispatchers.Main){
            if (numberOfRows >0){
                statusMessage.value = Event("All Subscribers Deleted Successfully.")
            }
            else{
                statusMessage.value = Event("Error Occurred !")
            }

        }
    }

    fun initUpdateAndDelete(subscriber: Subscriber){
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete =true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value="Delete"
    }
}