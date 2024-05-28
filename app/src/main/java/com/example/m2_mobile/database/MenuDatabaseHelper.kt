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
        private const val COLUMN_PATH = "path"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = buildString {
            append("CREATE TABLE $TABLE_MENU (")
            append("$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, ")
            append("$COLUMN_NAME TEXT, ")
            append("$COLUMN_PRICE REAL, ")
            append("$COLUMN_PATH TEXT )")
        } //
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // não precisa de implementação. Deixar por conta do override
    }

    fun insertMenu(menuItem: MenuItem) {
        println("CHEGOU AQUI, VAI INSERIR NO BANCO DE DADOS...")
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, menuItem.name)
            put(COLUMN_PRICE, menuItem.price)
            put(COLUMN_PATH, menuItem.path)
        }
        db.insert(TABLE_MENU, null, values)
        db.close()
    }

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
                val path = it.getString(it.getColumnIndexOrThrow(COLUMN_PATH))
                val menuItem = MenuItem(name, price, null, path)
                menuItems.add(menuItem)
            }

        }
        db.close()
        return menuItems
    }

    fun deleteAllData() {
        val db = writableDatabase
        db.delete("menu", null, null)
        db.close()
    }
}
