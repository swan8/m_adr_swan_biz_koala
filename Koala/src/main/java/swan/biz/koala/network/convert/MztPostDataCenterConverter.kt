package swan.biz.koala.network.convert

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Converter
import swan.biz.koala.model.MztPostDataCenter
import swan.biz.koala.model.MztUnit
import swan.biz.koala.network.IMztNodeField

/**
 * Created by stephen on 18-3-20.
 */
fun MztPostDataCenter.responseBodyConverter() : Converter<ResponseBody, MztPostDataCenter> {
    return Converter<ResponseBody, MztPostDataCenter> {
        this.documentConverter(Jsoup.parse(it.string()))
    }
}

fun MztPostDataCenter.documentConverter(document: Document?) : MztPostDataCenter {
    pageNavigationLastNumber = document?.select(IMztNodeField.Post.POST_LAST_PAGE_NO)?.last()?.text()

    image.documentConverter(document)

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