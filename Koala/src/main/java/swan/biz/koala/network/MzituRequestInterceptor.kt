package swan.biz.koala.network

import okhttp3.*

/**
 * Created by stephen on 18-2-11.
 */
class MzituRequestInterceptor : okhttp3.Interceptor {

    override fun intercept(chain: Interceptor.Chain?): Response {
        var builder: Request.Builder = chain!!.request().newBuilder()
//                .removeHeader("Pragma")
//                .removeHeader("Cache-Control")
//                .header("Cache-Control", "max-age=${3600 * 24 * 1}")
//                .cacheControl(CacheControl.Builder().maxAge(60, TimeUnit.MINUTES).build())

        when (chain.request().body() is FormBody) {
            true -> return interceptRequestFormBody(builder, chain)
            else -> return interceptRequestBody(builder, chain)
        }
    }

    private fun interceptRequestFormBody(builder: Request.Builder, chain: Interceptor.Chain): Response {
        var newRequestBuilder: FormBody.Builder = FormBody.Builder()
//                .addEncoded(IReadHubApiField.key, "3178a0cf34f5b16fef5cbfc7f588ad68")
//                .addEncoded(IReadHubApiField.dtype, "json")

        var body: FormBody = chain.request().body() as FormBody
        if (body.size() > 0) {
            for (i in 0 until body.size()) {
                newRequestBuilder.addEncoded(body.encodedName(i), body.encodedValue(i))
            }
        }

        var newRequest: Request = builder.post(newRequestBuilder.build()).build()

        var response: Response = chain.proceed(newRequest)
        var responseString: String = response.body()!!.string()

        return response.newBuilder()
                .body(ResponseBody.create(MediaType.parse("application/x-www-form-urlencoded;"), responseString))
                .build()
    }

    private fun interceptRequestBody(builder: Request.Builder, chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}