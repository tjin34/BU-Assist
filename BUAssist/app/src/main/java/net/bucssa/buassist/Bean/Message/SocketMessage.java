package net.bucssa.buassist.Bean.Message;

/**
 * Created by KimuraShin on 17/7/25.
 */

public class SocketMessage {


    /**
     * text : En
     * user : {"_id":25,"avatar":"http:/bucssa.net/uc_server/avatar.php?uid=25&size=small"}
     * createdAt : 2017-07-25T06:01:30.106Z
     * _id : aa910ad7-a64e-409e-8242-4eb307270dd5
     * roomId : room-6
     */

    private String text;
    private UserBean user;
    private String createdAt;
    private String _id;
    private String roomId;

    public SocketMessage(String text, int uid, String avatar, String createAt, String id, String roomId) {
        setRoomId(roomId);
        setText(text);
        setCreatedAt(createAt);
        set_id(id);
        UserBean userBean = new UserBean();
        userBean.set_id(uid);
        userBean.setAvatar(avatar);
        setUser(userBean);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public static class UserBean {
        /**
         * _id : 25
         * avatar : http:/bucssa.net/uc_server/avatar.php?uid=25&size=small
         */

        private int _id;
        private String avatar;

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
