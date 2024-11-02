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
    suspend fun updateSubscriber(suscriber: Subscriber):Int

    @Delete
    suspend fun deleteSubscriber(suscriber: Subscriber):Int

    @Query("Delete FROM subscriber_data_table")
    suspend fun deleteAll():Int

    @Query("SELECT * FROM subscriber_data_table")
    fun getAllSubscribers(): LiveData<List<Subscriber>>


}