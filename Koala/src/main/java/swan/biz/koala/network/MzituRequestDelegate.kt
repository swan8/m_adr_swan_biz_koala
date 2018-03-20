package swan.biz.koala.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import swan.biz.koala.network.convert.MztJsoupConverterFactory

/**
 * Created by stephen on 18-3-9.
 */
object MzituRequestDelegate {

    private var requestService: IMzituRequestService? = null

    init {

        var okHttpClient: OkHttpClient = OkHttpClient.Builder()
//                .cache(Cache(File("/mnt/sdcard/", "ReadHubRequestDelegate"), 10 * 1024 * 1024))
                .addInterceptor(MzituRequestInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()

        var retrofit: Retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(IMzituRequestService.BASE_URL)
                .addConverterFactory(MztJsoupConverterFactory)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        requestService = retrofit.create(IMzituRequestService::class.java)
    }

    fun Mzitu(): IMzituRequestService? {
        return requestService
    }
}