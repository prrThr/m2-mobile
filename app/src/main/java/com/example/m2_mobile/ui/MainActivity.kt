package com.example.m2_mobile.ui

import Utils.readMenuFromJson
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.m2_mobile.data.MenuItem
import com.example.m2_mobile.database.MenuDatabaseHelper
import com.example.m2_mobile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar o MenuDatabaseHelper
        val menuDatabaseHelper = MenuDatabaseHelper(this)

        // Truncate table
        //menuDatabaseHelper.deleteAllData()

        // Armazenar os itens do Json na variável
        val menuItemsFromJson = readMenuFromJson(this)

        // Inserir cada item do menu no banco de dados
        for (menuItem in menuItemsFromJson) {
            menuDatabaseHelper.insertMenu(menuItem)
        }

        // Mostrar itens armazenados no banco de dados
        val menuItems = menuDatabaseHelper.getAllMenuItems()

        // Configurar o RecyclerView com os itens do menu
        menuAdapter = MenuAdapter(menuItems)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = menuAdapter
        }
    }
}