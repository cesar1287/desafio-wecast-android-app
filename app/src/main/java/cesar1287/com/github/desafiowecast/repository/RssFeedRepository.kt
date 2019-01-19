package cesar1287.com.github.desafiowecast.repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cesar1287.com.github.desafiowecast.api.ApiService
import cesar1287.com.github.desafiowecast.api.callbacks.CallbackRss
import cesar1287.com.github.desafiowecast.utils.ERROR_DEFAULT
import cesar1287.com.github.desafiowecast.utils.ErrorUtils
import cesar1287.com.github.desafiowecast.utils.Resource
import me.toptas.rssconverter.RssFeed
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RssFeedRepository {

    fun loadAllRssFeed(): LiveData<Resource<RssFeed>> {
        val mAllRss: MutableLiveData<Resource<RssFeed>> = MutableLiveData()
        mAllRss.value = Resource.loading(null)

        ApiService.getRssFeedApiClient().create(CallbackRss::class.java).getRssFeed().enqueue(object : Callback<RssFeed> {
            override fun onFailure(call: Call<RssFeed>, t: Throwable) {
                mAllRss.value = Resource.error(t.localizedMessage, null)
            }

            override fun onResponse(call: Call<RssFeed>, response: Response<RssFeed>) {
                if (response.isSuccessful) {
                    val rssList = response.body()
                    mAllRss.value = Resource.success(rssList)
                } else {
                    val error = ErrorUtils.parseError(response)

                    error?.message?.let {  message ->
                        mAllRss.value = Resource.error(message, null)
                    } ?: run {
                        mAllRss.value = Resource.error(ERROR_DEFAULT, null)
                    }
                }
            }
        })

        return mAllRss
    }
}