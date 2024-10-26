package com.satyam.roomdemo.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SubscriberDAO {
    @Insert
    suspend fun insertSubscriber(suscriber: Subscriber):Long

    @Update
    suspend fun updateSubscriber(suscriber: Subscriber)

    @Delete
    suspend fun deleteSubscriber(suscriber: Subscriber)

    @Query("Delete FROM subscriber_data_table")
    suspend fun deleteAll(){}

    @Query("SELECT * FROM subscriber_data_table")
    fun getAllSubscribers(): LiveData<List<Subscriber>>


}