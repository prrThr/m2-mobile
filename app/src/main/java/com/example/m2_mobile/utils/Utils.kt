package com.example.m2_mobile.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.m2_mobile.data.MenuItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


object Utils {
    private fun salvarImagemLocalmente(bitmap: Bitmap?, diretorio: String, nomeArquivo: String) {
        bitmap?.let {
            try {
                val diretorioImagens = File(diretorio)
                if (!diretorioImagens.exists()) {
                    diretorioImagens.mkdirs()
                }
                val arquivoImagem = File(diretorioImagens, nomeArquivo)
                val outputStream = FileOutputStream(arquivoImagem)
                it.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun baixarImagem(url: String, diretorio: String, nomeArquivo: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                salvarImagemLocalmente(bitmap, diretorio, nomeArquivo)
                println("Arquivo salvo com sucesso!")
                bitmap
            } catch (e: Exception) {
                println("Erro ao baixar a imagem: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }

    fun loadImageFromLocalStorage(context: Context, nomeArquivo: String): Bitmap? {
        return try {
            val arquivoImagem = File(context.filesDir.absolutePath + "/image_files", "$nomeArquivo.png")
            if (arquivoImagem.exists()) {
                FileInputStream(arquivoImagem).use { BitmapFactory.decodeStream(it) }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun readMenuFromJson(context: Context): List<MenuItem> {
        val menuItems = mutableListOf<MenuItem>()

        runBlocking {
            // Ler o conteúdo do arquivo JSON
            val inputStream: InputStream = context.assets.open("menu.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            // Analisar o conteúdo JSON
            val jsonObject = JSONObject(jsonString)
            val jsonArray = jsonObject.getJSONArray("menuItems")
            println("Valor do JSON: ${jsonArray.length()}")

            val deferredItems = (0 until jsonArray.length()).map { i ->
                async {
                    val itemObject = jsonArray.getJSONObject(i)
                    val name = itemObject.getString("name")
                    val price = itemObject.getDouble("price")
                    val imageUrl = itemObject.getString("imageUrl")

                    val bitmap = baixarImagem(imageUrl, context.filesDir.absolutePath + "/image_files", "$name.png")
                    MenuItem(name, price, bitmap)
                }
            }
            menuItems.addAll(deferredItems.awaitAll())
        }
        return menuItems
    }
}
