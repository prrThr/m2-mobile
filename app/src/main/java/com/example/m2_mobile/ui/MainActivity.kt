package com.example.m2_mobile.ui

import com.example.m2_mobile.utils.Utils.readMenuFromJson
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.m2_mobile.database.MenuDatabaseHelper
import com.example.m2_mobile.databinding.ActivityMainBinding
import com.example.m2_mobile.utils.NetworkUtils
import com.example.m2_mobile.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menuDatabaseHelper = MenuDatabaseHelper(this)
        val isOffline = !NetworkUtils.isOnline(this)

        CoroutineScope(Dispatchers.Main).launch {
            if (!isOffline) {
                // Online mode: Fetch from JSON and save to DB
                menuDatabaseHelper.deleteAllData()
                val menuItemsFromJson = withContext(Dispatchers.IO) { readMenuFromJson(this@MainActivity) }
                menuItemsFromJson.forEach { menuItem -> menuDatabaseHelper.insertMenu(menuItem) }
            }

            // Fetch from database
            val menuItems = menuDatabaseHelper.getAllMenuItems()

            if (isOffline) {
                // Load images from local storage
                menuItems.forEach { menuItem ->
                    menuItem.image = Utils.loadImageFromLocalStorage(this@MainActivity, menuItem.name)
                    menuItem.price = null // Display "a consultar" for price
                }
            } else {
                // Fetch images
                val menuItemsWithImages = readMenuFromJson(this@MainActivity).associateBy { it.name }
                menuItems.forEach { menuItem -> menuItem.image = menuItemsWithImages[menuItem.name]?.image }
            }

            // Setup RecyclerView
            menuAdapter = MenuAdapter(menuItems, isOffline)
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = menuAdapter
            }
        }
    }
}
