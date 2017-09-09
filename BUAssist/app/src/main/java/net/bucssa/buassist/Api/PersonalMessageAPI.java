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

//    @GET("pm/checkNewMessage.php")
//    Observable<BaseEntity<NewMsg>> checkNewMsg(@Query("uid") int uid);
//
//    @GET("pm/checkNewMessageByplid.php")
//    Observable<BaseEntity> checkNewMsgBypid(@Query("uid") int uid,
//                                            @Query("plid") int plid,
//                                            @Query("lastpmid") int lastpmid,
//                                            @Query("token") String token);
//
//    @GET("pm/getChats.php")
//    Observable<BaseEntity<List<Chat>>> getChats(@Query("uid") int uid,
//                                                   @Query("token") String token,
//                                                   @Query("pageIndex") int pageIndex,
//                                                   @Query("pageSize") int pageSize);
//
//    @GET("pm/getMsgByOffset.php")
//    Observable<BaseEntity<List<Message>>> getMsgByOffset(@Query("uid") int uid,
//                                                         @Query("plid") int plid,
//                                                         @Query("type") int type,
//                                                         @Query("pmid") int pmid,
//                                                         @Query("offset") int offset,
//                                                         @Query("token") String token);
//
//
//    @Headers({"Content-Type: application/json","Accept: application/json"})
//    @POST("pm/replyMessage.php")
//    Observable<BaseEntity> replyMessage(@Body RequestBody json);
//
//
//    @Headers({"Content-Type: application/json","Accept: application/json"})
//    @POST("pm/sendMessage.php")
//    Observable<BaseEntity> sendMessage(@Body RequestBody json);


    /**
     * 创建新的对话
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("pm/chat/create.php")
    Observable<BaseEntity> createChat(@Body RequestBody json);


    /**
     * 删除对话
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("pm/chat/delete.php")
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
    @GET("pm/chat/get.php")
    Observable<BaseEntity<List<Chat>>> getChats(@Query("uid") int uid,
                                                @Query("token") String token,
                                                @Query("pageIndex") int pageIndex,
                                                @Query("pageSize") int pageSize);


    /**
     * 对话设置已读
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("pm/chat/setRead.php")
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
    @GET("pm/check/newByPlid.php")
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
    @GET("pm/check/newByUid.php")
    Observable<BaseEntity<NewMsg>> checkNewMsgByUid(@Query("uid") int uid);


    /**
     * 回复消息
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("pm/message/reply.php")
    Observable<BaseEntity> replyMessage(@Body RequestBody json);




    /**
     * 删除消息
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("pm/message/delete.php")
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
    @GET("pm/message/getByOffset.php")
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
    @GET("pm/message/getByPlid.php")
    Observable<BaseEntity<List<Message>>> getMsgByPlid(@Query("uid") int uid,
                                                         @Query("plid") int plid,
                                                         @Query("type") int type,
                                                         @Query("pmid") int pmid,
                                                         @Query("pageIndex") int pageIndex,
                                                         @Query("pageSize") int pageSize,
                                                         @Query("token") String token);





















}
