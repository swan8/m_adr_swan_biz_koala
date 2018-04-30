package swan.biz.koala.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import kotlinx.android.synthetic.main.mzt_master.*
import swan.atom.core.base.AtomCoreBaseToolbarActivity
import swan.atom.core.extensions.obtainViewModel
import swan.biz.koala.R
import swan.biz.koala.adapter.MztMasterTabAdapter
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.network.IMzituRequestService
import swan.biz.koala.vm.MztMasterGalaxyViewModel
import swan.biz.koala.vm.MztMasterSortedViewModel

/**
 * Created by stephen on 18-3-9.
 */
class MzituMasterActivity: AtomCoreBaseToolbarActivity(), Toolbar.OnMenuItemClickListener {

    private var dataCenter: MztDataCenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mzt_master)

        layoutResId = R.layout.mzt_master

        cirrusResId = R.id.cirrus

        cirrus?.let {
            builder
                    .withCirrusTitle(R.string.app_name)
                    .withCirrusMenu(R.menu.mz_master_cirrus_sorted_menu, this@MzituMasterActivity)
                    .withCirrusOverflowResId(R.drawable.cirrus_ic_category_white_64dp)
                    .build(it)
        }

        immersionBar.init()

        val masterSortedViewModel: MztMasterSortedViewModel? = obtainViewModel(MztMasterSortedViewModel::class.java)
        masterSortedViewModel?.dataCenter?.observe(this, android.arch.lifecycle.Observer {
            this@MzituMasterActivity.dataCenter = it
        })

        masterPagerContainer.let {
            it.adapter = MztMasterTabAdapter(applicationContext, supportFragmentManager)
            it.offscreenPageLimit = MztMasterTabAdapter.POSITION.ALL
            it.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(masterTabContainer))
            it.addOnPageChangeListener(object: ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    when (position) {
                        MztMasterTabAdapter.POSITION.SORTED ->
                            builder.withCirrusMenu(R.menu.mz_master_cirrus_sorted_menu, this@MzituMasterActivity).build(cirrus)
                        MztMasterTabAdapter.POSITION.CATEGORY ->
                            builder.withCirrusMenu(R.menu.mz_master_cirrus_category_menu, this@MzituMasterActivity).build(cirrus)
                        else ->
                            builder.withCirrusMenu(R.menu.mz_master_cirrus_normal_menu, this@MzituMasterActivity).build(cirrus)
                    }
                }
            })
        }

        masterTabContainer.let {
            it.setupWithViewPager(masterPagerContainer)
            it.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(masterPagerContainer))
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return item?.itemId?.let { itemId ->
            if (itemId == R.id.cirrusMenuSearch) {
                MzSearchActivity.newInstance(this@MzituMasterActivity, this@MzituMasterActivity.dataCenter, "")
                return false
            }

            val masterSortedViewModel: MztMasterSortedViewModel = obtainViewModel(MztMasterSortedViewModel::class.java)
            when (itemId) {
                R.id.cirrusMenuSortedIndex -> masterSortedViewModel.resetMasterSortedCategory(IMzituRequestService.CATEGORY.INDEX)
                R.id.cirrusMenuSortedHot -> masterSortedViewModel.resetMasterSortedCategory(IMzituRequestService.CATEGORY.HOT)
                R.id.cirrusMenuSortedRecommend -> masterSortedViewModel.resetMasterSortedCategory(IMzituRequestService.CATEGORY.BEST)

                else -> {
                    val masterGalaxyViewModel: MztMasterGalaxyViewModel = obtainViewModel(MztMasterGalaxyViewModel::class.java)
                    when (itemId) {
                        R.id.cirrusMenuCategoryGirl -> masterGalaxyViewModel.resetMasterSortedCategory(IMzituRequestService.CATEGORY.MM)
                        R.id.cirrusMenuCategorySex -> masterGalaxyViewModel.resetMasterSortedCategory(IMzituRequestService.CATEGORY.SEXY)
                        R.id.cirrusMenuCategoryJapan -> masterGalaxyViewModel.resetMasterSortedCategory(IMzituRequestService.CATEGORY.JAPAN)
                        R.id.cirrusMenuCategoryTaiwan -> masterGalaxyViewModel.resetMasterSortedCategory(IMzituRequestService.CATEGORY.TW)
                    }
                }
            }

            false
        } ?: false
    }
}