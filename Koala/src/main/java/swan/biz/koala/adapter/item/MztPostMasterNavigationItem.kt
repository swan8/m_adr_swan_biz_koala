package swan.biz.koala.adapter.item

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.mzt_post_master_navigation_body.view.*
import swan.atom.core.base.AtomCoreBaseFasterItem
import swan.atom.core.base.AtomCoreBaseFasterItemHolder
import swan.biz.koala.KoalaApplicationImpl
import swan.biz.koala.R

/**
 * Created by stephen on 09/03/2018.
 */
class MztPostMasterNavigationItem(position: String) : AtomCoreBaseFasterItem<String, MztPostMasterNavigationItem, MztPostMasterNavigationItem.PostMasterNavigationBodyHolder>(position) {

    override fun getType(): Int {
        return R.id.mzt_resAdapterItem_PostMasterNavigationBody
    }

    override fun getViewHolder(view: View?): PostMasterNavigationBodyHolder {
        return PostMasterNavigationBodyHolder(view)
    }

    override fun getLayoutRes(): Int {
        return R.layout.mzt_post_master_navigation_body
    }

    override fun bindView(holder: PostMasterNavigationBodyHolder?, payloads: MutableList<Any>?) {
        super.bindView(holder!!, payloads!!)
        holder.onBindView(KoalaApplicationImpl.getContext(), src)
    }

    class PostMasterNavigationBodyHolder(view: View?) : AtomCoreBaseFasterItemHolder<String>(view) {

        override fun onBindView(context: Context?, src: String?) {
            itemView.bodyPageNo.text = layoutPosition.toString()
        }
    }
}