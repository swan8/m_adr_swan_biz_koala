package swan.biz.koala.vm

import io.reactivex.Observable
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.network.IMzApiRequestService
import swan.biz.koala.network.MzituRequestDelegate

/**
 * Created by stephen on 18-3-16.
 */
class MztMasterMixSelfieViewModel : MztMasterViewModel<MztDataCenter>(IMzApiRequestService.CATEGORY.SELFIE) {

    override val initializerPageNo: Int = 0

    override var pageNo: Int = 0

    override fun postRequestSetPageNoValue(isRefresh: Boolean) {
        postRequestMinusPageNoValue(isRefresh)
    }

    override fun postRequestGetService(category: String, pageNo: Int): Observable<MztDataCenter> {
        return when (pageNo) {
            initializerPageNo ->
                MzituRequestDelegate.requestService().postRequestMztPagePathData(category, pageNo)
            else ->
                MzituRequestDelegate.requestService().postRequestMztCommentPagePath(category, pageNo)
        }
    }

    override fun postRequestOnSuccess(dataCenter: MztDataCenter) {
        this.dataCenter.value = dataCenter
        if (pageNo == initializerPageNo) {
            pageNo = dataCenter.pageNavigationCurrentNumber?.toInt() ?: initializerPageNo
        }
    }

    override fun postRequestOnError(throwable: Throwable) {
        throwable.printStackTrace()
    }
}