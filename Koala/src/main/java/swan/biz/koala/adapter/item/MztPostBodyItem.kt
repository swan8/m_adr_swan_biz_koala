package swan.biz.koala.adapter.item

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.mzt_master_sorted_body.view.*
import swan.atom.core.base.AtomCoreBaseFasterItem
import swan.atom.core.base.AtomCoreBaseFasterItemHolder
import swan.biz.koala.KoalaApplicationImpl
import swan.biz.koala.R
import swan.biz.koala.model.MztUnit

/**
 * Created by stephen on 09/03/2018.
 */
class MztPostBodyItem(unit: MztUnit) : AtomCoreBaseFasterItem<MztUnit, MztPostBodyItem, MztPostBodyItem.MztPostListBodyItemHolder>(unit) {

    override fun getType(): Int {
        return R.id.mzt_resAdapterItem_PostBody
    }

    override fun getViewHolder(view: View?): MztPostListBodyItemHolder {
        return MztPostListBodyItemHolder(view)
    }

    override fun getLayoutRes(): Int {
        return R.layout.mzt_post_body
    }

    override fun bindView(holder: MztPostListBodyItemHolder?, payloads: MutableList<Any>?) {
        super.bindView(holder!!, payloads!!)
        holder.onBindView(KoalaApplicationImpl.getContext(), src)
    }

    class MztPostListBodyItemHolder(view: View?) : AtomCoreBaseFasterItemHolder<MztUnit>(view) {

        override fun onBindView(context: Context?, src: MztUnit?) {
            itemView.bodyImage.setImageURI(src?.image)
        }
    }
}