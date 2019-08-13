package com.rushikesh.auxilio;

import java.util.Date;

/**
 * Created by rushi on 07-Oct-18.
 */

public class msg {
    private String messageText;
    private String messageUser;
    private long messageTime;

    public msg(String messageText,String messageUser)
    {
        this.messageText=messageText;
        this.messageUser=messageUser;

        messageTime=new Date().getTime();
    }

    public msg() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
