package cesar1287.com.github.desafiowecast.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cesar1287.com.github.desafiowecast.R
import cesar1287.com.github.desafiowecast.utils.KEY_EXTRA_RSS_ITEM
import me.toptas.rssconverter.RssItem

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val rssItem = intent.getSerializableExtra(KEY_EXTRA_RSS_ITEM) as RssItem

        Toast.makeText(this, rssItem.title, Toast.LENGTH_SHORT).show()
    }
}
