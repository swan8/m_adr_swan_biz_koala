package swan.biz.koala.vm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import org.jsoup.Jsoup
import swan.atom.core.base.AtomCoreBaseSchedulerTransformer
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.network.IMzituRequestService
import swan.biz.koala.network.IMztNodeField
import swan.biz.koala.network.MzituRequestDelegate

/**
 * Created by stephen on 18-3-16.
 */
class MztMasterGalaxyViewModel : ViewModel() {

    var pageNo: Int = 1
        private set

    var galaxyDataCenter: MutableLiveData<MztDataCenter> = MutableLiveData<MztDataCenter>()
        private set

    var galaxyCategory: MutableLiveData<String> = MutableLiveData<String>()
        private set

    fun setGalazyCategoryValue(vaule: String) {
        galaxyCategory.value = vaule
    }

    fun clearAdapterItemDataWhenFirstPage(adapter: FastItemAdapter<*>?): Unit {
        when (pageNo) {
            1 -> adapter?.clear()
        }
    }

    fun loadGalaxyDataCenter(isRefresh: Boolean) {
        when (isRefresh) {
            true -> pageNo = 1
            false -> ++ pageNo
        }

        MzituRequestDelegate.Mzitu()?.postRequestMztPagePath(
                galaxyCategory.value ?: IMzituRequestService.CATEGORY.MM,
                pageNo
        )!!.compose(AtomCoreBaseSchedulerTransformer())
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

                    this.galaxyDataCenter.value = dataCenter
                }, {
                    it.printStackTrace()
                })
    }
}