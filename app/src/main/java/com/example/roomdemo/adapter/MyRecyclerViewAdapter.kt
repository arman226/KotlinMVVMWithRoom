package com.example.roomdemo.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.R
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.databinding.ListItemBinding
import com.example.roomdemo.db.Subscriber

class MyRecyclerViewAdapter(private val subscribers: List<Subscriber>, private val clickListener:(Subscriber)->Unit):RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding :ListItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return subscribers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(subscribers[position], clickListener)
    }

}

class ViewHolder (val binding:ListItemBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(subscriber: Subscriber, clickListener:(Subscriber)->Unit){
        binding.nameTextView.text= subscriber.name
        binding.emailTextView.text = subscriber.email
        binding.listItemLayout.setOnClickListener{
            clickListener(subscriber)
        }

    }

}