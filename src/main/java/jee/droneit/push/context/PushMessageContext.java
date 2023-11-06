package jee.droneit.push.context;

import jee.droneit.push.dto.Message;
import lombok.extern.java.Log;
import jee.droneit.push.dto.Message;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;


@ApplicationScoped
@Log
public class PushMessageContext
{

    /**
     * Channel for sending message to all active sessions.
     */
    @Inject
    @Push(channel = "broadcastChannel")
    private PushContext broadcastChannel;

    @Inject
    @Push
    private PushContext userChannel;


    /**
     * Send push message to all users.
     *
     * @param message message to be sent
     */
    public void notifyAll(Message message) {
        broadcastChannel.send(message);
    }

    public void notifyUser(Message message, String username) {
        userChannel.send(message, username);
    }



}
