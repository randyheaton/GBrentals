package com.injectorsuite.myapplication

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query




data class Results(val results:List<CardInfo>)

data class Image(val screen_large_url:String)

data class CardInfo(val name:String,val image:Image,val number_of_user_reviews:Int)

enum class ContentType{
    Popular,Search
}

//This is too tightly coupled with SearchPageViewModel() and I wouldn't actually do it in production, but I wanted to try something new for a single-screen app
//region QueryGrammar()
class QueryGrammar(){
    fun useInterface(): ApiInterface{
        return ApiInterface.create()
    }

    fun ApiInterface.forContentType(contentType:ContentType):ContentType{
        return contentType
    }

    suspend fun ContentType.withParameters(pagificationOffset:Int,limit:Int,searchText:String):List<CardInfo>?{
        when(this){
            ContentType.Popular->{return useInterface().queryNewGamesBatch(pagificationOffset.toString(),limit.toString()).body()?.results}
            ContentType.Search->{return useInterface().queryNewSearchBatch(searchText,pagificationOffset.toString(),limit.toString()).body()?.results}
        }

    }
}
//endregion



//region ApiInterface
interface ApiInterface {

    @GET("games")
    suspend fun queryNewGamesBatch(@Query("offset") offsetStringifed: String,@Query("limit") limitStringified:String): Response<Results>

    @GET("search")
    suspend fun queryNewSearchBatch(@Query("query") searchText: String, @Query("page") pageStringifed: String, @Query("limit") limitStringified:String): Response<Results>


    companion object{
        object queryParamAppendingInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                return chain.proceed(chain.request().let {
                    return@let it.newBuilder().url(it.url.newBuilder().addQueryParameter("api_key", KeyProviderSingleInstance.fetchKey("GiantBomb"))
                        .addQueryParameter("format", "json").build()).build()
                })



            }
        }


        fun constructClientWithAppendedAPIKey(): OkHttpClient {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            return OkHttpClient.Builder().addInterceptor(queryParamAppendingInterceptor).addInterceptor(httpLoggingInterceptor).build()
        }


        fun create(): ApiInterface {
            val client = constructClientWithAppendedAPIKey()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://www.giantbomb.com/api/")
                .client(client)
                .build()
            return retrofit.create(ApiInterface::class.java)

        }
    }
}
//endregion


