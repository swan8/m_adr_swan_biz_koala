package swan.biz.koala.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.mzt_master.*
import swam.atom.core.extensions.obtainViewModel
import swan.atom.core.base.AtomCoreBaseActivity
import swan.biz.koala.R
import swan.biz.koala.adapter.MztMasterTabAdapter
import swan.biz.koala.model.MztUnit
import swan.biz.koala.network.IMzituRequestService
import swan.biz.koala.vm.MztMasterGalaxyViewModel
import swan.biz.koala.vm.MztMasterSortedViewModel

/**
 * Created by stephen on 18-3-9.
 */
class MzituMasterActivity: AtomCoreBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mzt_master)

        masterFilterSelector.setOnClickListener(this@MzituMasterActivity)

        masterPagerContainer.let {
            it.adapter = MztMasterTabAdapter(applicationContext, supportFragmentManager)
            it.offscreenPageLimit = MztMasterTabAdapter.POSITION.ALL
            it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(masterTabContainer))
            it.addOnPageChangeListener(object: ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    when (position) {
                        MztMasterTabAdapter.POSITION.SORTED, MztMasterTabAdapter.POSITION.CATEGORY ->
                            masterFilterSelector.visibility = View.VISIBLE
                        else ->
                            masterFilterSelector.visibility = View.INVISIBLE
                    }
                }
            })
        }

        masterTabContainer.let {
            it.setupWithViewPager(masterPagerContainer)
            it.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(masterPagerContainer))
        }

        val masterSortedViewModel: MztMasterSortedViewModel = obtainViewModel(MztMasterSortedViewModel::class.java)
        masterSortedViewModel.postList.observe(this, android.arch.lifecycle.Observer {
            resetMasterTopLayout(it?.top)
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.masterFilterSelector -> {
                val itemsId: Int? = when (masterPagerContainer.currentItem) {
                    MztMasterTabAdapter.POSITION.SORTED ->
                        R.array.mzt_resStringMasterTabSorted
                    MztMasterTabAdapter.POSITION.CATEGORY ->
                        R.array.mzt_resStringMasterTabGalaxy
                    else -> null
                }

                itemsId?.let {
                    AlertDialog.Builder(this).setItems(it, { _, which ->
                        when (masterPagerContainer.currentItem) {
                            MztMasterTabAdapter.POSITION.SORTED -> {
                                val masterSortedViewModel: MztMasterSortedViewModel = obtainViewModel(MztMasterSortedViewModel::class.java)
                                when (which) {
                                    0 -> masterSortedViewModel.resetMasterSortedCategory(IMzituRequestService.CATEGORY.INDEX)
                                    1 -> masterSortedViewModel.resetMasterSortedCategory(IMzituRequestService.CATEGORY.HOT)
                                    2 -> masterSortedViewModel.resetMasterSortedCategory(IMzituRequestService.CATEGORY.BEST)
                                }
                            }

                            MztMasterTabAdapter.POSITION.CATEGORY -> {
                                val masterGalaxyViewModel: MztMasterGalaxyViewModel = obtainViewModel(MztMasterGalaxyViewModel::class.java)
                                when (which) {
                                    0 -> masterGalaxyViewModel.setGalazyCategoryValue(IMzituRequestService.CATEGORY.MM)
                                    1 -> masterGalaxyViewModel.setGalazyCategoryValue(IMzituRequestService.CATEGORY.SEXY)
                                    2 -> masterGalaxyViewModel.setGalazyCategoryValue(IMzituRequestService.CATEGORY.JAPAN)
                                    3 -> masterGalaxyViewModel.setGalazyCategoryValue(IMzituRequestService.CATEGORY.TW)
                                }
                            }
                        }
                    }).show()
                }
            } else -> super.onClick(v)
        }
    }

    private fun resetMasterTopLayout(top: MutableList<MztUnit>?): Unit {
        val textViews: Array<TextView> = arrayOf(
                masterPostTopGolden, masterPostTopSilver, masterPostTopBronze
        )

        top?.indices?.forEach {
            if (it < textViews.size) {
                textViews[it].text = top[it].title
                textViews[it].setOnClickListener(this@MzituMasterActivity)
            }
        }
    }

}