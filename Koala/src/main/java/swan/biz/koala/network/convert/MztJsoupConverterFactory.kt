package swan.biz.koala.network.convert

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import swan.biz.koala.model.MztDataCenter
import swan.biz.koala.model.MztPostDataCenter
import java.lang.reflect.Type

/**
 * Created by stephen on 18-3-20.
 */
object MztJsoupConverterFactory : Converter.Factory() {

    /**
     * 这个方法可以忽略掉，由retrofit2.converter.scalars.ScalarRequestBodyConverter接管处理
     */
    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<out Annotation>?, methodAnnotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        return null
    }

    override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        return when (type) {
            MztDataCenter::class.java ->
                MztDataCenterConverter
            MztPostDataCenter::class.java ->
                MztPostDataCenterConverter
            else -> null
        }
    }
}