package com.example.roomdemo.repository

import com.example.roomdemo.db.Subscriber
import com.example.roomdemo.db.SubscriberDAO

class SubscriberRepository(private val dao:SubscriberDAO) {

    val subscribers = dao.getAllSubscribers()

    suspend fun insert(subscriber: Subscriber):Long{
       return dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber):Int{
       return dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber){
        dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll(){
        dao.deleteAll()
    }

}