package swan.biz.koala.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fivehundredpx.greedolayout.GreedoLayoutManager
import com.fivehundredpx.greedolayout.GreedoLayoutSizeCalculator
import com.fivehundredpx.greedolayout.GreedoSpacingItemDecoration
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.mzt_master_mix_selfie.*
import me.dkzwm.widget.srl.SmoothRefreshLayout
import swam.atom.core.extensions.obtainViewModel
import swan.atom.core.base.AtomCoreBaseFragment
import swan.biz.koala.KoalaApplicationImpl
import swan.biz.koala.R
import swan.biz.koala.adapter.item.MztSortedListBodyItem
import swan.biz.koala.network.IMzituRequestService
import swan.biz.koala.vm.MztMasterMixSelfieViewModel
import java.util.*

/**
 * Created by stephen on 13/03/2018.
 */
class MztMasterMixSelfieFragment : AtomCoreBaseFragment(), SmoothRefreshLayout.OnRefreshListener, GreedoLayoutSizeCalculator.SizeCalculatorDelegate {

    var fastItemAdapter: FastItemAdapter<MztSortedListBodyItem>? = null

    var ratio: MutableList<Double> = mutableListOf<Double>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.mzt_master_mix_selfie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mixSelfieRefreshContainer.let {
            it.setOnRefreshListener(this)
            it.setDisableLoadMore(true)
        }

        mixSelfieRecyclerContainer.let {
            val layoutManager = GreedoLayoutManager(this)
            layoutManager.setMaxRowHeight(KoalaApplicationImpl.getDimensionPixelOffset(R.dimen.mzt_resDimensGreedoDefaultRowHeight))

            it.layoutManager = layoutManager
            it.addItemDecoration(GreedoSpacingItemDecoration(KoalaApplicationImpl.getDimensionPixelOffset(R.dimen.mzt_resDimensGreedoDefaultSpacing)))
            it.setHasFixedSize(true)

            fastItemAdapter = FastItemAdapter<MztSortedListBodyItem>()
            it.adapter = fastItemAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val mixSelfieViewModel: MztMasterMixSelfieViewModel? = obtainViewModel(MztMasterMixSelfieViewModel::class.java)
        mixSelfieViewModel?.category?.observe(this, android.arch.lifecycle.Observer {
            onRefreshBegin(true)
        })

        mixSelfieViewModel?.dataCenter?.observe(this, android.arch.lifecycle.Observer {
            val items: MutableList<MztSortedListBodyItem> = mutableListOf()
            it?.postList?.map {
                items.add(MztSortedListBodyItem(it))
            }

            mixSelfieViewModel.clearAdapterItemDataWhenFirstPage(fastItemAdapter)
            fastItemAdapter?.add(items)

            mixSelfieRefreshContainer.setDisableLoadMore(! it?.pageNavigationHasPrev!!)
            mixSelfieRefreshContainer.refreshComplete()
        })
    }

    override fun fragmentOnFirstVisibleToUser() {
        val mixSelfieViewModel: MztMasterMixSelfieViewModel? = obtainViewModel(MztMasterMixSelfieViewModel::class.java)
        mixSelfieViewModel?.setCategoryValue(IMzituRequestService.CATEGORY.SELFIE)
    }

    override fun onRefreshBegin(isRefresh: Boolean) {
        activity?.let {
            val masterGalaxyViewModel: MztMasterMixSelfieViewModel? = obtainViewModel(MztMasterMixSelfieViewModel::class.java)
            masterGalaxyViewModel?.loadDataCenter(isRefresh)
        }
    }

    override fun onRefreshComplete(isSuccessful: Boolean) {

    }

    override fun aspectRatioForIndex(index: Int): Double {
        var r: Double? = ratio.getOrNull(index)
        r = r ?: (Random().nextInt(25) + 55.0) / 100
        ratio.add(index, r)
        return r
    }
}