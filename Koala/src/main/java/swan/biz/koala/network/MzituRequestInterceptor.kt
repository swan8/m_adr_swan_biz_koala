package swan.biz.koala.network

import android.os.Build
import android.webkit.WebSettings
import okhttp3.*
import swan.biz.koala.KoalaApplicationImpl
import java.util.concurrent.TimeUnit

/**
 * Created by stephen on 18-2-11.
 */
object MzituRequestInterceptor : okhttp3.Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val builder: Request.Builder = request.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .cacheControl(
                        CacheControl.Builder()
                                .onlyIfCached()
                                .maxStale(43200 + 600, TimeUnit.SECONDS)
                                .build()
                )
                .removeHeader("User-Agent")
                .header("User-Agent", defaultUserAgent())

        return when (request.body()) {
            is FormBody ->
                interceptRequestFormBody(builder, chain)
            else ->
                interceptRequestBody(builder, chain)
        }
    }

    private fun interceptRequestFormBody(builder: Request.Builder, chain: Interceptor.Chain): Response {
        val newRequestBuilder: FormBody.Builder = FormBody.Builder()

        val body: FormBody = chain.request().body() as FormBody
        if (body.size() > 0) {
            for (i in 0 until body.size()) {
                newRequestBuilder.addEncoded(body.encodedName(i), body.encodedValue(i))
            }
        }

        val newRequest: Request = builder.post(newRequestBuilder.build()).build()

        val response: Response = chain.proceed(newRequest)
        val responseString: String = response.body()!!.string()

        return response.newBuilder()
                .body(ResponseBody.create(MediaType.parse("application/x-www-form-urlencoded;"), responseString))
                .build()
    }

    private fun interceptRequestBody(builder: Request.Builder, chain: Interceptor.Chain): Response {
        val request: Request = builder.build()
        val response: Response = chain.proceed(request)
        return when (response.code()) {
            200 -> response
            301, 302 ->
                request.header("Location")?.let {
                    chain.proceed(
                            request.newBuilder().url(it).cacheControl(CacheControl.FORCE_NETWORK).build()
                    )
                } ?: response
            304, 504 ->
                chain.proceed(
                        request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build()
                )
            else -> response
        }
    }

    private fun defaultUserAgent(): String {
        return when (Build.VERSION.SDK_INT) {
            in Build.VERSION_CODES.JELLY_BEAN_MR1 .. Int.MAX_VALUE -> {
                try {
                    WebSettings.getDefaultUserAgent(KoalaApplicationImpl.getContext())
                } catch (e: Throwable) {
                    System.getProperty("http.agent")
                }
            }
            else -> {
                System.getProperty("http.agent")
            }
        }?.run {
            replace("[\\u4E00-\\u9FA5]".toRegex(), "")
        } ?: ""
    }
}