package swan.biz.koala.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import swan.biz.koala.KoalaApplicationImpl
import swan.biz.koala.R
import swan.biz.koala.fragment.*

/**
 * Created by stephen on 01/03/2018.
 */
class MztMasterTabAdapter constructor(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    interface POSITION {

        companion object {

            const val SORTED: Int = 0

            const val CATEGORY = SORTED + 1

            const val MIX_TOPIC = CATEGORY + 1

            const val MIX_SELFIE = MIX_TOPIC + 1

            const val MIX_AXIS = MIX_SELFIE + 1

            const val ALL = MIX_AXIS + 1
        }
    }

    var context: Context? = null

    var masterSortedFragment: MztMasterSortedFragment? = null

    var masterGalaxyFragment: MztMasterGalaxyFragment? = null

    var masterMixTopicFragment: MztMasterMixTopicFragment? = null

    var masterMixSelfieFragment: MztMasterMixSelfieFragment? = null

    var masterMixAxisFragment: MztMasterMixAxisFragment? = null

    constructor(context: Context, fragmentManager: FragmentManager): this(fragmentManager) {
        this.context = context
    }

    init {
        masterSortedFragment = MztMasterSortedFragment()
        masterGalaxyFragment = MztMasterGalaxyFragment()
        masterMixTopicFragment = MztMasterMixTopicFragment()
        masterMixSelfieFragment = MztMasterMixSelfieFragment()
        masterMixAxisFragment = MztMasterMixAxisFragment()
    }

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            POSITION.SORTED ->
                masterSortedFragment
            POSITION.CATEGORY ->
                masterGalaxyFragment
            POSITION.MIX_TOPIC ->
                masterMixTopicFragment
            POSITION.MIX_SELFIE ->
                masterMixSelfieFragment
            POSITION.MIX_AXIS ->
                masterMixAxisFragment
            else -> null
        }
    }

    override fun getCount(): Int {
        return POSITION.ALL
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            POSITION.SORTED ->
                KoalaApplicationImpl.getContext()?.getString(R.string.mzt_resStringMasterTabSorted)
            POSITION.CATEGORY ->
                KoalaApplicationImpl.getContext()?.getString(R.string.mzt_resStringMasterTabGalaxy)
            POSITION.MIX_TOPIC ->
                KoalaApplicationImpl.getContext()?.getString(R.string.mzt_resStringMasterTabMixTopic)
            POSITION.MIX_SELFIE ->
                KoalaApplicationImpl.getContext()?.getString(R.string.mzt_resStringMasterTabMixSelfie)
            POSITION.MIX_AXIS ->
                KoalaApplicationImpl.getContext()?.getString(R.string.mzt_resStringMasterTabMixAxis)
            else -> null
        }
    }
}