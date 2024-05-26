package com.example.m2_mobile.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.m2_mobile.data.MenuItem

class MenuDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "menu.db"
        private const val TABLE_MENU = "menu"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_IMAGE_PATH = "image_path"
    }

// ------------------------------------------------------------------------------ //

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_MENU (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_PRICE REAL, " +
                "$COLUMN_IMAGE_PATH TEXT)"
        db.execSQL(createTableQuery)
    }

// ------------------------------------------------------------------------------ //

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Você pode implementar a lógica de atualização do banco de dados aqui se necessário
    }

// ------------------------------------------------------------------------------ //

    fun insertMenu(menuItem: MenuItem) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, menuItem.name)
            put(COLUMN_PRICE, menuItem.price)
        }
        db.insert(TABLE_MENU, null, values)
        db.close()
    }

    // ------------------------------------------------------------------------------ //

    fun getAllMenuItems(): List<MenuItem> {
        val menuItems = mutableListOf<MenuItem>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_MENU, null, null, null, null, null, null
        )
        cursor.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME))
                val price = it.getDouble(it.getColumnIndexOrThrow(COLUMN_PRICE))
                val menuItem = MenuItem(name, price, "")
                menuItems.add(menuItem)
            }
        }
        db.close()
        return menuItems
    }

// ------------------------------------------------------------------------------ //
    fun deleteAllData() {
        val db = writableDatabase
        db.delete("menu", null, null)
        db.close()
    }
}
