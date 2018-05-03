package swan.biz.koala.vm

import io.reactivex.Observable
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.network.IMzApiRequestService
import swan.biz.koala.network.MzituRequestDelegate

class MzSearchViewModel : MztMasterViewModel<MztDataCenter>(IMzApiRequestService.CATEGORY.SEARCH) {

    override val initializerPageNo: Int = 1

    override var pageNo: Int = 1

    override fun postRequestSetPageNoValue(isRefresh: Boolean) {
        postRequestPlusPageNoValue(isRefresh)
    }

    override fun postRequestGetService(category: String, pageNo: Int): Observable<MztDataCenter> {
        return MzituRequestDelegate.requestService().postApiRequestMzSearch(category, pageNo)
    }

    override fun postRequestOnSuccess(dataCenter: MztDataCenter) {
        this.dataCenter.value = dataCenter
    }

    override fun postRequestOnError(throwable: Throwable) {
        throwable.printStackTrace()
    }
}