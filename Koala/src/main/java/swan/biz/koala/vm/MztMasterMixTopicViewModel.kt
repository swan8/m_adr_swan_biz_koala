package swan.biz.koala.vm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import org.jsoup.Jsoup
import swan.atom.core.base.AtomCoreBaseSchedulerTransformer
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.network.IMzituRequestService
import swan.biz.koala.network.IMztNodeField
import swan.biz.koala.network.MzituRequestDelegate

/**
 * Created by stephen on 18-3-16.
 */
class MztMasterMixTopicViewModel : ViewModel() {

    var dataCenter: MutableLiveData<MztDataCenter> = MutableLiveData<MztDataCenter>()
        private set

    fun loadDataCenter(isRefresh: Boolean) {
        MzituRequestDelegate.requestService().postRequestMztPagePath(IMzituRequestService.CATEGORY.TOPIC)
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
                                dataCenter.postListTopicWithElements(it.select(IMztNodeField.POST_LIST_TOPIC))
                            }

                    this.dataCenter.value = dataCenter
                }, {
                    it.printStackTrace()
                })
    }
}