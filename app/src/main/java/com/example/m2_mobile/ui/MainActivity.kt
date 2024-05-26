package com.example.m2_mobile.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.m2_mobile.data.MenuItem
import com.example.m2_mobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menuItems = listOf(
            MenuItem("Item 1", 10.0, "https://via.placeholder.com/150"),
            MenuItem("Item 2", 12.5, "https://via.placeholder.com/150"),
            MenuItem("Item 3", 8.0, "https://via.placeholder.com/150"),
            MenuItem("Item 4", 15.0, "https://via.placeholder.com/150")
        )

        menuAdapter = MenuAdapter(menuItems)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = menuAdapter
        }
    }
}