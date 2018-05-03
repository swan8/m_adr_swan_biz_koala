package swan.biz.koala.adapter.item

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.mzt_master_sorted_body.view.*
import swan.atom.core.base.AtomCoreBaseFasterItem
import swan.atom.core.base.AtomCoreBaseFasterItemHolder
import swan.biz.koala.KoalaApplicationImpl
import swan.biz.koala.R
import swan.biz.koala.model.MztAlbum

/**
 * Created by stephen on 09/03/2018.
 */
class MztSortedListBodyItem(album: MztAlbum) : AtomCoreBaseFasterItem<MztAlbum, MztSortedListBodyItem, MztSortedListBodyItem.MztSortedListBodyItemHolder>(album) {

    override fun getType(): Int {
        return R.id.mzt_resAdapterItem_AlbumList
    }

    override fun getViewHolder(view: View?): MztSortedListBodyItemHolder {
        return MztSortedListBodyItemHolder(view)
    }

    override fun getLayoutRes(): Int {
        return R.layout.mzt_master_sorted_body
    }

    override fun bindView(holder: MztSortedListBodyItemHolder?, payloads: MutableList<Any>?) {
        super.bindView(holder!!, payloads!!)
        holder.onBindView(KoalaApplicationImpl.getContext(), src)
    }

    class MztSortedListBodyItemHolder(view: View?) : AtomCoreBaseFasterItemHolder<MztAlbum>(view) {

        override fun onBindView(context: Context?, src: MztAlbum?) {
//            itemView.bodyImage.setImageURI(src?.image)
        }
    }
}