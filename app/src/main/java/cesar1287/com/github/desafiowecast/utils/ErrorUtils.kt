package cesar1287.com.github.desafiowecast.utils

import cesar1287.com.github.desafiowecast.api.APIError
import cesar1287.com.github.desafiowecast.api.ApiService
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

object ErrorUtils {

    fun parseError(response: Response<*>): APIError? {
        val converter: Converter<ResponseBody, APIError> = ApiService.getRssFeedApiClient()
            .responseBodyConverter(APIError::class.java, arrayOfNulls<Annotation>(0))

        var error: APIError? = null

        try {
            response.errorBody()?.let { errorBody ->
                error = converter.convert(errorBody)
            }
        } catch (e: IOException) {
            return APIError()
        }

        return error
    }
}