package swan.biz.koala.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fivehundredpx.greedolayout.GreedoLayoutManager
import com.fivehundredpx.greedolayout.GreedoLayoutSizeCalculator
import com.fivehundredpx.greedolayout.GreedoSpacingItemDecoration
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.mzt_master_mix_topic.*
import me.dkzwm.widget.srl.SmoothRefreshLayout
import swam.atom.core.extensions.obtainViewModel
import swan.atom.core.base.AtomCoreBaseFragment
import swan.biz.koala.KoalaApplicationImpl
import swan.biz.koala.R
import swan.biz.koala.adapter.item.MztSortedListBodyItem
import swan.biz.koala.vm.MztMasterMixTopicViewModel

/**
 * Created by stephen on 13/03/2018.
 */
class MztMasterMixTopicFragment : AtomCoreBaseFragment(), SmoothRefreshLayout.OnRefreshListener, GreedoLayoutSizeCalculator.SizeCalculatorDelegate {

    var fastItemAdapter: FastItemAdapter<MztSortedListBodyItem>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.mzt_master_mix_topic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mixTopicRefreshContainer.let {
            it.setOnRefreshListener(this)
            it.setDisableLoadMore(true)
        }

        mixTopicRecyclerContainer.let {
            val layoutManager = GreedoLayoutManager(this)
            layoutManager.setMaxRowHeight(KoalaApplicationImpl.getDimensionPixelOffset(R.dimen.mzt_resDimensGreedoDefaultRowHeightSquare))

            it.layoutManager = layoutManager
            it.addItemDecoration(GreedoSpacingItemDecoration(KoalaApplicationImpl.getDimensionPixelOffset(R.dimen.mzt_resDimensGreedoDefaultSpacing)))
            it.setHasFixedSize(true)

            fastItemAdapter = FastItemAdapter<MztSortedListBodyItem>()
            it.adapter = fastItemAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val masterGalaxyViewModel: MztMasterMixTopicViewModel? = obtainViewModel(MztMasterMixTopicViewModel::class.java)
        masterGalaxyViewModel?.dataCenter?.observe(this, android.arch.lifecycle.Observer {
            val items: MutableList<MztSortedListBodyItem> = mutableListOf()
            it?.postList?.map {
                items.add(MztSortedListBodyItem(it))
            }

            fastItemAdapter?.add(items)

            mixTopicRefreshContainer.setDisableLoadMore(! it?.pageNavigationHasNext!!)
            mixTopicRefreshContainer.refreshComplete()
        })
    }

    override fun fragmentOnFirstVisibleToUser() {
        val masterGalaxyViewModel: MztMasterMixTopicViewModel? = obtainViewModel(MztMasterMixTopicViewModel::class.java)
        masterGalaxyViewModel?.loadDataCenter(true)
    }

    override fun onRefreshBegin(isRefresh: Boolean) {
        activity?.let {
            val masterGalaxyViewModel: MztMasterMixTopicViewModel? = obtainViewModel(MztMasterMixTopicViewModel::class.java)
            masterGalaxyViewModel?.loadDataCenter(isRefresh)
        }
    }

    override fun onRefreshComplete(isSuccessful: Boolean) {

    }

    override fun aspectRatioForIndex(index: Int): Double {
        return 1.0
    }
}