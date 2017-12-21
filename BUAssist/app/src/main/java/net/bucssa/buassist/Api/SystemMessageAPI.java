package net.bucssa.buassist.Api;

import net.bucssa.buassist.Bean.BaseEntity;
import net.bucssa.buassist.Bean.Message.Chat;
import net.bucssa.buassist.Bean.Message.SystemNotification;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tjin3 on 2017/11/13.
 */

public interface SystemMessageAPI {

    /**
     * 获取系统消息列表
     *
     * @param uid
     * @param token
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GET("systemMessage/message/getByUid.php")
    Observable<BaseEntity<List<SystemNotification>>> getChats(@Query("uid") int uid,
                                                              @Query("token") String token,
                                                              @Query("pageIndex") int pageIndex,
                                                              @Query("pageSize") int pageSize);
}
