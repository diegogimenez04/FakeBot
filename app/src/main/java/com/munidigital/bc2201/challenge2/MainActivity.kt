package com.munidigital.bc2201.challenge2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.munidigital.bc2201.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.msEmptyView.visibility = View.VISIBLE

        val viewModel: MainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.msListLiveData.observe(this, {
            handleEmptyView(it)
            binding.msEditText.text.clear()
            updateActivity(it)
        })

        binding.msRecycler.layoutManager = LinearLayoutManager(this)
        adapter = MsAdapter()
        binding.msRecycler.adapter = adapter

        binding.msSend.setOnClickListener {
            if (binding.msEditText.text.isEmpty()){
                Toast.makeText(this, "You must input a message first!",
                               Toast.LENGTH_SHORT).show()
            } else {
                val textMessage = binding.msEditText.text.toString()
                viewModel.sendMessage(textMessage)
            }
        }
    }

    private fun handleEmptyView(it: MutableList<Message>) {
        if (it.isEmpty())
            binding.msEmptyView.visibility = View.VISIBLE
        else
            binding.msEmptyView.visibility = View.GONE
    }

    private fun updateActivity(msList: MutableList<Message>) {
        adapter = MsAdapter()
        binding.msRecycler.adapter = adapter
        adapter.submitList(msList)
        binding.msRecycler.scrollToPosition(msList.size - 1)
    }
}