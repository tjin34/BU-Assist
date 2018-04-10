package net.bucssa.buassist.Api;


import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Message.Chat;
import net.bucssa.buassist.Bean.Message.Message;
import net.bucssa.buassist.Bean.Message.NewMsg;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by KimuraShin on 17/7/24.
 */

public interface PersonalMessageAPI {

    /**
     * 创建新的对话
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("personalMessage/chat/create.php")
    Observable<BaseEntity> createChat(@Body RequestBody json);


    /**
     * 删除对话
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("personalMessage/chat/delete.php")
    Observable<BaseEntity> deleteChat(@Body RequestBody json);


    /**
     * 获取对话列表
     *
     * @param uid
     * @param token
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GET("personalMessage/chat/get.php")
    Observable<BaseEntity<List<Chat>>> getChats(@Query("uid") int uid,
                                                @Query("token") String token,
                                                @Query("pageIndex") int pageIndex,
                                                @Query("pageSize") int pageSize);


    @GET("personalMessage/chat/getChatByPlid.php")
    Observable<BaseEntity<Chat>> getChatByPlid(@Query("uid") int uid,
                                          @Query("plid") int plid,
                                          @Query("token") String token);


    /**
     * 对话设置已读
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("personalMessage/chat/setRead.php")
    Observable<BaseEntity> setRead(@Body RequestBody json);


    /**
     * 获取单个对话中有多少新消息
     *
     * @param uid
     * @param plid
     * @param lastpmid
     * @param token
     * @return
     */
    @GET("personalMessage/check/newByPlid.php")
    Observable<BaseEntity> checkNewMsgBypid(@Query("uid") int uid,
                                            @Query("plid") int plid,
                                            @Query("lastpmid") int lastpmid,
                                            @Query("token") String token);


    /**
     * 获取个人有多少新消息
     *
     * @param uid
     * @return
     */
    @GET("personalMessage/check/newByUid.php")
    Observable<BaseEntity<NewMsg>> checkNewMsgByUid(@Query("uid") int uid);


    /**
     * 回复消息
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("personalMessage/message/reply.php")
    Observable<BaseEntity> replyMessage(@Body RequestBody json);




    /**
     * 删除消息
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("personalMessage/message/delete.php")
    Observable<BaseEntity> deleteMessage(@Body RequestBody json);


    /**
     * 根据条目数来去消息
     *
     * @param uid
     * @param plid
     * @param type
     * @param pmid
     * @param offset
     * @param token
     * @return
     */
    @GET("personalMessage/message/getByOffset.php")
    Observable<BaseEntity<List<Message>>> getMsgByOffset(@Query("uid") int uid,
                                                         @Query("plid") int plid,
                                                         @Query("type") int type,
                                                         @Query("pmid") int pmid,
                                                         @Query("offset") int offset,
                                                         @Query("token") String token);


    /**
     * 根据分页来获取消息
     *
     * @param uid
     * @param plid
     * @param type
     * @param pmid
     * @param pageIndex
     * @param pageSize
     * @param token
     * @return
     */
    @GET("personalMessage/message/getByPlid.php")
    Observable<BaseEntity<List<Message>>> getMsgByPlid(@Query("uid") int uid,
                                                         @Query("plid") int plid,
                                                         @Query("type") int type,
                                                         @Query("pmid") int pmid,
                                                         @Query("pageIndex") int pageIndex,
                                                         @Query("pageSize") int pageSize,
                                                         @Query("token") String token);





















}
