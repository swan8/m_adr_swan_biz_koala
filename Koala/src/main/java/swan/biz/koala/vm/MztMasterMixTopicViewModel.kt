package swan.biz.koala.vm

import io.reactivex.Observable
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.network.IMzituRequestService
import swan.biz.koala.network.MzituRequestDelegate

/**
 * Created by stephen on 18-3-16.
 */
class MztMasterMixTopicViewModel : MztMasterViewModel<MztDataCenter>(IMzituRequestService.CATEGORY.TOPIC) {

    override val initializerPageNo: Int = 0

    override var pageNo: Int = initializerPageNo

    override fun postRequestSetPageNoValue(isRefresh: Boolean) {

    }

    override fun postRequestGetService(category: String, pageNo: Int): Observable<MztDataCenter> {
        return MzituRequestDelegate.requestService().postRequestMztPagePathData(category, pageNo)
    }

    override fun postRequestOnSuccess(dataCenter: MztDataCenter) {
        this.dataCenter.value = dataCenter
    }

    override fun postRequestOnError(throwable: Throwable) {
        throwable.printStackTrace()
    }
}