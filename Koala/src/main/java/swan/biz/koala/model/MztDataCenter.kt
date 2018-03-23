package swan.biz.koala.model

/**
 * Created by stephen on 12/03/2018.
 */
class MztDataCenter : IMztDataCenter {

    override var canonical: String? = null

    var pageNavigationHasPrev: Boolean? = false

    var pageNavigationHasNext: Boolean = true

    var pageNavigationCurrentNumber: String? = null

    var pageNavigationLastNumber: String? = null

    var searchPlaceHolder: String? = null

    var top: MutableList<MztUnit> = mutableListOf()

    var guess: MutableList<MztUnit> = mutableListOf()

    var love: MutableList<MztUnit> = mutableListOf()

    var postList: MutableList<MztAlbum> = mutableListOf()
}