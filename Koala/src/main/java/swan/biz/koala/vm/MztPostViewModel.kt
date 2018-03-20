package swan.biz.koala.vm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import swan.atom.core.base.AtomCoreBaseSchedulerTransformer
import swan.biz.koala.model.MztPostDataCenter
import swan.biz.koala.network.MzituRequestDelegate

/**
 * Created by stephen on 18-3-16.
 */
class MztPostViewModel : ViewModel() {

    var pageNo: Int = 1
        private set

    var dataCenter: MutableLiveData<MztPostDataCenter> = MutableLiveData<MztPostDataCenter>()
        private set

    fun loadDataCenter(postId: String, isRefresh: Boolean) {
        when (isRefresh) {
            true -> pageNo = 1
            false -> ++ pageNo
        }

        MzituRequestDelegate.Mzitu()?.postRequestMztPostData(postId, 1)!!
                .compose(AtomCoreBaseSchedulerTransformer())
                .subscribe({
                    val dataCenter = it
                    it.pageNavigationLastNumber?.toIntOrNull()?.let {
                        dataCenter.pageNavigationHasNext = it > pageNo
                    }

                    this.dataCenter.value = dataCenter
                }, {
                    it.printStackTrace()
                })
    }
}