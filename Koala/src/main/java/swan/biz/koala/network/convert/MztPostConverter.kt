package swan.biz.koala.network.convert

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Converter
import swan.biz.koala.model.MztPost
import swan.biz.koala.network.IMztNodeField

/**
 * Created by stephen on 18-3-20.
 */
fun MztPost.responseBodyConverter() : Converter<ResponseBody, MztPost> {
    return Converter<ResponseBody, MztPost> {
        this.documentConverter(Jsoup.parse(it.string()))
    }
}

fun MztPost.documentConverter(document: Document?) : MztPost {
    document?.also {
        title = it.selectFirst(IMztNodeField.Post.POST_MAIN_TITLE)?.text()
        image = it.selectFirst(IMztNodeField.Post.POST_MAIN_IMAGE)?.attr(IMztNodeField.NODE.SRC)
    }?.let {
                category = it.selectFirst(IMztNodeField.META.META_CATEGORY).attr(IMztNodeField.NODE.HREF)
                categoryName = it.selectFirst(IMztNodeField.META.META_CATEGORY).text()

                it.select(IMztNodeField.META.META_PURE_SPAN)
            }?.also {
                time = it.first()?.text()
                view = it.last()?.text()
            }

    return this
}