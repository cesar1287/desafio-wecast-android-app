package cesar1287.com.github.desafiowecast.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import cesar1287.com.github.desafiowecast.repository.RssFeedRepository
import cesar1287.com.github.desafiowecast.utils.Resource
import me.toptas.rssconverter.RssFeed

class RssFeedViewModel(application: Application): AndroidViewModel(application) {

    private var rssFeedRepository: RssFeedRepository? = null

    init {
        rssFeedRepository = RssFeedRepository()
    }

    fun getAllRssFeed(): LiveData<Resource<RssFeed>>? {
        return rssFeedRepository?.loadAllRssFeed()
    }
}