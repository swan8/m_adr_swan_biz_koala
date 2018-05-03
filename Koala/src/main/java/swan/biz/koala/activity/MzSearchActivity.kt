package swan.biz.koala.activity

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.mz_search_master.*
import me.dkzwm.widget.srl.SmoothRefreshLayout
import swan.atom.core.base.AtomCoreBaseActivity
import swan.atom.core.base.AtomCoreBaseToolbarActivity
import swan.atom.core.extensions.obtainViewModel
import swan.atom.core.extentions.withAnyItemDecoration
import swan.atom.core.listener.AtomCoreRecyclerItemClickListener
import swan.biz.koala.KoalaApplicationImpl
import swan.biz.koala.R
import swan.biz.koala.adapter.item.MzSearchHeaderItem
import swan.biz.koala.adapter.item.MztSortedListBodyItem
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.network.IMzituApiField
import swan.biz.koala.vm.MzSearchViewModel


class MzSearchActivity : AtomCoreBaseToolbarActivity(), SmoothRefreshLayout.OnRefreshListener {

    companion object {

        fun newInstance(context: AtomCoreBaseActivity, dataCenter: MztDataCenter?, keyword: String): Unit {
            val intent: Intent = Intent(context, MzSearchActivity::class.java)
            intent.putExtra(IMzituApiField.searchPlaceHolder, dataCenter?.searchPlaceHolder)
            intent.putExtra(IMzituApiField.searchKeyword, keyword)
            context.startActivity(intent)
        }
    }

    private lateinit var fastItemAdapter: FastItemAdapter<IItem<*, *>>

    private lateinit var headerItemAdapter: ItemAdapter<IItem<*, *>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutResId = R.layout.mz_search_master

        cirrusResId = R.id.cirrus

        cirrus?.let {
            builder
                    .withCirrusTitle(R.string.mz_resStringSearch)
                    .withCirrusNavigationBackPressIcon()
                    .build(it)
        }

        immersionBar.init()

        searchInputText.hint = intent?.getStringExtra(IMzituApiField.searchPlaceHolder)

        searchRefreshContainer.let {
            it.setOnRefreshListener(this)
            it.setDisableLoadMore(true)
        }

        searchRecyclerContainer.let {
            headerItemAdapter = ItemAdapter.items()

            fastItemAdapter = FastItemAdapter<IItem<*, *>>()
            fastItemAdapter.let { adapter ->
                adapter.withSelectable(true).setHasStableIds(true)
                adapter.addAdapter(0, headerItemAdapter)
                headerItemAdapter.add(MzSearchHeaderItem(1))
                it.adapter = adapter
            }

            val layoutManager: GridLayoutManager = GridLayoutManager(this, 2)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (position) {
                        0 -> layoutManager.spanCount
                        else -> 1
                    }
                }
            }

            it.layoutManager = layoutManager
            it.setHasFixedSize(true)
            it.addItemDecoration(it.withAnyItemDecoration(KoalaApplicationImpl.getDimens(R.dimen.atom_core_resDimensGapSmall), 1))

            obtainViewModel(MzSearchViewModel::class.java).let { viewModel ->
                viewModel.dataCenter.observe(this@MzSearchActivity, Observer {
                    val items: MutableList<IItem<*, *>> = mutableListOf()
                    it?.postList?.map {
                        items.add(MztSortedListBodyItem(it))
                    }

                    viewModel.clearAdapterItemDataWhenFirstPage(fastItemAdapter)
                    fastItemAdapter.add(items)

                    searchRefreshContainer.setDisableLoadMore(!it?.pageNavigationHasNext!!)
                    searchRefreshContainer.refreshComplete()
                })

                viewModel.category.value = "你"
                viewModel.postRequestDataCenter(true)
            }


            it.addOnItemTouchListener(AtomCoreRecyclerItemClickListener(this@MzSearchActivity, object : AtomCoreRecyclerItemClickListener.SimpleOnItemClickListener() {

                override fun onItemClick(childView: View?, position: Int) {
                    fastItemAdapter.getAdapterItem(position)?.let {
//                        it.src?.unitId?.run {
//                            MztPostActivity.newInstance(this@MzSearchActivity, this)
//                        }
                    }
                }
            }))
        }
    }

    override fun onRefreshBegin(isRefresh: Boolean) {
        obtainViewModel(MzSearchViewModel::class.java).postRequestDataCenter(isRefresh)
    }

    override fun onRefreshComplete(isSuccessful: Boolean) {

    }
}