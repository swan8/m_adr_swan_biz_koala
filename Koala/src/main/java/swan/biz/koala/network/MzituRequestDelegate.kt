package swan.biz.koala.network

import com.github.ajalt.timberkt.Timber
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import swan.atom.core.AtomCoreApplicationImpl
import swan.biz.koala.network.convert.MztJsoupConverterFactory
import java.io.File

/**
 * Created by stephen on 18-3-9.
 */
object MzituRequestDelegate {

    private val requestService: IMzituRequestService

    init {

        var okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .cache(Cache(File(AtomCoreApplicationImpl.getContext()?.obbDir, "KoalaHttpCache"), 10 * 1024 * 1024))
                .addInterceptor(MzituRequestInterceptor)
                .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                    Timber.e { it }
                }).setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .build()

        var retrofit: Retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(IMzituRequestService.BASE_URL)
                .addConverterFactory(MztJsoupConverterFactory)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        requestService = retrofit.create(IMzituRequestService::class.java)
    }

    fun requestService(): IMzituRequestService {
        return requestService
    }
}