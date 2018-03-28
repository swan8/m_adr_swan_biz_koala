package swan.biz.koala.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.view.View
import android.widget.LinearLayout
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import kotlinx.android.synthetic.main.mzt_post.*
import swam.atom.core.extensions.obtainViewModel
import swan.atom.core.base.AtomCoreBaseActivity
import swan.biz.koala.R
import swan.biz.koala.adapter.item.MztPostBodyItem
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

    var fastItemAdapter: FastItemAdapter<MztPostBodyItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mzt_post)

        postId = intent.getStringExtra(IMzituApiField.postId)

        postImage.setOnPhotoTapListener {
            view, x, y -> kotlin.run {
                val behavior: BottomSheetBehavior<LinearLayout> = BottomSheetBehavior.from(postMasterPostMetaContainer)
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
        }

        val postViewModel: MztPostViewModel = obtainViewModel(MztPostViewModel::class.java)
        postViewModel.resetMasterSortedCategory(postId!!)
        postViewModel.dataCenter.observe(this, android.arch.lifecycle.Observer {
            it?.image?.let {
                val items: MutableList<MztPostBodyItem> = mutableListOf()
                items.add(MztPostBodyItem(it))
                fastItemAdapter?.add(items)

                postImage.setPhotoUri(Uri.parse(it.image))

                postMasterPostTitle.text = it.title
                postMasterPostCreateTime.text = it.time
            }
        })

        postViewModel.postRequestDataCenter(true)
    }

}