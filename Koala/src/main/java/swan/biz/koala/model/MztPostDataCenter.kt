package swan.biz.koala.model

/**
 * Created by stephen on 12/03/2018.
 */
class MztPostDataCenter {

    var pageNavigationHasNext: Boolean = true

    var pageNavigationLastNumber: String? = null

    var guess: MutableList<MztUnit> = mutableListOf()

    var love: MutableList<MztUnit> = mutableListOf()

    var title: String? = null

    var image: MztPost = MztPost()
}