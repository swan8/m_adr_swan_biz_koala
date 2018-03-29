package swan.biz.koala.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.fivehundredpx.greedolayout.GreedoLayoutManager
import com.fivehundredpx.greedolayout.GreedoLayoutSizeCalculator
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.mzt_post.*
import swam.atom.core.extensions.obtainViewModel
import swan.atom.core.base.AtomCoreBaseActivity
import swan.biz.koala.KoalaApplicationImpl
import swan.biz.koala.R
import swan.biz.koala.RecyclerItemClickListener
import swan.biz.koala.adapter.item.MztPostMasterNavigationItem
import swan.biz.koala.network.IMzituApiField
import swan.biz.koala.vm.MztPostViewModel


/**
 * Created by stephen on 18-3-19.
 */
class MztPostActivity : AtomCoreBaseActivity() {

    companion object {

        fun newInstance(context: AtomCoreBaseActivity, postId: String): Unit {
            val intent: Intent = Intent(context, MztPostActivity::class.java)
            intent.putExtra(IMzituApiField.postId, postId)
            context.startActivity(intent)
        }
    }

    var postId: String? = null

    var fastItemAdapter: FastItemAdapter<MztPostMasterNavigationItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mzt_post)

        postId = intent.getStringExtra(IMzituApiField.postId)

        postImage.setOnPhotoTapListener { view, x, y ->
            val behavior: BottomSheetBehavior<LinearLayout> = BottomSheetBehavior.from(postMasterPostMetaContainer)
            behavior.setSkipCollapsed(false);
            when (behavior.state) {
                BottomSheetBehavior.STATE_EXPANDED ->
                        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                BottomSheetBehavior.STATE_COLLAPSED ->
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {

                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }
            })
        }

        fastItemAdapter = FastItemAdapter<MztPostMasterNavigationItem>()
        postMasterNavigationContainer.adapter = fastItemAdapter

        postMasterNavigationContainer.layoutManager = GridLayoutManager(applicationContext, 8, RecyclerView.VERTICAL, false)
//        postMasterNavigationContainer.layoutManager = GridLayoutManager(applicationContext, 3, RecyclerView.HORIZONTAL, false)

//        var layoutManager: GreedoLayoutManager = GreedoLayoutManager(GreedoLayoutSizeCalculator.SizeCalculatorDelegate { 1.0 })
//        layoutManager.setMaxRowHeight(KoalaApplicationImpl.getDimensionPixelOffset(R.dimen.mzt_resDimensGreedoDefaultRowHeight36))
//
//        postMasterNavigationContainer.layoutManager = layoutManager
        postMasterNavigationContainer.addOnItemTouchListener(RecyclerItemClickListener(applicationContext, object: RecyclerItemClickListener.SimpleOnItemClickListener() {

            override fun onItemClick(childView: View?, position: Int) {

            }
        }))

        val postViewModel: MztPostViewModel = obtainViewModel(MztPostViewModel::class.java)
        postViewModel.resetMasterSortedCategory(postId!!)
        postViewModel.dataCenter.observe(this, android.arch.lifecycle.Observer {
            it?.image?.let {
                postImage.setPhotoUri(Uri.parse(it.image))

                postMasterPostTitle.text = it.title
                postMasterPostCreateTime.text = it.time
            }

            it?.pageNavigationLastNumber?.let {
                val items: MutableList<MztPostMasterNavigationItem> = mutableListOf()
                for (i in 1 .. it.toInt()) {
                    items.add(MztPostMasterNavigationItem(it))
                }

                fastItemAdapter?.set(items)
            }
        })

        postViewModel.postRequestDataCenter(true)
    }

}