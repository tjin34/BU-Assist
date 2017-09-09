package net.bucssa.buassist.Api;

import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.News.NewsContent;
import net.bucssa.buassist.Bean.Thread.Collection;
import net.bucssa.buassist.Bean.Thread.TuiSong;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by KimuraShin on 17/7/6.
 */

public interface TuiSongAPI {



//    @GET("thread/getOfficialThreads.php")
//    Observable<BaseEntity<List<TuiSong>>> getOfficialThreads(@Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);
//
//
//    @GET("thread/getThreadContent.php")
//    Observable<BaseEntity<NewsContent>> getThreadContent(@Query("tid") int tid);


    /**
     * 添加入收藏夹
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("thread/collection/add.php")
    Observable<BaseEntity> addCollection(@Body RequestBody json);


    /**
     * 删除收藏
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("thread/collection/delete.php")
    Observable<BaseEntity> delCollection(@Body RequestBody json);


    /**
     * 获取收藏夹列表
     *
     * @param uid
     * @param pageIndex
     * @param pageSize
     * @param token
     * @return
     */
    @GET("thread/collection/get.php")
    Observable<BaseEntity<List<Collection>>> getCollection(@Query("uid") int uid,
                                                           @Query("pageIndex") int pageIndex,
                                                           @Query("pageSize") int pageSize,
                                                           @Query("token") String token);

    /**
     * 获取官方推送列表
     *
     * @param uid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GET("thread/getOfficialThreads.php")
    Observable<BaseEntity<List<TuiSong>>> getOfficialThreads(@Query("uid") int uid,
                                                             @Query("pageIndex") int pageIndex,
                                                             @Query("pageSize") int pageSize);


    /**
     * 获取推送详情页
     *
     * @param tid
     * @return
     */
    @GET("thread/getThreadContent.php")
    Observable<BaseEntity<NewsContent>> getThreadContent(@Query("tid") int tid);





}
