package cesar1287.com.github.desafiowecast.api.callbacks

import me.toptas.rssconverter.RssFeed
import retrofit2.Call
import retrofit2.http.GET

interface CallbackRss {

    @GET("podcastmrg")
    fun getRssFeed(): Call<RssFeed>
}