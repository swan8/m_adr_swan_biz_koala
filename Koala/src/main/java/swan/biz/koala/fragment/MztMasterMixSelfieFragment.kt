package swan.biz.koala.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import swan.atom.core.base.AtomCoreBaseFragment
import swan.biz.koala.R

/**
 * Created by stephen on 13/03/2018.
 */
class MztMasterMixSelfieFragment : AtomCoreBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.mzt_master_sorted, container, false)
    }

}