package swan.biz.koala.vm

import io.reactivex.Observable
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.network.IMzituRequestService
import swan.biz.koala.network.MzituRequestDelegate

/**
 * Created by stephen on 18-3-16.
 */
class MztMasterGalaxyViewModel : MztMasterViewModel<MztDataCenter>(IMzituRequestService.CATEGORY.MM) {

    override val initializerPageNo: Int = 1

    override var pageNo: Int = initializerPageNo

    override fun postRequestSetPageNoValue(isRefresh: Boolean) {
        postRequestPlusPageNoValue(isRefresh)
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