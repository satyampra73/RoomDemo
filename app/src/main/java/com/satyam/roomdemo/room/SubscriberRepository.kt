package com.satyam.roomdemo.room

class SubscriberRepository (private val dao : SubscriberDAO){
    val subscriber = dao.getAllSubscribers()

    suspend fun insert (subscriber: Subscriber){
        dao.insertSubscriber(subscriber)
    }

    suspend fun update (subscriber: Subscriber){
        dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber : Subscriber){
        dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll(subscriber: Subscriber){
        dao.deleteAll()
    }


}