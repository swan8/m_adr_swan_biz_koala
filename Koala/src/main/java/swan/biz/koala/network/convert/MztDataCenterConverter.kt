package swan.biz.koala.network.convert

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import retrofit2.Converter
import swan.biz.koala.database.MzPersistentTop
import swan.biz.koala.model.MztAlbum
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.model.MztUnit
import swan.biz.koala.network.IMzApiRequestService
import swan.biz.koala.network.IMztNodeField

/**
 * Created by stephen on 18-3-20.
 */
object MztDataCenterConverter : Converter<ResponseBody, MztDataCenter> {

    override fun convert(value: ResponseBody?): MztDataCenter {
        return MztDataCenter().documentConverter(Jsoup.parse(value?.string()))
    }

    private fun MztDataCenter.documentConverter(document: Document?): MztDataCenter {
        document?.also {
            searchPlaceHolder = it.selectFirst(IMztNodeField.SEARCH_INPUT)?.attr(IMztNodeField.NODE_PLACEHOLDER)
        }?.also {
                    canonical = it.selectFirst(IMztNodeField.LINK.LINK_CANONICAL)?.attr(IMztNodeField.NODE.HREF)?.also {
                        when {
                            it.contains(IMzApiRequestService.CATEGORY.TOPIC) ->
                                postListTopicWithElements(document)
                            it.contains(IMzApiRequestService.CATEGORY.SELFIE) ->
                                postListSelfieWithElements(document)
                            else -> postListConverter(document)
                        }
                    }
                }?.also {
                    pageNavigationHasPrev = null != it.selectFirst(IMztNodeField.PAGE_NAVI_PREV)
                    pageNavigationHasNext = null != it.selectFirst(IMztNodeField.PAGE_NAVI_NEXT)
                    pageNavigationCurrentNumber = it.selectFirst(IMztNodeField.PAGE_NAVI_NUMBER_CURRENT)?.text()
                    pageNavigationLastNumber = it.select(IMztNodeField.PAGE_NAVI_NUMBER)?.last()?.text()
                }?.also {
                    it.select(IMztNodeField.WIDGET_TOP)?.forEach {
                        val persistentTop: MzPersistentTop = MzPersistentTop()
                        persistentTop.unitId = it?.selectFirst(IMztNodeField.VALID_A)?.attr(IMztNodeField.NODE_HREF)
                        persistentTop.title = it?.selectFirst(IMztNodeField.VALID_IMG_JPG)?.attr(IMztNodeField.NODE_ALT)
                        persistentTop.image = it?.selectFirst(IMztNodeField.VALID_IMG_JPG)?.attr(IMztNodeField.NODE_SRC)

                        top.add(persistentTop)
                    }
                }?.also {
                    it.select(IMztNodeField.WIDGET_LIKE_GUESS)?.forEach {
                        val unit: MztUnit = MztUnit()
                        guess.add(unit)

                        unit.unitId = it?.selectFirst(IMztNodeField.VALID_A)?.attr(IMztNodeField.NODE_HREF)
                        unit.title = it?.selectFirst(IMztNodeField.VALID_A)?.text()
                    }
                }?.also {
                    document.select(IMztNodeField.WIDGET_LIKE_LOVE)?.forEach {
                        val unit: MztUnit = MztUnit()
                        love.add(unit)

                        unit.unitId = it?.selectFirst(IMztNodeField.VALID_A)?.attr(IMztNodeField.NODE_HREF)
                        unit.title = it?.selectFirst(IMztNodeField.VALID_A)?.text()
                    }
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

    private fun MztDataCenter.postListTopicWithElements(document: Document?) {
        document?.select(IMztNodeField.POST_LIST_TOPIC)?.forEach {
            val album: MztAlbum = MztAlbum()
            postList.add(album)

            album.unitId = it?.selectFirst(IMztNodeField.VALID_A)?.attr(IMztNodeField.NODE_HREF)
            album.time = it?.selectFirst(IMztNodeField.POST_LIST_TIME)?.html()
            album.view = it?.selectFirst(IMztNodeField.NODE_I)?.text()

            val imgElement: Element? = it?.selectFirst(IMztNodeField.VALID_IMG_JPG)
            imgElement?.let {
                album.title = it.attr(IMztNodeField.NODE_ALT)
                album.image = it.attr(IMztNodeField.NODE_SRC)
            }
        }
    }

    private fun MztDataCenter.postListSelfieWithElements(document: Document?) {
        document?.select(IMztNodeField.POST_LIST_SELFIE)?.forEach {
            val album: MztAlbum = MztAlbum()
            postList.add(album)

            album.time = it?.selectFirst(IMztNodeField.VALID_A)?.text()
            album.image = it?.selectFirst(IMztNodeField.VALID_IMG_JPG)?.attr(IMztNodeField.NODE_SRC)
        }
    }
}