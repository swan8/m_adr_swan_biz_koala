package swan.biz.koala.vm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import io.reactivex.Observable
import swan.atom.core.base.AtomCoreBaseSchedulerTransformer
import swan.biz.koala.model.IMztDataCenter

/**
 * Created by stephen on 18-3-22.
 */
abstract class MztMasterViewModel<DataCenter : IMztDataCenter> constructor(
        category: String
): ViewModel() {

    abstract val initializerPageNo: Int

    abstract var pageNo: Int
        protected set

    var dataCenter: MutableLiveData<DataCenter> = MutableLiveData<DataCenter>()
        protected set

    var category: MutableLiveData<String> = MutableLiveData<String>()
        protected set

    init {
        this.category.value = category
    }

    open fun postRequestDataCenter(isRefresh: Boolean) {
        postRequestSetPageNoValue(isRefresh)

        postRequestGetService(category.value ?: "", pageNo)
                .compose(AtomCoreBaseSchedulerTransformer())
                .subscribe({
                    postRequestOnSuccess(dataCenter = it)
                }, {
                    postRequestOnError(throwable = it)
                })
    }

    protected abstract fun postRequestSetPageNoValue(isRefresh: Boolean)

    protected abstract fun postRequestGetService(category: String, pageNo: Int) : Observable<DataCenter>

    protected abstract fun postRequestOnSuccess(dataCenter: DataCenter)

    protected abstract fun postRequestOnError(throwable: Throwable)

    protected fun postRequestMinusPageNoValue(isRefresh: Boolean) {
        when (isRefresh) {
            true -> pageNo = initializerPageNo
            false -> Math.max(-- pageNo, initializerPageNo)
        }
    }

    protected fun postRequestPlusPageNoValue(isRefresh: Boolean) {
        when (isRefresh) {
            true -> pageNo = initializerPageNo
            false -> ++ pageNo
        }
    }

    fun resetMasterSortedCategory(categoryValue: String) {
        category.value = categoryValue
    }

    fun clearAdapterItemDataWhenFirstPage(adapter: FastItemAdapter<*>?): Unit {
        when (pageNo) {
            initializerPageNo -> adapter?.clear()
        }
    }
}