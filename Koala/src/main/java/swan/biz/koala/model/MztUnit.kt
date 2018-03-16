package swan.biz.koala.model

/**
 * Created by stephen on 12/03/2018.
 */
open class MztUnit {

    var unitId: String? = null
        set(value) {
            field = value?.substringAfterLast("/", "")
        }

    var title: String? = null

    var image: String? = null
}