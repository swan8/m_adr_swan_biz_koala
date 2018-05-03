package swan.biz.koala.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fivehundredpx.greedolayout.GreedoLayoutManager
import com.fivehundredpx.greedolayout.GreedoLayoutSizeCalculator
import com.fivehundredpx.greedolayout.GreedoSpacingItemDecoration
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.mzt_master_sorted.*
import me.dkzwm.widget.srl.SmoothRefreshLayout
import swan.atom.core.base.AtomCoreBaseActivity
import swan.atom.core.base.AtomCoreBaseFragment
import swan.atom.core.extensions.obtainViewModel
import swan.atom.core.listener.AtomCoreRecyclerItemClickListener
import swan.biz.koala.KoalaApplicationImpl
import swan.biz.koala.R
import swan.biz.koala.activity.MztPostActivity
import swan.biz.koala.adapter.item.MztSortedListBodyItem
import swan.biz.koala.network.IMzApiRequestService
import swan.biz.koala.vm.MztMasterSortedViewModel
import java.util.*

/**
 * Created by stephen on 13/03/2018.
 */
class MztMasterSortedFragment : AtomCoreBaseFragment(), SmoothRefreshLayout.OnRefreshListener, GreedoLayoutSizeCalculator.SizeCalculatorDelegate {

    private var fastItemAdapter: FastItemAdapter<MztSortedListBodyItem>? = null

    private var ratio: MutableList<Double> = mutableListOf<Double>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mzt_master_sorted, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        masterSortedRefreshContainer.let {
            it.setOnRefreshListener(this)
            it.setDisableLoadMore(true)
        }

        masterSortedRecyclerContainer.let {
            val layoutManager = GreedoLayoutManager(this)
            layoutManager.setMaxRowHeight(KoalaApplicationImpl.getDimens(R.dimen.mzt_resDimensGreedoDefaultRowHeight))

            it.layoutManager = layoutManager
            it.addItemDecoration(GreedoSpacingItemDecoration(KoalaApplicationImpl.getDimens(R.dimen.mzt_resDimensGreedoDefaultSpacing)))
            it.setHasFixedSize(true)

            fastItemAdapter = FastItemAdapter<MztSortedListBodyItem>()
            it.adapter = fastItemAdapter

            it.addOnItemTouchListener(AtomCoreRecyclerItemClickListener(context!!, object : AtomCoreRecyclerItemClickListener.SimpleOnItemClickListener() {

                override fun onItemClick(childView: View?, position: Int) {
                    fastItemAdapter?.getAdapterItem(position)?.let {
                        it.src?.unitId?.run {
                            MztPostActivity.newInstance(activity as AtomCoreBaseActivity, this)
                        }
                    }
                }
            }))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val masterSortedViewModel: MztMasterSortedViewModel? = obtainViewModel(MztMasterSortedViewModel::class.java)
        masterSortedViewModel?.category?.observe(this, android.arch.lifecycle.Observer {
            onRefreshBegin(true)
        })

        masterSortedViewModel?.dataCenter?.observe(this, android.arch.lifecycle.Observer {
            val items: MutableList<MztSortedListBodyItem> = mutableListOf()
            it?.postList?.map {
                items.add(MztSortedListBodyItem(it))
            }

            masterSortedViewModel.clearAdapterItemDataWhenFirstPage(fastItemAdapter)
            fastItemAdapter?.add(items)

            masterSortedRefreshContainer.setDisableLoadMore(!it?.pageNavigationHasNext!!)
            masterSortedRefreshContainer.refreshComplete()
        })

        masterSortedViewModel?.resetMasterSortedCategory(IMzApiRequestService.CATEGORY.INDEX)
    }

    override fun onRefreshBegin(isRefresh: Boolean) {
        activity?.let {
            val masterSortedViewModel: MztMasterSortedViewModel? = obtainViewModel(MztMasterSortedViewModel::class.java)
            masterSortedViewModel?.postRequestDataCenter(isRefresh)
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