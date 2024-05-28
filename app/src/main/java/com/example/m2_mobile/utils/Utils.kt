package com.example.m2_mobile.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.m2_mobile.data.MenuItem
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.FileInputStream


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

    fun readMenuFromJson(context: Context, jsonUrl: String): List<MenuItem> {
        val menuItems = mutableListOf<MenuItem>()

        runBlocking {
            // Fazendo solicitação HTTP para obter o JSON da web
            val jsonString = withContext(Dispatchers.IO) {
                try {
                    val connection = URL(jsonUrl).openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.connect()

                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        println("Conexão JSON OK!")
                        val inputStream = connection.inputStream
                        inputStream.bufferedReader().use { it.readText() }
                    } else {
                        // Lidar com o código de resposta não OK, se necessário
                        ""
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    ""
                }
            }

            // Analisar o conteúdo JSON
            if (jsonString.isNotEmpty()) {
                val jsonObject = JSONObject(jsonString)
                val jsonArray = jsonObject.getJSONArray("menuItems")
                println("quantos itens no JSON: ${jsonArray.length()}")

                val deferredItems = (0 until jsonArray.length()).map { i ->
                    async {
                        val itemObject = jsonArray.getJSONObject(i)
                        val name = itemObject.getString("name")
                        val price = itemObject.getDouble("price")
                        val imageUrl = itemObject.getString("imageUrl")

                        val bitmap = baixarImagem(imageUrl, context.filesDir.absolutePath + "/image_files", "$name.png")
                        val path = context.filesDir.absolutePath + "/image_files/$name.png"
                        println("PATH: $path")
                        MenuItem(name, price, bitmap, path)
                    }
                }
                menuItems.addAll(deferredItems.awaitAll())
            }
        }
        return menuItems
    }
}

