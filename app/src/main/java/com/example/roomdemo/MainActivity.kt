package com.example.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdemo.adapter.MyRecyclerViewAdapter
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.db.Subscriber
import com.example.roomdemo.db.SubscriberDAO
import com.example.roomdemo.db.SubscriberDatabase
import com.example.roomdemo.repository.SubscriberRepository
import com.example.roomdemo.viewmodel.SubscriberViewModel
import com.example.roomdemo.viewmodel.SubscriberViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao= SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel= ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)
        binding.myViewModel = subscriberViewModel
        binding.lifecycleOwner= this
        initRecyclerView()

        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    //this is a function that observes the list of subscribers
    private fun displaySubscribersList(){
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.i("LIST OF SUBS", it.toString())
            //bind your custom adapter to recycler view and pass the subscribers as parameter
            binding.subscriberRecyclerView.adapter= MyRecyclerViewAdapter(it,{
                selectedItem-> listItemChecked((selectedItem))
            });

        })
    }

    //this displays list of subscribers to the recycler view
    private fun initRecyclerView(){
        binding.subscriberRecyclerView.layoutManager= LinearLayoutManager(this)
        displaySubscribersList()
    }

    //this serves as onClick Action Listener
    private fun listItemChecked(subscriber: Subscriber){
//        Toast.makeText(this, subscriber.name, Toast.LENGTH_LONG).show();
        subscriberViewModel.initUpdateAndDelete(subscriber)
    }


}