import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.m2_mobile.data.MenuItem
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


object Utils {
    fun salvarImagemLocalmente(bitmap: Bitmap?, diretorio: String, nomeArquivo: String) {
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

    fun baixarImagem(url: String, diretorio: String, nomeArquivo: String): Bitmap? {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val inputStream = connection.inputStream
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Salvar a imagem localmente
            salvarImagemLocalmente(bitmap, diretorio, nomeArquivo)

            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun readMenuFromJson(context: Context): List<MenuItem> {
        val menuItems = mutableListOf<MenuItem>()

        // Ler o conteúdo do arquivo JSON
        val inputStream: InputStream = context.assets.open("menu.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        // Analisar o conteúdo JSON
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("menuItems")
        for (i in 0 until jsonArray.length()) {
            val itemObject = jsonArray.getJSONObject(i)
            val name = itemObject.getString("name")
            val price = itemObject.getDouble("price")
            val imageUrl = itemObject.getString("imageUrl")
            val bitmap = baixarImagem(imageUrl, context.filesDir.absolutePath + "/image_files", "$name.png")

            menuItems.add(MenuItem(name, price, bitmap))
        }

        return menuItems
    }
}
