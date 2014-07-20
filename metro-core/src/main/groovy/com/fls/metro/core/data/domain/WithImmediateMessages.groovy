package com.fls.metro.core.data.domain
/**
 * User: NFadin
 * Date: 08.05.14
 * Time: 13:06
 */
public interface WithImmediateMessages {
    List<ImmediateMessage> getMessages()
    void setMessages(List<ImmediateMessage> messages)
}