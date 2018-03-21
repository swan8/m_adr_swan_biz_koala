package swan.biz.koala.vm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import io.reactivex.Observable
import org.jsoup.Jsoup
import swan.atom.core.base.AtomCoreBaseSchedulerTransformer
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.network.IMzituRequestService
import swan.biz.koala.network.IMztNodeField
import swan.biz.koala.network.MzituRequestDelegate

/**
 * Created by stephen on 18-3-16.
 */
class MztMasterMixSelfieViewModel : ViewModel() {

    var pageNo: Int = 0
        private set

    var dataCenter: MutableLiveData<MztDataCenter> = MutableLiveData<MztDataCenter>()
        private set

    var category: MutableLiveData<String> = MutableLiveData<String>()
        private set

    fun setCategoryValue(vaule: String) {
        category.value = vaule
    }

    fun clearAdapterItemDataWhenFirstPage(adapter: FastItemAdapter<*>?): Unit {
        when (pageNo) {
            0 -> adapter?.clear()
        }
    }

    fun loadDataCenter(isRefresh: Boolean) {
        when (isRefresh) {
            true -> pageNo = 0
            false -> Math.max(-- pageNo, 0)
        }

        postRequest(
                category.value ?: IMzituRequestService.CATEGORY.SELFIE,
                pageNo
        ).compose(AtomCoreBaseSchedulerTransformer())
                .subscribe({
                    val dataCenter: MztDataCenter = MztDataCenter()
                    it.let {
                        Jsoup.parse(it)
                    }?.let {
                                dataCenter.searchPlaceHolder = it.selectFirst(IMztNodeField.SEARCH_INPUT).attr(IMztNodeField.NODE_PLACEHOLDER)
                                dataCenter.pageNavigationWithDocument(it)
                                dataCenter.topWithElements(it.select(IMztNodeField.WIDGET_TOP))
                                dataCenter.guessWithElements(it.select(IMztNodeField.WIDGET_LIKE_GUESS))
                                dataCenter.loveWithElements(it.select(IMztNodeField.WIDGET_LIKE_LOVE))
                                dataCenter.postListSelfieWithElements(it.select(IMztNodeField.POST_LIST_SELFIE))

                                if (pageNo == 0) {
                                    pageNo = dataCenter.pageNavigationCurrentNumber?.toInt() ?: 0
                                }
                            }

                    this.dataCenter.value = dataCenter
                }, {
                    it.printStackTrace()
                })
    }

    private fun postRequest(category: String, page: Int): Observable<String> {
        return when (pageNo) {
            0 ->
                MzituRequestDelegate.requestService().postRequestMztPagePath(category, page)
            else ->
                MzituRequestDelegate.requestService().postRequestMztCommentPagePath(category, page)
        }
    }
}