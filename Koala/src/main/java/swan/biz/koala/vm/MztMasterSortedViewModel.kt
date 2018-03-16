package swan.biz.koala.vm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.mzt_master_sorted.*
import org.jsoup.Jsoup
import swan.atom.core.base.AtomCoreBaseSchedulerTransformer
import swan.biz.koala.R
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.network.IMzituRequestService
import swan.biz.koala.network.IMztNodeField
import swan.biz.koala.network.MzituRequestDelegate

/**
 * Created by stephen on 14/03/2018.
 */
class MztMasterSortedViewModel : ViewModel() {

    var pageNo: Int = 1
    private set

    var postList: MutableLiveData<MztDataCenter> = MutableLiveData<MztDataCenter>()
    private set

    var category: MutableLiveData<String> = MutableLiveData<String>()
    private set

    fun resetMasterSortedCategory(c: String) {
        category.value = c
    }

    fun isFirstPage(adapter: FastItemAdapter<*>?): Unit {
        when (pageNo) {
            1 -> adapter?.clear()
        }
    }

    fun loadMasterDataCenter(isRefresh: Boolean) {
        when (isRefresh) {
            true -> pageNo = 1
            false -> ++ pageNo
        }

        val category: String = if (category.value == null) IMzituRequestService.CATEGORY.INDEX else category.value!!

        MzituRequestDelegate.Mzitu()?.postRequestMztPagePath(category, pageNo)!!
                .compose(AtomCoreBaseSchedulerTransformer())
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
                        dataCenter.postListWithElements(it.select(IMztNodeField.POST_LIST))
                    }

                    postList.value = dataCenter
                }, {
                    it.printStackTrace()
                })
    }
}