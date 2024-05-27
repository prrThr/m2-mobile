package com.example.m2_mobile.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.m2_mobile.data.MenuItem
import com.example.m2_mobile.databinding.ItemMenuBinding

class MenuAdapter(private val menuItems: List<MenuItem>, private val isOffline: Boolean) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuItem = menuItems[position]
        holder.bind(menuItem, isOffline)
    }

    override fun getItemCount(): Int = menuItems.size

    inner class MenuViewHolder(private val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: MenuItem, isOffline: Boolean) {
            binding.itemName.text = menuItem.name
            binding.itemPrice.text = if (isOffline) "a consultar" else menuItem.price.toString()
            menuItem.image?.let { binding.itemImage.setImageBitmap(it) }
        }
    }
}
