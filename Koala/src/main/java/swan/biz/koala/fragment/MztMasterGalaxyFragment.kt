package swan.biz.koala.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fivehundredpx.greedolayout.GreedoLayoutManager
import com.fivehundredpx.greedolayout.GreedoLayoutSizeCalculator
import com.fivehundredpx.greedolayout.GreedoSpacingItemDecoration
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.mzt_master_galaxy.*
import me.dkzwm.widget.srl.SmoothRefreshLayout
import swan.atom.core.extensions.obtainViewModel
import swan.atom.core.base.AtomCoreBaseFragment
import swan.biz.koala.KoalaApplicationImpl
import swan.biz.koala.R
import swan.biz.koala.adapter.item.MztSortedListBodyItem
import swan.biz.koala.network.IMzApiRequestService
import swan.biz.koala.vm.MztMasterGalaxyViewModel
import java.util.*

/**
 * Created by stephen on 13/03/2018.
 */
class MztMasterGalaxyFragment : AtomCoreBaseFragment(), SmoothRefreshLayout.OnRefreshListener, GreedoLayoutSizeCalculator.SizeCalculatorDelegate {

    var fastItemAdapter: FastItemAdapter<MztSortedListBodyItem>? = null

    var ratio: MutableList<Double> = mutableListOf<Double>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.mzt_master_galaxy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        masterGalaxyRefreshContainer.let {
            it.setOnRefreshListener(this)
            it.setDisableLoadMore(true)
        }

        masterGalaxyRecyclerContainer.let {
            val layoutManager = GreedoLayoutManager(this)
            layoutManager.setMaxRowHeight(KoalaApplicationImpl.getDimens(R.dimen.mzt_resDimensGreedoDefaultRowHeight))

            it.layoutManager = layoutManager
            it.addItemDecoration(GreedoSpacingItemDecoration(KoalaApplicationImpl.getDimens(R.dimen.mzt_resDimensGreedoDefaultSpacing)))
            it.setHasFixedSize(true)

            fastItemAdapter = FastItemAdapter<MztSortedListBodyItem>()
            it.adapter = fastItemAdapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val masterGalaxyViewModel: MztMasterGalaxyViewModel? = obtainViewModel(MztMasterGalaxyViewModel::class.java)
        masterGalaxyViewModel?.category?.observe(this, android.arch.lifecycle.Observer {
            onRefreshBegin(true)
        })

        masterGalaxyViewModel?.dataCenter?.observe(this, android.arch.lifecycle.Observer {
            val items: MutableList<MztSortedListBodyItem> = mutableListOf()
            it?.postList?.map {
                items.add(MztSortedListBodyItem(it))
            }

            masterGalaxyViewModel.clearAdapterItemDataWhenFirstPage(fastItemAdapter)
            fastItemAdapter?.add(items)

            masterGalaxyRefreshContainer.setDisableLoadMore(! it?.pageNavigationHasNext!!)
            masterGalaxyRefreshContainer.refreshComplete()
        })
    }

    override fun fragmentOnFirstVisibleToUser() {
        val masterGalaxyViewModel: MztMasterGalaxyViewModel? = obtainViewModel(MztMasterGalaxyViewModel::class.java)
        masterGalaxyViewModel?.resetMasterSortedCategory(IMzApiRequestService.CATEGORY.MM)
    }

    override fun onRefreshBegin(isRefresh: Boolean) {
        activity?.let {
            val masterGalaxyViewModel: MztMasterGalaxyViewModel? = obtainViewModel(MztMasterGalaxyViewModel::class.java)
            masterGalaxyViewModel?.postRequestDataCenter(isRefresh)
        }
    }

    override fun onRefreshComplete(isSuccessful: Boolean) {

    }

    override fun aspectRatioForIndex(index: Int): Double {
        var r: Double? = ratio.getOrNull(index)
        r?.let {
            return it
        }

        r = (Random().nextInt(25) + 55.0) / 100
        ratio.add(index, r)

        return r
    }
}