package swan.biz.koala.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.mzt_post.*
import me.dkzwm.widget.srl.SmoothRefreshLayout
import me.dkzwm.widget.srl.extra.footer.MaterialFooter
import me.dkzwm.widget.srl.extra.header.MaterialHeader
import me.dkzwm.widget.srl.indicator.DefaultIndicator
import me.dkzwm.widget.srl.utils.PixelUtl
import swam.atom.core.extensions.obtainViewModel
import swan.atom.core.base.AtomCoreBaseActivity
import swan.biz.koala.R
import swan.biz.koala.adapter.item.MztPostBodyItem
import swan.biz.koala.network.IMzituApiField
import swan.biz.koala.vm.MztPostViewModel


/**
 * Created by stephen on 18-3-19.
 */
class MztPostActivity : AtomCoreBaseActivity(), SmoothRefreshLayout.OnRefreshListener {

    companion object {

        fun newInstance(context: AtomCoreBaseActivity, postId: String): Unit {
            val intent: Intent = Intent(context, MztPostActivity::class.java)
            intent.putExtra(IMzituApiField.postId, postId)
            context.startActivity(intent)
        }
    }

    var postId: String? = null

    var fastItemAdapter: FastItemAdapter<MztPostBodyItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mzt_post)

        postId = intent.getStringExtra(IMzituApiField.postId)

        postRecyclerContainer.let {
            it.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            it.setHasFixedSize(true)

            PagerSnapHelper().attachToRecyclerView(it)

            fastItemAdapter = FastItemAdapter<MztPostBodyItem>()
            it.adapter = fastItemAdapter
        }

        val postViewModel: MztPostViewModel = obtainViewModel(MztPostViewModel::class.java)
        postViewModel.resetMasterSortedCategory(postId!!)
        postViewModel.dataCenter.observe(this, android.arch.lifecycle.Observer {
            it?.image?.let {
                val items: MutableList<MztPostBodyItem> = mutableListOf()
                items.add(MztPostBodyItem(it))
                fastItemAdapter?.add(items)
            }

            postRefreshContainer.setDisableLoadMore(! it?.pageNavigationHasNext!!)
            postRefreshContainer.refreshComplete()
        })

        postRefreshContainer.let {
            val header = MaterialHeader<DefaultIndicator>(this)
            header.setColorSchemeColors(intArrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK))
            header.setPadding(PixelUtl.dp2px(this, 25f), 0, PixelUtl.dp2px(this, 25f), 0)
            it.setHeaderView(header)

            val footer = MaterialFooter<DefaultIndicator>(this)
            footer.setProgressBarColors(intArrayOf(Color.RED, Color.BLUE, Color.GREEN, Color.BLACK))
            it.setFooterView(footer)

            it.setOnRefreshListener(this)
            it.setDisableLoadMore(true)
            it.autoRefresh()
        }
    }

    override fun onRefreshBegin(isRefresh: Boolean) {
        val postViewModel: MztPostViewModel = obtainViewModel(MztPostViewModel::class.java)
        postViewModel.postRequestDataCenter(isRefresh)
    }

    override fun onRefreshComplete(isSuccessful: Boolean) {

    }

}