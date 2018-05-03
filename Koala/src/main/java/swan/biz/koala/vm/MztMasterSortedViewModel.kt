package swan.biz.koala.vm

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import swan.biz.koala.database.MzPersistentDatabaseHelper
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.network.IMzApiRequestService
import swan.biz.koala.network.MzituRequestDelegate

/**
 * Created by stephen on 14/03/2018.
 */
class MztMasterSortedViewModel : MztMasterViewModel<MztDataCenter>(IMzApiRequestService.CATEGORY.INDEX) {

    override val initializerPageNo: Int = 1

    override var pageNo: Int = 1

    override fun postRequestSetPageNoValue(isRefresh: Boolean) {
        postRequestPlusPageNoValue(isRefresh)
    }

    override fun postRequestGetService(category: String, pageNo: Int): Observable<MztDataCenter> {
        return MzituRequestDelegate.requestService().postRequestMztPagePathData(category, pageNo)
    }

    override fun postRequestOnSuccess(dataCenter: MztDataCenter) {
        this.dataCenter.value = dataCenter

        Single.fromCallable {
            MzPersistentDatabaseHelper.TOP().requestMzTopList()
        }.filter {
            it.firstOrNull()?.let {
                it.createTime + 86400L * 1000 < System.currentTimeMillis()
            } ?: true
        }.subscribeOn(Schedulers.io())
                .subscribeBy(
                        onSuccess = {
                            MzPersistentDatabaseHelper.TOP().let {
                                it.clearAll()
                                it.insert(dataCenter.top)
                            }
                        },

                        onError = {
                            it.printStackTrace()
                        }
                )
    }

    override fun postRequestOnError(throwable: Throwable) {
        throwable.printStackTrace()
    }
}