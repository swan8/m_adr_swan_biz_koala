package swan.atom.core.base

import io.reactivex.FlowableTransformer
import io.reactivex.Maybe
import io.reactivex.MaybeTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object AtomCoreBaseRxJava2Transformer {

    fun <T> all_main(): FlowableTransformer<T, T> {
        return FlowableTransformer<T, T> {
            upstream -> upstream.subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> io_main(): FlowableTransformer<T, T> {
        return FlowableTransformer<T, T> {
            upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> maybe_io2main() : MaybeTransformer<T, T> {
        return MaybeTransformer<T, T> {
            upstream: Maybe<T> -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
        }
    }
}