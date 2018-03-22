package swan.biz.koala.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.model.MztPostDataCenter

/**
 * Created by stephen on 18-3-9.
 */
interface IMzituRequestService {
    
    companion object {

        const val BASE_URL = "http://www.mzitu.com/"

        const val API_PAGE = "page/{page}/"

        const val API_COMMENT_PAGE = "comment-page-{page}"

        const val BASE_PATH = "{category}/${IMzituRequestService.API_PAGE}"

        const val BASE_PATH_NO_PAGE = "{category}/"

        const val BASE_COMMENT_PATH = "{category}/${IMzituRequestService.API_COMMENT_PAGE}"

        const val API_IMAGE_LIST = "{imageId}/{page}"
    }

    class CATEGORY {

        companion object {

            const val INDEX: String = ""

            const val HOT: String = "hot"

            const val BEST: String = "best"

            const val MM: String = "mm"

            const val SEXY: String = "xinggan" // no cache-control

            const val JAPAN: String = "japan"

            const val TW: String = "taiwan"

            const val SELFIE: String = "zipai"

            const val TOPIC: String = "zhuanti" // no cache-control
        }
    }

    @GET(IMzituRequestService.BASE_PATH)
    fun postRequestMztPagePath(
            @Path(IMzituApiField.category) category: String,
            @Path(IMzituApiField.page) page: Int
    ): Observable<String>

    @GET(IMzituRequestService.BASE_PATH_NO_PAGE)
    fun postRequestMztPagePath(
            @Path(IMzituApiField.category) category: String
    ): Observable<String>

    @GET(IMzituRequestService.BASE_COMMENT_PATH)
    fun postRequestMztCommentPagePath(
            @Path(IMzituApiField.category) category: String,
            @Path(IMzituApiField.page) page: Int
    ): Observable<String>


    @GET(IMzituRequestService.BASE_PATH)
    fun postRequestMztPagePathData(
            @Path(IMzituApiField.category) category: String,
            @Path(IMzituApiField.page) page: Int
    ): Observable<MztDataCenter>

    @GET(IMzituRequestService.API_IMAGE_LIST)
    fun postRequestMztPostData(
            @Path(IMzituApiField.imageId) imageId: String,
            @Path(IMzituApiField.page) page: Int
    ): Observable<MztPostDataCenter>
}