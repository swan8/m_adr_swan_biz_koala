package swan.biz.koala.network

/**
 * Created by stephen on 12/03/2018.
 */
class IMztNodeField {

    companion object {

        const val NODE_ALT: String = "alt"

        const val NODE_HREF: String = "href"

        const val NODE_SRC: String = "src"

        const val NODE_DATA_ORIGINAL: String = "data-original"

        const val NODE_PLACEHOLDER: String = "placeholder"


        const val VALID_A: String = "a[abs:${IMztNodeField.NODE_HREF}]" // 有效a标签

        const val VALID_IMG: String = "img[src~=(?i)\\.(png|jpe?g)]" // 有效img标签

        const val VALID_IMG_JPG: String = "img[src~=(?i)\\.(jpe?g)]" // 有效img.jpg标签

        const val VALID_IMG_PNG: String = "img[src$=png]" // 有效img.png标签

        const val VALID_IMG_ORIGINAL: String = "img[${IMztNodeField.NODE_DATA_ORIGINAL}]" // 有效img.data-original标签

        const val SEARCH_INPUT: String = "input.search-input"

        const val POST_LIST: String = "div.postlist li" // 专辑(也可以使用[^data]来匹配)

        const val POST_LIST_TIME = "span.time" // 专辑日期

        const val POST_LIST_VIEW = "span.view" // 专辑浏览次数

        const val PAGE_NAVI_NUMBER: String = "a[class=page-numbers]" // 导航条页码

        const val PAGE_NAVI_PREV: String = "a[class=prev page-numbers]" // 上一页

        const val PAGE_NAVI_NEXT: String = "a[class=next page-numbers]" // 下一页

        const val WIDGET_TOP: String = "div.widgets_top ${IMztNodeField.VALID_A}" // 排行榜(<a><img>)

        const val WIDGET_LIKE: String = "dl#like"

        const val WIDGET_LIKE_GUESS: String = WIDGET_LIKE + " dd:not(dd.no)"

        const val WIDGET_LIKE_LOVE: String = WIDGET_LIKE + " dd.no"

        const val TAG: String = "a[abs:href~=(?i)tag]" // 标签
    }
}