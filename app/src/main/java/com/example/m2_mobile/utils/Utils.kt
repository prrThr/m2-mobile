import android.content.Context
import android.util.Log
import com.example.m2_mobile.data.MenuItem
import org.json.JSONObject
import java.io.InputStream

object Utils {

    fun readMenuFromJson(context: Context): List<MenuItem> {
        val menuItems = mutableListOf<MenuItem>()

        // Ler o conteúdo do arquivo JSON
        val path = "file://${context.filesDir}/menu.json"
        Log.d("File Path", path)
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
            menuItems.add(MenuItem(name, price, imageUrl))
        }

        return menuItems
    }
}
