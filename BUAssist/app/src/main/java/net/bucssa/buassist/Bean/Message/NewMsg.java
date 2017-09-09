package net.bucssa.buassist.Bean.Message;

/**
 * Created by KimuraShin on 17/7/27.
 */

public class NewMsg {


    /**
     * hasNewMessage : false
     * countNewMessage : 0
     */

    private boolean hasNewMessage;
    private int countNewMessage;

    public boolean isHasNewMessage() {
        return hasNewMessage;
    }

    public void setHasNewMessage(boolean hasNewMessage) {
        this.hasNewMessage = hasNewMessage;
    }

    public int getCountNewMessage() {
        return countNewMessage;
    }

    public void setCountNewMessage(int countNewMessage) {
        this.countNewMessage = countNewMessage;
    }
}
