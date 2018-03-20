package swan.biz.koala.model

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import swan.biz.koala.network.IMztNodeField

/**
 * Created by stephen on 12/03/2018.
 */
class MztDataCenter : IMztDataCenter {

    override var canonical: String? = null

    var pageNavigationHasPrev: Boolean? = false

    var pageNavigationHasNext: Boolean = true

    var pageNavigationLastNumber: String? = null

    var searchPlaceHolder: String? = null

    var top: MutableList<MztUnit> = mutableListOf()

    var guess: MutableList<MztUnit> = mutableListOf()

    var love: MutableList<MztUnit> = mutableListOf()

    var postList: MutableList<MztAlbum> = mutableListOf()

    fun pageNavigationWithDocument(document: Document?) {
        document?.let {
            pageNavigationHasPrev = null != it.selectFirst(IMztNodeField.PAGE_NAVI_PREV)
            pageNavigationHasNext = null != it.selectFirst(IMztNodeField.PAGE_NAVI_NEXT)
            pageNavigationLastNumber = it.select(IMztNodeField.PAGE_NAVI_NUMBER)?.last()?.text()
        }
    }

    fun topWithElements(elements: Elements?) {
        elements?.forEach {
            val unit: MztUnit = MztUnit()
            unit.unitId = it?.selectFirst(IMztNodeField.VALID_A)?.attr(IMztNodeField.NODE_HREF)
            unit.title = it?.selectFirst(IMztNodeField.VALID_IMG_JPG)?.attr(IMztNodeField.NODE_ALT)
            unit.image = it?.selectFirst(IMztNodeField.VALID_IMG_JPG)?.attr(IMztNodeField.NODE_SRC)

            top.add(unit)
        }
    }

    fun guessWithElements(elements: Elements?) {
        elements?.forEach {
            val unit: MztUnit = MztUnit()
            unit.unitId = it?.selectFirst(IMztNodeField.VALID_A)?.attr(IMztNodeField.NODE_HREF)
            unit.title = it?.selectFirst(IMztNodeField.VALID_A)?.html()

            guess.add(unit)
        }
    }

    fun loveWithElements(elements: Elements?) {
        elements?.forEach {
            val unit: MztUnit = MztUnit()
            unit.unitId = it?.selectFirst(IMztNodeField.VALID_A)?.attr(IMztNodeField.NODE_HREF)
            unit.title = it?.selectFirst(IMztNodeField.VALID_A)?.html()

            love.add(unit)
        }
    }

    fun postListWithElements(elements: Elements?) {
        elements?.forEach {
            val album: MztAlbum = MztAlbum()
            album.unitId = it?.selectFirst(IMztNodeField.VALID_A)?.attr(IMztNodeField.NODE_HREF)
            album.time = it?.selectFirst(IMztNodeField.POST_LIST_TIME)?.html()
            album.view = it?.selectFirst(IMztNodeField.POST_LIST_VIEW)?.html()

            val imgElement: Element? = it?.selectFirst(IMztNodeField.VALID_IMG_ORIGINAL)
            imgElement?.let {
                album.title = it.attr(IMztNodeField.NODE_ALT)
                album.image = it.attr(IMztNodeField.NODE_DATA_ORIGINAL)
            }

            postList.add(album)
        }
    }
}