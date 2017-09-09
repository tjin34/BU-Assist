package net.bucssa.buassist.Api;


import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Friend.Friend;
import net.bucssa.buassist.Bean.Login.UserInfo;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by LiCola on  2016/05/23  21:06
 */

public interface UserAPI {

//
//    /**
//     * 用户登录并获取数据的接口
//     *
//     * @param useracc
//     * @param userpw
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("user/login.php")
//    Observable<BaseEntity<UserInfo>> login(@Field("useracc") String useracc, @Field("userpw") String userpw);
//
//    /**
//     * 自动保持登录的验证
//     *
//     * @param uid
//     * @param token
//     * @return
//     */
////    @FormUrlEncoded
//    @GET("user/getMyInfos.php")
//    Observable<BaseEntity<UserInfo>> getMyInfos(@Query("uid") int uid, @Query("token") String token);
//
//
//
//    /**
//     * 修改个人信息JSON格式
//     *
//     * @param json
//     * @return
//     */
//    @Headers({"Content-Type: application/json","Accept: application/json"})
//    @POST("user/editUserInfo.php")
//    Observable<BaseEntity> editUserInfos(@Body RequestBody json);


    /**
     * 用户登录并获取数据的接口
     *
     * @param useracc
     * @param userpw
     * @return
     */
    @FormUrlEncoded
    @POST("user/account/login.php")
    Observable<BaseEntity<UserInfo>> login(@Field("useracc") String useracc, @Field("userpw") String userpw);



    /**
     * 修改密码
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("user/account/editPwd.php")
    Observable<BaseEntity> editPwd(@Body RequestBody json);



    /**
     * 添加好友
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("user/friend/add.php")
    Observable<BaseEntity> addFriend(@Body RequestBody json);


    /**
     * 删除好友
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("user/friend/delete.php")
    Observable<BaseEntity> deleteFriend(@Body RequestBody json);



    /**
     * 获取好友列表
     *
     * @param uid
     * @param pageIndex
     * @param pageSize
     * @param token
     * @return
     */
//    @FormUrlEncoded
    @GET("user/friend/getFriends.php")
    Observable<BaseEntity<List<Friend>>> getFriends(@Query("uid") int uid,
                                                    @Query("page") int pageIndex,
                                                    @Query("pageSize") int pageSize,
                                                    @Query("token") String token);



        /**
     * 自动保持登录的验证
     *
     * @param uid
     * @param token
     * @return
     */
//    @FormUrlEncoded
    @GET("user/userinfo/getMyInfos.php")
    Observable<BaseEntity<UserInfo>> getMyInfos(@Query("uid") int uid, @Query("token") String token);

    /**
     * 修改文字的个人信息
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("user/userinfo/editInfoStr.php")
    Observable<BaseEntity> editInfoStr(@Body RequestBody json);




    /**
     * 修改数字的个人信息
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("user/userinfo/editInfoInt.php")
    Observable<BaseEntity> editInfoInt(@Body RequestBody json);




    /**
     * 修改生日
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("user/userinfo/editDoB.php")
    Observable<BaseEntity> editDoB(@Body RequestBody json);



    /**
     * 修改头像
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("user/userinfo/uploadAvatar.php")
    Observable<BaseEntity> uploadAvatar(@Body RequestBody json);



    /**
     * 修改个人信息JSON格式
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("user/userinfo/zeditUserInfo.php")
    Observable<BaseEntity> editUserInfos(@Body RequestBody json);





}
