package com.example.m2_mobile.ui

import Utils.readMenuFromJson
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.m2_mobile.data.MenuItem
import com.example.m2_mobile.databinding.ActivityMainBinding
import android.content.res.AssetManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //val assetManager: AssetManager = assets

        val menuItems = readMenuFromJson(this)
        menuAdapter = MenuAdapter(menuItems)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = menuAdapter
        }
    }
}