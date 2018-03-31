package swan.biz.koala

import android.content.Context
import android.graphics.Bitmap
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import com.github.ajalt.timberkt.Timber
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import swan.atom.core.base.SwanAtomApplicationImpl
import swan.biz.koala.network.IMzituRequestService
import java.lang.ref.WeakReference

/**
 * Created by stephen on 09/03/2018.
 */
object KoalaApplicationImpl: SwanAtomApplicationImpl {

    override lateinit var reference: WeakReference<Context>

    override fun applicationInit(context: Context) {
        Fresco.initialize(context, OkHttpImagePipelineConfigFactory
                .newBuilder(context, OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                            Timber.e {
                                it
                            }
                        }).setLevel(HttpLoggingInterceptor.Level.HEADERS))
                        .addInterceptor({
                            val builder: Request.Builder = it.request().newBuilder()
                            it.proceed(builder.addHeader("Referer", IMzituRequestService.BASE_URL).build())
                        }).build())
                .setMainDiskCacheConfig(
                        DiskCacheConfig.newBuilder(context)
                                .setBaseDirectoryName("images")
                                .setBaseDirectoryPathSupplier { context.obbDir }
                                .setMaxCacheSize(50.times(1024).times(1024))
                                .setMaxCacheSizeOnLowDiskSpace(20.times(1024).times(1024))
                                .setMaxCacheSizeOnVeryLowDiskSpace(15.times(1024).times(1024))
                                .build()
                )
                .setDownsampleEnabled(true)
                .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
                .setBitmapsConfig(Bitmap.Config.RGB_565)
                .build()
        )
    }
}