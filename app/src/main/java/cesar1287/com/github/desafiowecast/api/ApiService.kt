package cesar1287.com.github.desafiowecast.api

import cesar1287.com.github.desafiowecast.utils.BASE_URL_PODCAST_MRG
import me.toptas.rssconverter.RssConverterFactory
import retrofit2.Retrofit

class ApiService {

    companion object {
        fun getRssFeedApiClient() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL_PODCAST_MRG)
                .addConverterFactory(RssConverterFactory.create())
                .build()
        }
    }
}