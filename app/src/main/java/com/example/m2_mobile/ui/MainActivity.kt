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

        //val menuItems = listOf(
        //    MenuItem("Hambúrguer", 10.0, "https://static.vecteezy.com/system/resources/previews/000/763/553/large_2x/hamburger-isolated-on-white-background-photo.jpg"),
        //    MenuItem("Pizza", 12.5, "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ffreepngimg.com%2Fthumb%2Fpizza%2F35-pizza-png-image.png&f=1&nofb=1&ipt=d549d8016c332a4e28239da78f3a7e7513a893f6dff9207fb4951aa33f7a7874&ipo=images"),
        //    MenuItem("Sushi", 15.0, "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fthumbs.dreamstime.com%2Fb%2Ffresh-sushi-white-background-27497610.jpg&f=1&nofb=1&ipt=38f24738149b87106a0825d624f7ec35db49b7122bb761a2f5b325c34489f929&ipo=images")
        //)

        menuAdapter = MenuAdapter(menuItems)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = menuAdapter
        }
    }
}