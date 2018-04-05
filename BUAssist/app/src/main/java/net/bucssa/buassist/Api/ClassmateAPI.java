package net.bucssa.buassist.Api;

import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Classmate.Class;
import net.bucssa.buassist.Bean.Classmate.Comment;
import net.bucssa.buassist.Bean.Classmate.Group;
import net.bucssa.buassist.Bean.Classmate.Member;
import net.bucssa.buassist.Bean.Classmate.Post;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Shinji on 2017/12/21.
 */

public interface ClassmateAPI {

    /**
     * 获取课程列表，实时筛选
     *
     * @param pageIndex
     * @param pageSize
     * @param key
     * @return
     */
    @GET("classmate/class/get.php")
    Observable<BaseEntity<List<Class>>> getClassList(@Query("pageIndex") int pageIndex,
                                                     @Query("pageSize") int pageSize,
                                                     @Query("key") String key);

    /* ------------------- */
    /* Collection 相关接口 */
    /* ------------------- */

    /**
     * 获取课程列表，实时筛选
     *
     * @param uid
     * @param pageIndex
     * @param pageSize
     * @param token
     * @return
     */
    @GET("classmate/class/getClassCollection.php")
    Observable<BaseEntity<List<Class>>> getClassCollection(@Query("uid") int uid,
                                                           @Query("pageIndex") int pageIndex,
                                                           @Query("pageSize") int pageSize,
                                                           @Query("token") String token);

    /**
     * 添加课程收藏
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("classmate/class/addClassCollection.php")
    Observable<BaseEntity> addClassCollection(@Body RequestBody json);

    /**
     * 删除课程收藏
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("classmate/class/deleteClassCollection.php")
    Observable<BaseEntity> deleteClassCollection(@Body RequestBody json);


    /* ------------- */
    /* Post 相关接口 */
    /* ------------- */

    /**
     * 获取帖子
     *
     * @param uid
     * @param  classid
     * @param pageIndex
     * @param pageSize
     * @param token
     * @return
     */
    @GET("classmate/post/get.php")
    Observable<BaseEntity<List<Post>>> getPost(@Query("uid") int uid,
                                               @Query("classid") int classid,
                                               @Query("pageIndex") int pageIndex,
                                               @Query("pageSize") int pageSize,
                                               @Query("token") String token);

    /**
     * 新建帖子
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("classmate/post/create.php")
    Observable<BaseEntity> createPost(@Body RequestBody json);


    /**
     * 获取回复列表
     *
     * @param uid
     * @param postid
     * @param pageIndex
     * @param pageSize
     * @param token
     * @return
     */
    @GET("classmate/post/getComment.php")
    Observable<BaseEntity<List<Comment>>> getComment(@Query("uid") int uid,
                                                     @Query("postid") int postid,
                                                     @Query("pageIndex") int pageIndex,
                                                     @Query("pageSize") int pageSize,
                                                     @Query("token") String token);

    /**
     * 新建回复
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("classmate/post/makeComment.php")
    Observable<BaseEntity> makeComment(@Body RequestBody json);

    /**
     * 删除回复
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("classmate/post/deleteComment.php")
    Observable<BaseEntity> deleteComment(@Body RequestBody json);




    /* -------------- */
    /* Group 相关接口 */
    /* -------------- */


    /**
     * 新建小组
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("classmate/group/create.php")
    Observable<BaseEntity> createGroup(@Body RequestBody json);


    /**
     * 获取回复列表
     *
     * @param uid
     * @param classCode
     * @param pageIndex
     * @param pageSize
     * @param token
     * @return
     */
    @GET("classmate/group/get.php")
    Observable<BaseEntity<List<Group>>> getGroup(@Query("uid") int uid,
                                                 @Query("classCode") String classCode,
                                                 @Query("pageIndex") int pageIndex,
                                                 @Query("pageSize") int pageSize,
                                                 @Query("token") String token);

    /**
     * 申请入群
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("classmate/group/requestJoin.php")
    Observable<BaseEntity> requestJoin(@Body RequestBody json);

    /**
     * 处理入群申请
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("classmate/group/handleRequest.php")
    Observable<BaseEntity> handleJoin(@Body RequestBody json);


    /**
     * 邀请入群
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("classmate/group/inviteJoin.php")
    Observable<BaseEntity> groupInvite(@Body RequestBody json);


    /**
     * 处理入群邀请
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("classmate/group/handleInvite.php")
    Observable<BaseEntity> handleInvite(@Body RequestBody json);


    /**
     * 离开小组
     *
     * @param json
     * @return
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("classmate/group/leave.php")
    Observable<BaseEntity> leaveGroup(@Body RequestBody json);


    /**
     * 获取小组成员
     *
     * @param groupid
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GET("classmate/group/member.php")
    Observable<BaseEntity<List<Member>>> getMember(@Query("groupid") int groupid,
                                                   @Query("pageIndex") int pageIndex,
                                                   @Query("pageSize") int pageSize);

}
