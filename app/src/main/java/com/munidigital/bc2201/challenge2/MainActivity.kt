package com.munidigital.bc2201.challenge2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.munidigital.bc2201.R
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val auth: FirebaseAuth = Firebase.auth
        auth.signOut()
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
        return true
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