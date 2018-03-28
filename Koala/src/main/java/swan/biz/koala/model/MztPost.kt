package swan.biz.koala.model

/**
 * Created by stephen on 18-3-20.
 */
class MztPost : MztUnit() {

    var time: String? = null

    var view: String? = null
        set(value) {
            field = value?.replace("\\D+".toRegex(), "")
        }

    var category: String? = null

    var categoryName: String? = null
}