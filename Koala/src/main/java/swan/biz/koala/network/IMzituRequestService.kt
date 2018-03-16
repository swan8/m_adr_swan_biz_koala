package swan.biz.koala.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by stephen on 18-3-9.
 */
interface IMzituRequestService {
    
    companion object {

        const val BASE_URL = "http://www.mzitu.com/"

        const val API_PAGE = "page/{page}"

        const val BASE_PATH = "{galaxyCategory}/${IMzituRequestService.API_PAGE}"

        const val API_IMAGE_LIST = "{imageId}/{page}"
    }

    class CATEGORY {

        companion object {

            const val INDEX: String = ""

            const val HOT: String = "hot"

            const val BEST: String = "best"

            const val MM: String = "mm"

            const val SEXY: String = "xinggan"

            const val JAPAN: String = "japan"

            const val TW: String = "taiwan"

            const val SELFIE: String = "zipai"
        }
    }

    @GET(IMzituRequestService.BASE_PATH)
    fun postRequestMztPagePath(
            @Path(IMzituApiField.category) category: String,
            @Path(IMzituApiField.page) page: Int
    ): Observable<String>

    @GET(IMzituRequestService.API_IMAGE_LIST)
    fun postRequestMzituImageList(
            @Path(IMzituApiField.imageId) imageId: String,
            @Path(IMzituApiField.page) page: Int
    ): Observable<String>
}