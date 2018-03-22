package swan.biz.koala.vm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import swan.atom.core.base.AtomCoreBaseSchedulerTransformer
import swan.biz.koala.model.IMztDataCenter
import swan.biz.koala.model.MztDataCenter

/**
 * Created by stephen on 18-3-22.
 */
open class MztMasterViewModel<DataCenter : IMztDataCenter> constructor(
        category: String
): ViewModel() {

    var pageNo: Int = 0
        private set

    var dataCenter: MutableLiveData<MztDataCenter> = MutableLiveData<MztDataCenter>()
        private set

    var category: String
        private set

    init {
        this.category = category
    }

    open fun postRequestDataCenter(isRefresh: Boolean) {
        pageNoValueWhenPostRequest(isRefresh)

    }

    open fun pageNoValueWhenPostRequest(isRefresh: Boolean) {
        when (isRefresh) {
            true -> pageNo = 0
            false -> Math.max(-- pageNo, 0)
        }

        requestServiceWhenPostRequest(category, pageNo)
                .compose(AtomCoreBaseSchedulerTransformer())
                .subscribe({

                }, {

                })
    }

    open fun requestServiceWhenPostRequest(category: String, pageNo: Int) : Observable<DataCenter> {
        return Observable.create {  }
    }
}