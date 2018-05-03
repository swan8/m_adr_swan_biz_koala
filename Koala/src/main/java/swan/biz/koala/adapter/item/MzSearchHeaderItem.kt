package swan.biz.koala.adapter.item

import android.content.Context
import android.view.View
import swan.atom.core.base.AtomCoreBaseFasterItem
import swan.atom.core.base.AtomCoreBaseFasterItemHolder
import swan.biz.koala.R

class MzSearchHeaderItem(res: Int) : AtomCoreBaseFasterItem<Int, MzSearchHeaderItem, MzSearchHeaderItem.MzSearchHeaderItemHolder>(res) {

    override fun getType(): Int {
        return R.id.mz_resAdapterItem_SearchHeader
    }

    override fun getViewHolder(v: View?): MzSearchHeaderItemHolder {
        return MzSearchHeaderItemHolder(v)
    }

    override fun getLayoutRes(): Int {
        return R.layout.mz_search_header
    }

    class MzSearchHeaderItemHolder(view: View?) : AtomCoreBaseFasterItemHolder<Int>(view) {

        override fun onBindView(context: Context?, src: Int?) {

        }
    }
}