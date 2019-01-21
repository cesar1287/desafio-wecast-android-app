package cesar1287.com.github.desafiowecast.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cesar1287.com.github.desafiowecast.R
import cesar1287.com.github.desafiowecast.utils.GlideApp
import cesar1287.com.github.desafiowecast.utils.KEY_EXTRA_AUDIO_URL
import cesar1287.com.github.desafiowecast.utils.KEY_EXTRA_TITLE_URL
import cesar1287.com.github.desafiowecast.view.URLMediaPlayerActivity
import kotlinx.android.synthetic.main.item_rss_horizontal.view.*
import me.toptas.rssconverter.RssItem

class RssFeedHorizontalAdapter(private var context: Context, private var list: List<RssItem>) : RecyclerView.Adapter<RssFeedHorizontalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rss_horizontal, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, list[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(context: Context, rssItem: RssItem) = with(itemView) {
            itemView.title.text = rssItem.title
            itemView.description.text = rssItem.description

            itemView.rssItemLayout.setOnClickListener {
                val intent = Intent(context, URLMediaPlayerActivity::class.java)
                intent.putExtra(KEY_EXTRA_AUDIO_URL, rssItem.enclosures?.first()?.link)
                intent.putExtra(KEY_EXTRA_TITLE_URL, rssItem.title)
                context.startActivity(intent)
            }

            GlideApp
                .with(context)
                .load(ContextCompat.getDrawable(context, R.drawable.mrg_itunes))
                .into(itemView.thumbnail)
        }
    }
}