package cesar1287.com.github.desafiowecast.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import cesar1287.com.github.desafiowecast.R
import cesar1287.com.github.desafiowecast.utils.Status
import cesar1287.com.github.desafiowecast.viewmodel.RssFeedViewModel
import cesar1287.com.github.desafiowecast.adapter.RssFeedAdapter
import kotlinx.android.synthetic.main.activity_main.*
import me.toptas.rssconverter.RssItem

class MainActivity : AppCompatActivity() {

    private var rssFeedAdapter: RssFeedAdapter? = null
    private val rssFeedList: MutableList<RssItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        loadContent()

        buttonRetry.setOnClickListener {
            loadContent()
        }
    }

    private fun loadContent() {
        val viewModel = ViewModelProviders.of(this).get(RssFeedViewModel::class.java)
        viewModel.getAllRssFeed()?.observe(this, Observer { resource ->
            when(resource?.status) {
                Status.LOADING -> {
                    setVisibility(View.VISIBLE, View.GONE, View.GONE, View.GONE)
                }
                Status.ERROR -> {
                    errorMessage.text = resource.message

                    setVisibility(View.GONE, View.GONE, View.GONE, View.VISIBLE)
                }
                Status.SUCCESS -> {
                    resource.data?.items?.let { rssFeedListNonNull ->
                        rssFeedList.addAll(rssFeedListNonNull)
                        rssFeedAdapter?.notifyDataSetChanged()

                        setVisibility(View.GONE, View.VISIBLE, View.GONE, View.GONE)
                    } ?: run {
                        setVisibility(View.GONE, View.GONE, View.VISIBLE, View.GONE)
                    }
                }
            }
        })
    }

    private fun setVisibility(progress: Int, recycler: Int, noContent: Int, error: Int) {
        progressCircular.visibility = progress
        contentLayout.visibility = recycler
        no_content.visibility = noContent
        errorLayout.visibility = error
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.layoutManager = layoutManager
        rssFeedAdapter = RssFeedAdapter(this@MainActivity, rssFeedList)
        recyclerView.adapter = rssFeedAdapter
    }
}
