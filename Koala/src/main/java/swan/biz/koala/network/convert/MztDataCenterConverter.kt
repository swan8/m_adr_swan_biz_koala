package swan.biz.koala.network.convert

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import retrofit2.Converter
import swan.biz.koala.model.MztAlbum
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.model.MztUnit
import swan.biz.koala.network.IMzituRequestService
import swan.biz.koala.network.IMztNodeField

/**
 * Created by stephen on 18-3-20.
 */
object MztDataCenterConverter : Converter<ResponseBody, MztDataCenter> {

    override fun convert(value: ResponseBody?): MztDataCenter {
        return MztDataCenter().documentConverter(Jsoup.parse(value?.string()))
    }

    private fun MztDataCenter.documentConverter(document: Document?): MztDataCenter {
        searchPlaceHolder = document?.selectFirst(IMztNodeField.SEARCH_INPUT)?.attr(IMztNodeField.NODE_PLACEHOLDER)

        canonical = document?.selectFirst(IMztNodeField.LINK.LINK_CANONICAL)?.attr(IMztNodeField.NODE.HREF)?.also {
            when {
                it == IMzituRequestService.BASE_URL
                        || it.contains(IMzituRequestService.CATEGORY.BEST) ->
                    postListConverter(document)
            }
        }

        document?.select(IMztNodeField.WIDGET_TOP)?.forEach {
            val unit: MztUnit = MztUnit()
            unit.unitId = it?.selectFirst(IMztNodeField.VALID_A)?.attr(IMztNodeField.NODE_HREF)
            unit.title = it?.selectFirst(IMztNodeField.VALID_IMG_JPG)?.attr(IMztNodeField.NODE_ALT)
            unit.image = it?.selectFirst(IMztNodeField.VALID_IMG_JPG)?.attr(IMztNodeField.NODE_SRC)

            top.add(unit)
        }

        document?.select(IMztNodeField.WIDGET_LIKE_GUESS)?.forEach {
            val unit: MztUnit = MztUnit()
            guess.add(unit)

            unit.unitId = it?.selectFirst(IMztNodeField.VALID_A)?.attr(IMztNodeField.NODE_HREF)
            unit.title = it?.selectFirst(IMztNodeField.VALID_A)?.text()
        }

        document?.select(IMztNodeField.WIDGET_LIKE_LOVE)?.forEach {
            val unit: MztUnit = MztUnit()
            love.add(unit)

            unit.unitId = it?.selectFirst(IMztNodeField.VALID_A)?.attr(IMztNodeField.NODE_HREF)
            unit.title = it?.selectFirst(IMztNodeField.VALID_A)?.text()
        }

        return this
    }

    private fun MztDataCenter.postListConverter(document: Document?): MztDataCenter {
        document?.select(IMztNodeField.POST_LIST)?.forEach {
            val album: MztAlbum = MztAlbum()
            postList.add(album)

            album.unitId = it?.selectFirst(IMztNodeField.VALID_A)?.attr(IMztNodeField.NODE_HREF)
            album.time = it?.selectFirst(IMztNodeField.POST_LIST_TIME)?.text()
            album.view = it?.selectFirst(IMztNodeField.POST_LIST_VIEW)?.text()

            val imgElement: Element? = it?.selectFirst(IMztNodeField.VALID_IMG_ORIGINAL)
            imgElement?.let {
                album.title = it.attr(IMztNodeField.NODE_ALT)
                album.image = it.attr(IMztNodeField.NODE_DATA_ORIGINAL)
            }
        }

        return this
    }
}