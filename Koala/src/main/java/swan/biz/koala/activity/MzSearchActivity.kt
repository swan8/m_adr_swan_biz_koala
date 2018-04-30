package swan.biz.koala.activity

import android.content.Intent
import android.os.Bundle
import swan.atom.core.base.AtomCoreBaseActivity
import swan.biz.koala.R
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.network.IMzituApiField
import kotlinx.android.synthetic.main.mz_search_master.*


class MzSearchActivity : AtomCoreBaseActivity() {

    companion object {

        fun newInstance(context: AtomCoreBaseActivity, dataCenter: MztDataCenter?, keyword: String): Unit {
            val intent: Intent = Intent(context, MzSearchActivity::class.java)
            intent.putExtra(IMzituApiField.searchPlaceHolder, dataCenter?.searchPlaceHolder)
            intent.putExtra(IMzituApiField.searchKeyword, keyword)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutResId = R.layout.mz_search_master

        immersionBar.init()

        searchInputText.hint = intent?.getStringExtra(IMzituApiField.searchPlaceHolder)


    }
}