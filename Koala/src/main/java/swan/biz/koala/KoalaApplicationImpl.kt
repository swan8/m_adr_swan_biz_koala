package swan.biz.koala

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.github.ajalt.timberkt.Timber
import okhttp3.OkHttpClient
import swan.atom.core.basectx.SwanAtomBaseApplication
import java.lang.ref.WeakReference

/**
 * Created by stephen on 09/03/2018.
 */
class KoalaApplicationImpl: SwanAtomBaseApplication.SwanAtomApplicationImpl {

    companion object {

        lateinit var instance: KoalaApplicationImpl
            private set

        fun getContext(): Context? {
            return instance.getContext()
        }

        fun getResource(): Resources? {
            return getContext()?.resources
        }

        fun getDimensionPixelOffset(id: Int): Int {
            return getResource()?.getDimensionPixelOffset(id) ?: 0
        }
    }

    private var context: WeakReference<Context>? = null

    override fun onCreate(application: Application) {
        instance = this
        instance.context = WeakReference(application.applicationContext)

        Timber.plant(Timber.DebugTree())

        Fresco.initialize(
                application.applicationContext,
                OkHttpImagePipelineConfigFactory
                        .newBuilder(
                                application.applicationContext,
                                OkHttpClient.Builder()
                                        .addInterceptor({
                                            it.proceed(it.request().newBuilder().addHeader("Referer", "http://www.mzitu.com/").build())
                                        })
                                        .build()
                        ).build()
        )
    }

    override fun getContext(): Context? {
        context?.get()?.let {
            return it
        }

        return null
    }
}