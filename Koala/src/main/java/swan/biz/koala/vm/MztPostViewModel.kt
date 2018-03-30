package swan.biz.koala.vm

import io.reactivex.Observable
import swan.biz.koala.model.MztPostDataCenter
import swan.biz.koala.network.MzituRequestDelegate

/**
 * Created by stephen on 18-3-16.
 */
class MztPostViewModel : MztMasterViewModel<MztPostDataCenter>("") {

    override val initializerPageNo: Int = 1

    override var pageNo: Int = initializerPageNo
        public set(value) {
            field = value
        }

    override fun postRequestSetPageNoValue(isRefresh: Boolean) {

    }

    override fun postRequestGetService(category: String, pageNo: Int): Observable<MztPostDataCenter> {
        return MzituRequestDelegate.requestService().postRequestMztPostData(category, pageNo)
    }

    override fun postRequestOnSuccess(dataCenter: MztPostDataCenter) {
        dataCenter.pageNavigationLastNumber?.toIntOrNull()?.let {
            dataCenter.pageNavigationHasNext = it > pageNo
        }

        this.dataCenter.value = dataCenter
    }

    override fun postRequestOnError(throwable: Throwable) {
        throwable.printStackTrace()
    }
}